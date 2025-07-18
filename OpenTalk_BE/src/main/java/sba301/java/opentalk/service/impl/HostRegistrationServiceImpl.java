package sba301.java.opentalk.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import sba301.java.opentalk.dto.HostRegistrationDTO;
import sba301.java.opentalk.dto.IHostRegistration;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.entity.HostRegistration;
import sba301.java.opentalk.entity.OpenTalkMeeting;
import sba301.java.opentalk.entity.User;
import sba301.java.opentalk.enums.HostRegistrationStatus;
import sba301.java.opentalk.event.HostRegistrationEvent;
import sba301.java.opentalk.mapper.OpenTalkMeetingMapper;
import sba301.java.opentalk.mapper.UserMapper;
import sba301.java.opentalk.model.UserHostCount;
import sba301.java.opentalk.repository.HostRegistrationRepository;
import sba301.java.opentalk.repository.OpenTalkMeetingRepository;
import sba301.java.opentalk.repository.UserRepository;
import sba301.java.opentalk.service.HostRegistrationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HostRegistrationServiceImpl implements HostRegistrationService {
    private final HostRegistrationRepository hostRegistrationRepository;
    private final UserRepository userRepository;
    private final OpenTalkMeetingRepository meetingRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void registerOpenTalk(HostRegistrationDTO registrationDTO) {
        User user = userRepository.findById(registrationDTO.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        OpenTalkMeeting topic = meetingRepository.findById(registrationDTO.getMeeting().getId())
                .orElseThrow(() -> new RuntimeException("Topic Not Found"));

        if (hostRegistrationRepository.existsByUserIdAndOpenTalkMeetingId(registrationDTO.getUser().getId(), registrationDTO.getMeeting().getId())) {
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
                    dto.setUser(UserMapper.INSTANCE.userToUserDTO(userRepository.findById(registration.getUserId()).get()));
                    dto.setMeeting(OpenTalkMeetingMapper.INSTANCE.toDto(meetingRepository.findById(registration.getOpenTalkMeetingId()).get()));
                    dto.setStatus(registration.getStatus());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findRandomHost(Long meetingId) {
        OpenTalkMeeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found"));

        LocalDateTime startOfYear = LocalDateTime.of(LocalDate.now().getYear(), 1, 1, 0, 0);
        LocalDateTime now = LocalDateTime.now();
        List<UserHostCount> results = userRepository.findAllUsersWithApprovedHostCount(
                startOfYear, now, meeting.getCompanyBranch().getId());

        if (results.isEmpty()) {
            throw new IllegalStateException("No available users found");
        }

        Long minCount = results.get(0).getCount();
        List<User> leastHostedUsers = results.stream()
                .filter(r -> r.getCount().equals(minCount))
                .map(UserHostCount::getUser)
                .toList();
        User selected = leastHostedUsers.get(new Random().nextInt(leastHostedUsers.size()));

        HostRegistration registration = new HostRegistration();
        registration.setUser(selected);
        registration.setOpenTalkMeeting(meeting);
        registration.setStatus(HostRegistrationStatus.APPROVED);
        hostRegistrationRepository.save(registration);

        return UserMapper.INSTANCE.userToUserDTO(selected);
    }
}
