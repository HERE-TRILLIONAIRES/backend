package com.trillionares.tryit.notification.application.service;

import com.trillionares.tryit.notification.application.dto.NotificationResponse;
import com.trillionares.tryit.notification.application.dto.slack.SlackNotificationSender;
import com.trillionares.tryit.notification.domain.model.Notification;
import com.trillionares.tryit.notification.domain.model.NotificationStatus;
import com.trillionares.tryit.notification.infrastructure.messaging.event.SubmissionKafkaEvent;
import com.trillionares.tryit.notification.infrastructure.persistence.NotificationRepository;
import com.trillionares.tryit.notification.libs.client.auth.AuthServiceClient;
import com.trillionares.tryit.notification.libs.client.auth.UserResponseDto;
import com.trillionares.tryit.notification.libs.exception.ErrorCode;
import com.trillionares.tryit.notification.libs.exception.ExceptionConverter;
import com.trillionares.tryit.notification.libs.exception.GlobalException;
import com.trillionares.tryit.notification.presentation.dto.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

  private final ExceptionConverter exceptionConverter;
  private final NotificationRepository notificationRepository;
  private final SlackNotificationSender slackNotificationSender;
  private final AuthServiceClient authServiceClient;
  @Transactional
  public void createNotificationFromSubmissionEvent(SubmissionKafkaEvent event) {

    // 알림 엔티티 저장
    Notification notification = convertEventToNotification(event);
    Notification savedNotification = notificationRepository.save(notification);

    String slackId = getSlackId(savedNotification);

    // 슬랙 알림 발송
    slackNotificationSender.sendNotification(savedNotification, slackId, event.getStatus());
  }

  private String getSlackId(Notification notification) {

    BaseResponse<UserResponseDto> response = authServiceClient.getUserInfo(
        notification.getUserId());

    UserResponseDto userDto = response.getData();
    String slackId = userDto.getSlackId();

    log.info("slackId {}", slackId);

    if (slackId == null || slackId.isEmpty()) { // TODO: Feign error 추가 필요
      throw new GlobalException(ErrorCode.SLACK_ID_NOT_FOUND);
    }

    return slackId;
  }

  private Notification convertEventToNotification(SubmissionKafkaEvent event) {

    return Notification.builder()
        .userId(event.getUserId())
        .submissionId(event.getSubmissionId())
        .build();
  }

  @Transactional(readOnly = true)
  public NotificationResponse getNotification(UUID notificationId) {

    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> new GlobalException(ErrorCode.NOTIFICATION_NOT_FOUND));

    return NotificationResponse.from(notification);
  }

  @Transactional(readOnly = true)
  public Page<NotificationResponse> getNotificationByStatus(NotificationStatus status,
      Pageable pageable) {

    return notificationRepository.findByNotificationStatus(status, pageable);
  }
}
