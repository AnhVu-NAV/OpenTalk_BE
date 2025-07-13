package sba301.java.opentalk.service;

import sba301.java.opentalk.dto.HostRegistrationDTO;

import java.util.List;

public interface HostRegistrationService {
    void registerOpenTalk(HostRegistrationDTO registrationDTO);
    List<HostRegistrationDTO> findByOpenTalkMeetingId(Long topicId);
    List<HostRegistrationDTO> findByOpenTalkMeetingIdWithNativeQuery(Long topicId);
    List<HostRegistrationDTO> findByOpenTalkMeetingIdWithInterfaceProjection(Long topicId);
}
