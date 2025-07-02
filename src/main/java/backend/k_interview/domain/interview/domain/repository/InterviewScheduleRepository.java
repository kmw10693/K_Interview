package backend.k_interview.domain.interview.domain.repository;

import backend.k_interview.domain.interview.domain.InterviewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Long> {

    List<InterviewSchedule> findByDateAndInterviewName(LocalDate date, String search);
}
