package k_interview.interview.domain.interview.service;

import k_interview.interview.domain.interview.domain.InterviewReservation;
import k_interview.interview.domain.interview.domain.InterviewSlot;
import k_interview.interview.domain.interview.domain.repository.InterviewReservationRepository;
import k_interview.interview.domain.interview.domain.repository.InterviewScheduleRepsitory;
import k_interview.interview.domain.interview.domain.repository.InterviewSlotRepository;
import k_interview.interview.domain.interview.dto.InterviewReservationRequest;
import k_interview.interview.domain.interview.dto.InterviewReservationResponse;
import k_interview.interview.domain.interview.dto.InterviewScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InterviewReservationService {

    private final RedissonClient redissonClient;
    private final InterviewSlotRepository slotRepository;
    private final InterviewReservationRepository reservationRepository;
    private final InterviewScheduleRepsitory interviewScheduleRepsitory;

    @Transactional
    public InterviewReservationResponse reserveSlot(InterviewReservationRequest request) {
        String lockKey = "lock:slot:" + request.slotId();
        RLock lock = redissonClient.getLock(lockKey);

        boolean isLocked = false;
        try {
            isLocked = lock.tryLock(5, 3, TimeUnit.SECONDS);

            if (!isLocked) {
                throw new IllegalStateException("다른 사용자가 같은 시간대 면접을 예약 중입니다.");
            }

            InterviewSlot slot = slotRepository.findById(request.slotId())
                    .orElseThrow(() -> new IllegalStateException("이미 예약된 면접 시간입니다."));

            if (slot.isReserved()) {
                throw new IllegalStateException("이미 예약된 면접 시간입니다.");
            }

            // 예약 처리
            slot.setReserved(true);
            slotRepository.save(slot);

            InterviewReservation reservation = InterviewReservation.builder()
                    .userId(request.userId())
                    .slot(slot)
                    .status("RESERVED")
                    .reservedAt(LocalDateTime.now())
                    .build();

            reservationRepository.save(reservation);

            return new InterviewReservationResponse(reservation.getId(), "면접 예약이 완료되었습니다.");

        } catch (InterruptedException e) {
            throw new RuntimeException("락 획득 중 인터럽트 발생", e);
        } finally {
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public List<InterviewScheduleResponse> getSchedules(LocalDate date, String search) {

        return interviewScheduleRepsitory.findByInterviewDateAndInterviewName(date, search).stream()
                .map(s -> {
                    long reservedCount = s.getSlots().stream().filter(InterviewSlot::isReserved).count();
                    return new InterviewScheduleResponse(
                            s.getId(),
                            s.getInterviewName(),
                            s.getLocation(),
                            s.getInterviewDate(),
                            s.getTotalSlots(),
                            (int) reservedCount
                    );
                })
                .toList();
    }
}

