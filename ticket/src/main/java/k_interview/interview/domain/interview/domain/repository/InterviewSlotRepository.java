package k_interview.interview.domain.interview.domain.repository;

import k_interview.interview.domain.interview.domain.InterviewSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface InterviewSlotRepository extends JpaRepository<InterviewSlot, Long> {
}
