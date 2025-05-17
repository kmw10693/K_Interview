package k_interview.interview.domain.interview.service;

import k_interview.interview.domain.interview.domain.InterviewSchedule;
import k_interview.interview.domain.interview.domain.InterviewSlot;
import k_interview.interview.domain.interview.domain.repository.InterviewScheduleRepsitory;
import k_interview.interview.domain.interview.domain.repository.InterviewSlotRepository;
import k_interview.interview.domain.interview.dto.InterviewScheduleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class InterviewScheduleIntegrationTest {

    @Autowired
    private InterviewReservationService interviewScheduleService;

    @Autowired
    private InterviewScheduleRepsitory interviewScheduleRepository;

    @Autowired
    private InterviewSlotRepository interviewSlotRepository;

    @BeforeEach
    void setUp() {
        InterviewSchedule schedule = InterviewSchedule.builder()
                .interviewName("백엔드")
                .location("A동 302호")
                .interviewDate(LocalDate.of(2025, 6, 1))
                .totalSlots(2)
                .build();

        InterviewSlot reservedSlot = InterviewSlot.builder()
                .time(LocalTime.of(10, 0))
                .isReserved(true)
                .build();

        InterviewSlot unreservedSlot = InterviewSlot.builder()
                .time(LocalTime.of(10, 30))
                .isReserved(false)
                .build();

        schedule.addSlot(reservedSlot);
        schedule.addSlot(unreservedSlot);

        interviewScheduleRepository.save(schedule);
    }

    @Test
    void 면접_일정_조회_및_예약_슬롯_카운트_검증() {
        // when
        List<InterviewScheduleResponse> results = interviewScheduleService.getSchedules(
                LocalDate.of(2025, 6, 1), "백엔드"
        );

        // then
        assertEquals(1, results.size());

        InterviewScheduleResponse res = results.get(0);
        assertEquals("백엔드", res.interviewName());
        assertEquals("A동 302호", res.location());
        assertEquals(2, res.totalSlots());
        assertEquals(1, res.reservedSlots()); // isReserved == true 인 슬롯 1개
    }
}
