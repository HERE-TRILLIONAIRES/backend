package com.trillionares.tryit.notification.application.dto;

import com.trillionares.tryit.notification.domain.model.Notification;
import com.trillionares.tryit.notification.domain.model.NotificationStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NotificationResponse {
    private UUID notificationId;
    private UUID userId;
    private UUID submissionId;
    private NotificationStatus notificationStatus;
    private Integer attemptCount;
    private LocalDateTime createdAt;
    private String createdBy;

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .userId(notification.getUserId())
                .submissionId(notification.getSubmissionId())
                .notificationStatus(notification.getNotificationStatus())
                .attemptCount(notification.getAttemptCount())
                .build();
    }
}