package com.trillionares.tryit.notification.infrastructure.messaging.event;

import com.trillionares.tryit.notification.application.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmissionEventConsumer {

  private final NotificationService notificationService;

  @KafkaListener(
      topics = "tryit-completed",
      groupId = "${spring.kafka.consumer.group-id}"
  )

  public void handleSubmissionEvent(SubmissionKafkaEvent event) {
    log.info("Received Kafka message: {}", event);
    //TODO: 예외처리
    notificationService.createNotificationFromSubmissionEvent(event);
  }
}
