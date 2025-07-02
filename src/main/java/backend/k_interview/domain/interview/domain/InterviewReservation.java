package backend.k_interview.domain.interview.domain;

import backend.k_interview.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "slot_id")
    private InterviewSlot slot;

    @Enumerated(EnumType.STRING)
    private InterviewStatus status;

    private LocalDateTime reservedAt;

    private LocalDateTime canceledAt;
}
