package backend.k_interview.domain.member.domain.repository;

import backend.k_interview.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
