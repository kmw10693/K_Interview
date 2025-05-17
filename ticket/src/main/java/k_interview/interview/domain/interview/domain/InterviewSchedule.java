package k_interview.interview.domain.interview.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class InterviewSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String interviewName;
    private String location;
    private LocalDate interviewDate;
    private int totalSlots;

    @Builder.Default
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterviewSlot> slots = new ArrayList<>();

    public void addSlot(InterviewSlot slot) {
        this.slots.add(slot);
        slot.setSchedule(this);
    }
}
