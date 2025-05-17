package k_interview.interview.domain.interview.dto;

import java.time.LocalDate;

public record InterviewScheduleResponse(
        Long id,
        String interviewName,
        String location,
        LocalDate interviewDate,
        int totalSlots,
        int reservedSlots
) {}