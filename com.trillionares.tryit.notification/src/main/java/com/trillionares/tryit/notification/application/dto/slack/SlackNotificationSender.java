package com.trillionares.tryit.notification.application.dto.slack;

import com.trillionares.tryit.notification.domain.model.Notification;
import com.trillionares.tryit.notification.infrastructure.persistence.NotificationRepository;
import com.trillionares.tryit.notification.libs.exception.ExceptionConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackNotificationSender {

  private final ExceptionConverter exceptionConverter;
  @Value("${slack.webhook.url}")
  private String webhookUrl;

  private final RestTemplate restTemplate;
  private final NotificationRepository notificationRepository;

  @Transactional
  public void sendNotification(Notification notification, String slackId, String status) {

    SlackMessage message = SlackMessage.from(notification, slackId, status); // 슬랙 메세지 생성

    try {
      restTemplate.postForEntity(webhookUrl, message, String.class);

      notification.markAsDelivered();
      notificationRepository.save(notification);

      log.info("Slack notification sent successfully: {}", notification.getNotificationId());

    } catch (Exception e) {
      notification.increaseAttemptCount();
      notificationRepository.save(notification);

      log.error("Failed to send Slack notification: {}", e.getMessage());
       throw exceptionConverter.convertToBaseException(e);
    }
  }
}
