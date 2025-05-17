package k_interview.interview.domain.interview.domain.repository;

import k_interview.interview.domain.interview.domain.InterviewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InterviewScheduleRepsitory extends JpaRepository<InterviewSchedule, Long> {

    List<InterviewSchedule> findByInterviewDateAndInterviewName(LocalDate date, String name);

}
