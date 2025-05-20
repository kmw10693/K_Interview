package k_interview.interview.domain.interview.presentation;

import k_interview.interview.domain.interview.dto.InterviewReservationRequest;
import k_interview.interview.domain.interview.dto.InterviewReservationResponse;
import k_interview.interview.domain.interview.dto.InterviewScheduleResponse;
import k_interview.interview.domain.interview.service.InterviewReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InterviewScheduleController {

    private final InterviewReservationService scheduleService;
    private final KafkaTemplate<String, InterviewReservationRequest> kafkaTemplate;

    @GetMapping("/schedules")
    public ResponseEntity<List<InterviewScheduleResponse>> getSchedules(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam(required = false) String search) {
        return ResponseEntity.ok(scheduleService.getSchedules(date, search));
    }

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveInterview(@RequestBody InterviewReservationRequest request) {
        kafkaTemplate.send("interview-reservation-topic", request);
        return ResponseEntity.accepted().body("면접 예약 요청이 접수되었습니다.");
    }
}
