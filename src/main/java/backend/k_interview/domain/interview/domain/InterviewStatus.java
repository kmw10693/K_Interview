package backend.k_interview.domain.interview.domain;

import lombok.Getter;

@Getter
public enum InterviewStatus {
    RESERVED("예약"), CANCELED("취소");

    private String name;

    InterviewStatus(String name) {
        this.name = name;
    }
}
