package k_interview.interview.domain.interview.service;

import k_interview.interview.domain.interview.domain.InterviewReservation;
import k_interview.interview.domain.interview.domain.InterviewSchedule;
import k_interview.interview.domain.interview.domain.InterviewSlot;
import k_interview.interview.domain.interview.domain.repository.InterviewReservationRepository;
import k_interview.interview.domain.interview.domain.repository.InterviewScheduleRepsitory;
import k_interview.interview.domain.interview.domain.repository.InterviewSlotRepository;
import k_interview.interview.domain.interview.dto.InterviewReservationRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class InterviewReservationConcurrencyTest {

    @Autowired
    private InterviewReservationService reservationService;

    @Autowired
    private InterviewSlotRepository slotRepository;

    @Autowired
    private InterviewReservationRepository reservationRepository;

    @Autowired
    private InterviewScheduleRepsitory interviewScheduleRepsitory;

    @BeforeEach
    void setup() {
        reservationRepository.deleteAll();
        slotRepository.deleteAll();
        interviewScheduleRepsitory.deleteAll();

        InterviewSlot slot = new InterviewSlot();
        slot.setTime(LocalTime.of(14, 0));
        slot.setReserved(false);

        InterviewSchedule schedule = new InterviewSchedule();
        schedule.setInterviewName("백엔드 1차 면접");
        schedule.setLocation("당근 회사");
        schedule.setInterviewDate(LocalDate.now());
        schedule.setTotalSlots(1);
        interviewScheduleRepsitory.save(schedule);

        slot.setSchedule(schedule);
        slotRepository.save(slot);
    }

    @Test
    void 동시_예약_테스트() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Long slotId = slotRepository.findAll().get(0).getId();
        Long scheduleId = slotRepository.findAll().get(0).getSchedule().getId();

        for (int i = 0; i < threadCount; i++) {
            final long userId = i + 1L;

            executor.submit(() -> {
                try {
                    InterviewReservationRequest request = new InterviewReservationRequest(scheduleId, slotId, userId);
                    reservationService.reserveSlot(request);
                    System.out.println("성공한 예약 ID: " + request.userId());
                } catch (Exception e) {
                    // 실패는 예상됨 (락 실패 or 예약 중복)
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        List<InterviewReservation> results = reservationRepository.findAll();

        Assertions.assertEquals(1, results.size());
        Assertions.assertEquals("RESERVED", results.get(0).getStatus());
    }
}
