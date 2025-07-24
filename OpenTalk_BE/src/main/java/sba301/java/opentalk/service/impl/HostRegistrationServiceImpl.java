package sba301.java.opentalk.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import sba301.java.opentalk.dto.HostRegistrationDTO;
import sba301.java.opentalk.dto.IHostRegistration;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.dto.UserHostFrequency;
import sba301.java.opentalk.entity.HostRegistration;
import sba301.java.opentalk.entity.OpenTalkMeeting;
import sba301.java.opentalk.entity.User;
import sba301.java.opentalk.enums.HostRegistrationStatus;
import sba301.java.opentalk.enums.MeetingStatus;
import sba301.java.opentalk.event.HostRegistrationEvent;
import sba301.java.opentalk.mapper.HostRegistrationMapper;
import sba301.java.opentalk.mapper.OpenTalkMeetingMapper;
import sba301.java.opentalk.mapper.UserMapper;
import sba301.java.opentalk.model.Mail.Mail;
import sba301.java.opentalk.model.UserHostCount;
import sba301.java.opentalk.model.response.HostFrequencyResponse;
import sba301.java.opentalk.repository.HostRegistrationRepository;
import sba301.java.opentalk.repository.OpenTalkMeetingRepository;
import sba301.java.opentalk.repository.UserRepository;
import sba301.java.opentalk.service.HostRegistrationService;
import sba301.java.opentalk.service.MailService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final MailService mailService;


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
        List<HostRegistration> hostRegistrations = hostRegistrationRepository.findByOpenTalkMeetingIdAndStatus(topicId, HostRegistrationStatus.PENDING);
        return hostRegistrations.stream().map(HostRegistrationMapper.INSTANCE::toDto).toList();
    }

    @Override
    public List<HostRegistrationDTO> findByOpenTalkMeetingIdWithNativeQuery(Long topicId) {
        List<HostRegistration> dtos = hostRegistrationRepository.findByOpenTalkMeetingIdWithNativeQuery(topicId);
        return dtos.stream().map(HostRegistrationMapper.INSTANCE::toDto).toList();
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

    @Override
    public List<HostFrequencyResponse> getUserHostFrequency() {
        List<UserHostFrequency> userHostFrequencyList = hostRegistrationRepository.getUserHostFrequency();
        return userHostFrequencyList.stream().map(userHostFrequency -> {
            HostFrequencyResponse hostFrequencyResponse = new HostFrequencyResponse(
                    userHostFrequency.getUserId(),
                    userHostFrequency.getFullName(),
                    userHostFrequency.getBranchName(),
                    userHostFrequency.getApprovedCount(),
                    userHostFrequency.getLastApprovedAt());
            return hostFrequencyResponse;
        }).toList();
    }

    @Override
    public void updateHostSelection() {

    }

    @Override
    public Map<Long, Long> getRequestCountForMeetings(List<Long> meetingIds) {
        Map<Long, Long> map = new HashMap<>();
        if (meetingIds == null || meetingIds.isEmpty()) return map;

        List<Object[]> results = hostRegistrationRepository.countRequestsByMeetingIds(meetingIds, HostRegistrationStatus.PENDING);
        for (Object[] row : results) {
            Long meetingId = (Long) row[0];
            Long count = (Long) row[1];
            map.put(meetingId, count);
        }
        return map;
    }

    @Override
    public void approveHostRegistration(Long registrationId) {
        HostRegistration approvedReg = hostRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Not found"));

        List<HostRegistration> allRegs = hostRegistrationRepository
                .findByOpenTalkMeetingIdAndStatus(approvedReg.getOpenTalkMeeting().getId(), approvedReg.getStatus());

        for (HostRegistration reg : allRegs) {
            if (reg.getId() == registrationId) {
                reg.setStatus(HostRegistrationStatus.APPROVED);
            } else {
                reg.setStatus(HostRegistrationStatus.REJECTED);
            }
        }
        hostRegistrationRepository.saveAll(allRegs);

        OpenTalkMeeting meeting = approvedReg.getOpenTalkMeeting();
        meeting.setHost(approvedReg.getUser());
        meeting.setStatus(MeetingStatus.WAITING_HOST_SELECTION);
        meetingRepository.save(meeting);

        String userEmail = approvedReg.getUser().getEmail();
        if (userEmail == null || userEmail.isBlank()) {
            throw new IllegalArgumentException("User email is missing!");
        }

        Mail mail = new Mail();
        mail.setMailTo(new String[]{userEmail});
        mail.setMailSubject("OpenTalk: Host Registration Approved");
        mail.setMailContent("Congratulations! You have been approved as the host for meeting: " + meeting.getMeetingName());
        mailService.sendMail(mail);
    }

    @Override
    public void rejectHostRegistration(Long registrationId) {
        HostRegistration reg = hostRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Not found"));
        reg.setStatus(HostRegistrationStatus.REJECTED);
        hostRegistrationRepository.save(reg);
    }
}
