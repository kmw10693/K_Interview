package backend.k_interview.domain.interview.service;

import backend.k_interview.domain.interview.domain.InterviewReservation;
import backend.k_interview.domain.interview.domain.InterviewReservedEvent;
import backend.k_interview.domain.interview.domain.InterviewSlot;
import backend.k_interview.domain.interview.domain.repository.InterviewReservationRepository;
import backend.k_interview.domain.interview.domain.repository.InterviewScheduleRepository;
import backend.k_interview.domain.interview.domain.repository.InterviewSlotRepository;
import backend.k_interview.domain.interview.dto.InterviewReservationRequest;
import backend.k_interview.domain.interview.dto.InterviewReservationResponse;
import backend.k_interview.domain.interview.dto.InterviewScheduleResponse;
import backend.k_interview.domain.member.domain.Member;
import backend.k_interview.domain.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static backend.k_interview.domain.interview.domain.InterviewStatus.RESERVED;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class InterviewReservationService {

    private final RedissonClient redissonClient;
    private final InterviewSlotRepository slotRepository;
    private final MemberRepository memberRepository;
    private final InterviewReservationRepository reservationRepository;
    private final InterviewScheduleRepository interviewScheduleRepository;
    private final RabbitTemplate rabbitTemplate;

    private static final String QUEUE_NAME = "interview.reserved";

    @Transactional
    public InterviewReservationResponse reserveSlot(InterviewReservationRequest reservationRequest) {
        String lockKey = "lock:slot:" + reservationRequest.slotId();

        RLock lock = redissonClient.getLock(lockKey);
        boolean isLocked = false;

        try {
            // Redisson Pub/Sub 기반 분산 락 획득 시도 (최대 5초 대기, 5초 유지)
            isLocked = lock.tryLock(5, 5, TimeUnit.SECONDS);

            if (!isLocked) {
                throw new IllegalStateException("다른 사용자가 같은 시간대 면접을 예약 중입니다. 잠시 후 다시 시도해주세요.");
            }

            InterviewSlot slot = slotRepository.findById(reservationRequest.slotId())
                    .orElseThrow(() -> new IllegalStateException("해당 면접 시간대를 찾을 수 없습니다."));

            if (slot.isReserved()) {
                throw new IllegalStateException("이미 예약된 면접 시간입니다.");
            }

            // 예약 처리
            slot.setReserved(true);
            slotRepository.save(slot);

            Member member = memberRepository.findById(reservationRequest.memberId())
                    .orElseThrow(() -> new IllegalStateException("해당 멤버를 찾을 수 없습니다."));

            InterviewReservation reservation = InterviewReservation.builder()
                    .member(member)
                    .slot(slot)
                    .status(RESERVED)
                    .reservedAt(LocalDateTime.now())
                    .build();

            reservationRepository.save(reservation);

            // 메시지 큐 발행
            InterviewReservedEvent event = InterviewReservedEvent.builder()
                    .reservationId(reservation.getId())
                    .memberId(member.getId())
                    .email(member.getEmail())
                    .time(slot.getTime())
                    .build();

            rabbitTemplate.convertAndSend(QUEUE_NAME, event);

            return new InterviewReservationResponse(reservation.getId(), "면접 예약이 완료되었습니다.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("락 획득 중 인터럽트 발생", e);
        } finally {
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public List<InterviewScheduleResponse> getSchedules(LocalDate date, String search) {
        return interviewScheduleRepository.findByDateAndInterviewName(date, search).stream()
                .map(s -> new InterviewScheduleResponse(
                        s.getId(),
                        s.getInterviewName(),
                        s.getLocation(),
                        s.getDate()
                ))
                .toList();
    }
}
