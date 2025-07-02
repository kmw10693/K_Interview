package backend.k_interview.global.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendInterviewReservationMail(String toEmail, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("면접 예약 완료 안내");
            message.setText(content);

            mailSender.send(message);
            log.info("이메일 발송 성공: {}", toEmail);
        } catch (Exception e) {
            log.error("이메일 발송 실패: {}", toEmail, e);
        }
    }
}