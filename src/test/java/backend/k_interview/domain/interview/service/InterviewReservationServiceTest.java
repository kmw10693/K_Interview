package backend.k_interview.domain.interview.service;

import backend.k_interview.domain.interview.domain.InterviewReservation;
import backend.k_interview.domain.interview.domain.InterviewSlot;
import backend.k_interview.domain.interview.domain.repository.InterviewReservationRepository;
import backend.k_interview.domain.interview.domain.repository.InterviewSlotRepository;
import backend.k_interview.domain.interview.dto.InterviewReservationRequest;
import backend.k_interview.domain.member.domain.Member;
import backend.k_interview.domain.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InterviewReservationServiceTest {

    @Autowired
    private InterviewReservationService reservationService;

    @Autowired
    private InterviewSlotRepository slotRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private InterviewReservationRepository reservationRepository;

    private Long testSlotId;
    private Long testMemberId;

    @BeforeEach
    @Transactional
    public void setUp() {
        // 테스트용 Slot과 Member 저장
        InterviewSlot slot = InterviewSlot.builder()
                .time(LocalTime.now())
                .isReserved(false)
                .build();
        slotRepository.save(slot);
        testSlotId = slot.getId();

        Member member = Member.builder()
                .email("test@konkuk.ac.kr")
                .build();
        memberRepository.save(member);
        testMemberId = member.getId();
    }

    @Test
    public void testConcurrentReservations() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    InterviewReservationRequest request = new InterviewReservationRequest(testSlotId, testMemberId);
                    reservationService.reserveSlot(request);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println("실패: " + e.getMessage());
                    failureCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failureCount.get()).isEqualTo(threadCount - 1);
    }
}