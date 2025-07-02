package backend.k_interview.domain.interview.dto;

import java.time.LocalDate;

public record InterviewScheduleResponse(
        Long id,
        String interviewName,
        String location,
        LocalDate interviewDate
) {
}
