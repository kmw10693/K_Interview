package k_interview.interview.domain.interview.service;

import k_interview.interview.domain.interview.dto.InterviewReservationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InterviewReservationConsumer {
    private final InterviewReservationService service;

    @KafkaListener(topics = "interview-reservation-topic", groupId = "reservation-group")
    public void handleReservation(InterviewReservationRequest request) {
        try {
            service.reserveSlot(request); // 기존 Redisson 트랜잭션 로직 그대로 재사용
        } catch (Exception e) {
            // 실패 시 로깅 + DLQ 처리 등
            log.error("면접 예약 실패: {}", e.getMessage());
        }
    }
}
