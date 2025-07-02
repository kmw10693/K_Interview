package backend.k_interview.domain.interview.domain.repository;

import backend.k_interview.domain.interview.domain.InterviewSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewSlotRepository extends JpaRepository<InterviewSlot, Long> {
}
