package k_interview.interview.domain.interview.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import k_interview.interview.domain.interview.dto.InterviewReservationRequest;
import k_interview.interview.domain.interview.presentation.InterviewScheduleController;
import k_interview.interview.domain.interview.service.InterviewReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InterviewScheduleController.class)
@AutoConfigureMockMvc
class InterviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InterviewReservationService interviewReservationService;

    @MockitoBean
    private KafkaTemplate<String, InterviewReservationRequest> kafkaTemplate;

    @Test
    void 예약요청을_kafka로_전송한다() throws Exception {
        InterviewReservationRequest request = new InterviewReservationRequest(1L, 42L, 1L);

        // JSON 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isAccepted());

        // Kafka 메시지가 보내졌는지 검증
        verify(kafkaTemplate, times(1)).send(eq("interview-reservation-topic"), eq(request));
    }
}

