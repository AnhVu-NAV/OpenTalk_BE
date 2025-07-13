package sba301.java.opentalk.service;

import sba301.java.opentalk.dto.OpenTalkRegistrationDTO;

import java.util.List;

public interface OpenTalkRegistrationService {
    public void registerOpenTalk(OpenTalkRegistrationDTO registrationDTO);
    public List<OpenTalkRegistrationDTO> findByOpenTalkTopicId(Long topicId);
    public List<OpenTalkRegistrationDTO> findByOpenTalkTopicIdWithNativeQuery(Long topicId);
    public List<OpenTalkRegistrationDTO> findByOpenTalkTopicIdWithInterfaceProjection(Long topicId);
}
