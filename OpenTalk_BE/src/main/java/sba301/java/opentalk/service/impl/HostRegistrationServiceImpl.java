package sba301.java.opentalk.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import sba301.java.opentalk.dto.IHostRegistration;
import sba301.java.opentalk.dto.HostRegistrationDTO;
import sba301.java.opentalk.entity.HostRegistration;
import sba301.java.opentalk.entity.OpenTalkRegistration;
import sba301.java.opentalk.entity.OpenTalkMeeting;
import sba301.java.opentalk.entity.User;
import sba301.java.opentalk.enums.HostRegistrationStatus;
import sba301.java.opentalk.event.HostRegistrationEvent;
import sba301.java.opentalk.mapper.OpenTalkMeetingMapper;
import sba301.java.opentalk.mapper.UserMapper;
import sba301.java.opentalk.repository.HostRegistrationRepository;
import sba301.java.opentalk.repository.OpenTalkMeetingRepository;
import sba301.java.opentalk.repository.UserRepository;
import sba301.java.opentalk.service.HostRegistrationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HostRegistrationServiceImpl implements HostRegistrationService {
    private final HostRegistrationRepository hostRegistrationRepository;
    private final UserRepository userRepository;
    private final OpenTalkMeetingRepository topicRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void registerOpenTalk(HostRegistrationDTO registrationDTO) {
        User user = userRepository.findById(registrationDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        OpenTalkMeeting topic = topicRepository.findById(registrationDTO.getOpenTalkMeetingId())
                .orElseThrow(() -> new RuntimeException("Topic Not Found"));

        if (hostRegistrationRepository.existsByUserIdAndOpenTalkMeetingId(registrationDTO.getUserId(), registrationDTO.getOpenTalkMeetingId())) {
            throw new RuntimeException("User has registered this OpenTalk Topic");
        }

        HostRegistration registration = new HostRegistration();
        registration.setUser(user);
        registration.setOpenTalkMeeting(topic);
        registration.setStatus(registrationDTO.getStatus());

        hostRegistrationRepository.save(registration);

        eventPublisher.publishEvent(new HostRegistrationEvent(this, UserMapper.INSTANCE.userToUserDTO(user), OpenTalkMeetingMapper.INSTANCE.toDto(topic), registrationDTO.getStatus()));
    }

    @Override
    public List<HostRegistrationDTO> findByOpenTalkMeetingId(Long topicId) {
        ModelMapper modelMapper = new ModelMapper();
        List<HostRegistration> hostRegistrations = hostRegistrationRepository.findByOpenTalkMeetingId(topicId);
        return hostRegistrations.stream().map(hostRegistration -> modelMapper.map(hostRegistration, HostRegistrationDTO.class)).toList();
    }

    @Override
    public List<HostRegistrationDTO> findByOpenTalkMeetingIdWithNativeQuery(Long topicId) {
        List<HostRegistration> dtos = hostRegistrationRepository.findByOpenTalkMeetingIdWithNativeQuery(topicId);
        ModelMapper modelMapper = new ModelMapper();
        return dtos.stream().map(openTalkRegistration -> modelMapper.map(openTalkRegistration, HostRegistrationDTO.class)).toList();
    }

    @Override
    public List<HostRegistrationDTO> findByOpenTalkMeetingIdWithInterfaceProjection(Long topicId) {
        List<IHostRegistration> iHostRegistrations = hostRegistrationRepository.findRegistrationsByTopicId(topicId);
        return iHostRegistrations.stream()
                .map(registration -> {
                    HostRegistrationDTO dto = new HostRegistrationDTO();
                    dto.setId(registration.getId());
                    dto.setCreatedAt(registration.getCreatedAt());
                    dto.setUpdatedAt(registration.getUpdatedAt());
                    dto.setUserId(registration.getUserId());
                    dto.setOpenTalkMeetingId(registration.getOpenTalkMeetingId());
                    dto.setStatus(registration.getStatus());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
