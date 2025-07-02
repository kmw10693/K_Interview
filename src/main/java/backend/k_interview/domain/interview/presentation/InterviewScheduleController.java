package backend.k_interview.domain.interview.presentation;

import backend.k_interview.domain.interview.dto.InterviewReservationRequest;
import backend.k_interview.domain.interview.dto.InterviewScheduleResponse;
import backend.k_interview.domain.interview.service.InterviewReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InterviewScheduleController {

    private final InterviewReservationService interviewReservationService;

    @GetMapping("/schedules")
    public ResponseEntity<List<InterviewScheduleResponse>> getSchedules(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam(required = false) String search) {
        return ResponseEntity.ok(interviewReservationService.getSchedules(date, search));
    }

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveInterview(@RequestBody InterviewReservationRequest request) {
        interviewReservationService.reserveSlot(request);
        return ResponseEntity.accepted().body("면접 예약 요청이 접수되었습니다.");
    }

}
