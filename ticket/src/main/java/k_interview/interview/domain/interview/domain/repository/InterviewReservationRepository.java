package k_interview.interview.domain.interview.domain.repository;

import k_interview.interview.domain.interview.domain.InterviewReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewReservationRepository extends JpaRepository<InterviewReservation, Long> {
}
