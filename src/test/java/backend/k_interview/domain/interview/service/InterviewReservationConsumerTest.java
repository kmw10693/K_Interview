package backend.k_interview.domain.interview.service;

import backend.k_interview.domain.interview.domain.InterviewReservedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@SpringBootTest
class InterviewReservationConsumerTest {

    @MockitoBean
    private InterviewReservationConsumer consumer;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testMessageIsReserved() throws Exception {

        //given
        InterviewReservedEvent event = InterviewReservedEvent.builder()
                .id(1L)
                .reservationId(1L)
                .memberId(101L)
                .email("mock@example.com")
                .time(LocalTime.now())
                .build();

        //when
        rabbitTemplate.convertAndSend("interview.reserved", event);

        //then
        Thread.sleep(500);
        verify(consumer, atLeastOnce()).handleInterviewReservedEvent(any());

    }
}