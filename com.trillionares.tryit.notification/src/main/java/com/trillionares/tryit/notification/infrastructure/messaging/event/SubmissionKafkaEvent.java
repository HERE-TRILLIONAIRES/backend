package com.trillionares.tryit.notification.infrastructure.messaging.event;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SubmissionKafkaEvent {

  private UUID submissionId;
  private UUID userId;
  private UUID recruitmentId;
  private String status;
  private LocalDateTime submissionTime;
  private UUID messageId;
  private LocalDateTime eventTimestamp;

  protected SubmissionKafkaEvent() {}

  public SubmissionKafkaEvent(
      UUID submissionId,
      UUID userId,
      UUID recruitmentId,
      String status,
      LocalDateTime submissionTime,
      UUID messageId,
      LocalDateTime eventTimestamp
      ) {
    this.submissionId = submissionId;
    this.userId = userId;
    this.recruitmentId = recruitmentId;
    this.status = status;
    this.submissionTime = submissionTime;
    this.messageId = messageId;
    this.eventTimestamp = eventTimestamp;
  }
}
