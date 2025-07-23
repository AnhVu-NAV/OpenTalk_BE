package sba301.java.opentalk.service;

import sba301.java.opentalk.dto.HostRegistrationDTO;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.model.response.HostFrequencyResponse;

import java.util.List;

public interface HostRegistrationService {
    void registerOpenTalk(HostRegistrationDTO registrationDTO);

    List<HostRegistrationDTO> findByOpenTalkMeetingId(Long meetingId);

    List<HostRegistrationDTO> findByOpenTalkMeetingIdWithNativeQuery(Long meetingId);

    List<HostRegistrationDTO> findByOpenTalkMeetingIdWithInterfaceProjection(Long meetingId);

    UserDTO findRandomHost(Long meetingId);

    List<HostFrequencyResponse> getUserHostFrequency();

    void updateHostSelection();
}
