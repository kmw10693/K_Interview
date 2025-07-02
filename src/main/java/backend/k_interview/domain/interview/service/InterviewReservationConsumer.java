package backend.k_interview.domain.interview.service;

import backend.k_interview.domain.interview.domain.InterviewReservedEvent;
import backend.k_interview.global.mail.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class InterviewReservationConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = "interview.reserved")
    public void handleInterviewReservedEvent(InterviewReservedEvent event) {
        log.info("예약 이벤트 수신: {}", event);

        String formattedTime = event.getTime()
                .format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"));

        String content = String.format("""
                안녕하세요, KUIT IT 동아리입니다.
                
                면접 예약이 완료되었습니다.
                
                [면접일시] %s
                [예약번호] %d
                
                면접 당일 10분 전까지 입장해 주세요.
                감사합니다.
                """, formattedTime, event.getReservationId());

        emailService.sendInterviewReservationMail(event.getEmail(), content);

    }
}
