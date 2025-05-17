package k_interview.interview.domain.interview.domain;

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

    private Long userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", unique = true)
    private InterviewSlot slot;

    private String status;

    private LocalDateTime reservedAt;
    private LocalDateTime canceledAt;
}

