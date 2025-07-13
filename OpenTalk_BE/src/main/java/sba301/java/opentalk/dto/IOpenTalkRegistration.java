package sba301.java.opentalk.dto;

import sba301.java.opentalk.enums.OpenTalkRegistrationStatus;

import java.time.LocalDateTime;

public interface IOpenTalkRegistration {
    long getId();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    long getUserId();
    long getOpenTalkTopicId();
    OpenTalkRegistrationStatus getStatus();
}
