package com.trillionares.tryit.product.domain.model.recruitment;

import com.trillionares.tryit.product.domain.model.recruitment.type.RecruitmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_recruitment", schema = "product")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recruitment {
    //TODO: BaseEntity 상속

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "recruitment_id")
    private UUID recruitmentId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "recruitment_title", nullable = false)
    private String recruitmentTitle;

    @Column(name = "recruitment_description", nullable = false, columnDefinition = "TEXT")
    private String recruitmentDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "recruitment_status", nullable = false)
    private RecruitmentStatus recruitmentStatus;

    @Column(name = "recruitment_start_date", nullable = false)
    private LocalDateTime recruitmentStartDate;

    @Column(name = "recruitment_duration", nullable = false)
    private long recruitmentDuration; // 단위: 밀리초

    @Column(name = "recruitment_end_date", nullable = false)
    private LocalDateTime recruitmentEndDate;

    @Column(name = "current_participants", nullable = false)
    private long currentParticipants = 0;

    @Column(name = "max_participants", nullable = false)
    private Long maxParticipants;

    public void updateRecruitment(String title, String description, LocalDateTime startTime,
                                  Long during, LocalDateTime endTime, Long maxParticipants) {
        if (title != null) {
            this.recruitmentTitle = title;
        }
        if (description != null) {
            this.recruitmentDescription = description;
        }
        if (startTime != null) {
            this.recruitmentStartDate = startTime;
        }
        if (during != null) {
            this.recruitmentDuration = during;
        }
        if (endTime != null) {
            this.recruitmentEndDate = endTime;
        }
        if (maxParticipants != null) {
            this.maxParticipants = maxParticipants;
        }
    }

    public void updateStatus(RecruitmentStatus status) {
        this.recruitmentStatus = status;
    }

    public void updateCurrentParticipants(long currentParticipants) {
        this.currentParticipants = currentParticipants;
    }
}

