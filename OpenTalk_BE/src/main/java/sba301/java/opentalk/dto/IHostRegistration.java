package sba301.java.opentalk.dto;

import sba301.java.opentalk.enums.HostRegistrationStatus;

import java.time.LocalDateTime;

public interface IHostRegistration {
    long getId();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    long getUserId();
    long getOpenTalkMeetingId();
    HostRegistrationStatus getStatus();
}
