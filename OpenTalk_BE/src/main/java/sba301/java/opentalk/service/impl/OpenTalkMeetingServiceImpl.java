package sba301.java.opentalk.service.impl;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sba301.java.opentalk.common.RandomOpenTalkNumberGenerator;
import sba301.java.opentalk.dto.*;
import sba301.java.opentalk.entity.OpenTalkMeeting;
import sba301.java.opentalk.entity.User;
import sba301.java.opentalk.enums.HostRegistrationStatus;
import sba301.java.opentalk.enums.MailType;
import sba301.java.opentalk.enums.MeetingStatus;
import sba301.java.opentalk.mapper.OpenTalkMeetingMapper;
import sba301.java.opentalk.model.Mail.Mail;
import sba301.java.opentalk.model.Mail.MailSubjectFactory;
import sba301.java.opentalk.model.request.OpenTalkCompletedRequest;
import sba301.java.opentalk.repository.HostRegistrationRepository;
import sba301.java.opentalk.repository.OpenTalkMeetingRepository;
import sba301.java.opentalk.service.HostRegistrationService;
import sba301.java.opentalk.service.MailService;
import sba301.java.opentalk.service.OpenTalkMeetingService;
import sba301.java.opentalk.service.RedisService;
import sba301.java.opentalk.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OpenTalkMeetingServiceImpl implements OpenTalkMeetingService {
    private final OpenTalkMeetingRepository meetingRepository;
    private final OpenTalkMeetingRepository openTalkMeetingRepository;
    private final RedisService redisService;
    private final UserService userService;
    private final HostRegistrationService hostRegistrationService;
    private final RandomOpenTalkNumberGenerator randomOpenTalkNumberGenerator;
    private final MailService mailService;
    private final HostRegistrationRepository hostRegistrationRepository;

    @Override
    public OpenTalkMeetingDTO createMeeting(OpenTalkMeetingDTO topic) {
        meetingRepository.save(OpenTalkMeetingMapper.INSTANCE.toEntity(topic));
        return topic;
    }

    @Override
    public OpenTalkMeetingDTO updateMeeting(OpenTalkMeetingDTO topic) {
        if (meetingRepository.existsById(topic.getId())) {
            meetingRepository.save(OpenTalkMeetingMapper.INSTANCE.toEntity(topic));
            return topic;
        }
        return null;
    }

    @Override
    public List<OpenTalkMeetingDTO> getAllMeetings() {
        return meetingRepository.findAll().stream().map(OpenTalkMeetingMapper.INSTANCE::toDto).toList();
    }

    @Override
    public List<OpenTalkMeetingDetailDTO> getOpenTalkMeetingWithDetails(String meetingName, Long branchId) {
        List<OpenTalkMeetingDetailDTO> meetingDetails = openTalkMeetingRepository
                .findByStatusInWithMeetingNameAndBranchId(Arrays.asList(
                        MeetingStatus.WAITING_HOST_REGISTER,
                        MeetingStatus.UPCOMING,
                        MeetingStatus.ONGOING,
                        MeetingStatus.COMPLETED), meetingName, branchId)
                .stream().map(this::convertToDetailDTO).collect(Collectors.toList());
        List<Long> meetingIds = meetingDetails.stream().filter(
                meetingDetail -> !Objects.equals(meetingDetail.getStatus(), String.valueOf(MeetingStatus.WAITING_HOST_REGISTER))
        ).map(BaseDTO::getId).toList();
        List<Tuple> hostData = hostRegistrationRepository.findHostByOpenTalkMeetingIds(meetingIds);
        meetingDetails.forEach(meetingDetail -> {
            OpenTalkMeetingDetailDTO.UserDTO userHost = hostData
                    .stream().filter(t -> Objects.equals(t.get("meetingId", Long.class), meetingDetail.getId()))
                    .findFirst()
                    .map(t -> t.get("host", User.class))
                    .map(u -> new OpenTalkMeetingDetailDTO.UserDTO(
                            u.getUsername(),
                            u.getEmail(),
                            u.getFullName(),
                            new OpenTalkMeetingDetailDTO.CompanyBranchDTO(u.getCompanyBranch().getName())
                    )).orElse(null);
            meetingDetail.setHost(userHost);
        });
        return meetingDetails;
    }

    @Override
    public Page<OpenTalkMeetingDTO> getMeetingsCompleted(OpenTalkCompletedRequest request) {
        return meetingRepository.findCompletedOpenTalks(
                        request.getCompanyBranchId(), request.getHostName(),
                        request.getIsOrganized(), request.getIsEnableOfHost(),
                        request.getStartDate(), request.getEndDate(),
                        PageRequest.of(request.getPage(), request.getSize()))
                .map(OpenTalkMeetingMapper.INSTANCE::toDto);
    }

    @Override
    public Page<OpenTalkMeetingDTO> getRegisteredOpenTalks(Long userId, LocalDate startDate, LocalDate endDate, int page, int pageSize) {
        return openTalkMeetingRepository
                .findHostRegisteredOpenTalksByUser(
                        userId,
                        startDate,
                        endDate,
                        PageRequest.of(page, pageSize, Sort.by("scheduledDate").descending()))
                .map(OpenTalkMeetingMapper.INSTANCE::toDto);
    }

    @Override
    public Optional<OpenTalkMeetingDTO> getMeetingById(Long topicId) {
        return meetingRepository.findById(topicId).map(OpenTalkMeetingMapper.INSTANCE::toDto);
    }

    @Override
    public boolean deleteMeeting(Long topicId) {
        if (meetingRepository.existsById(topicId)) {
            meetingRepository.deleteById(topicId);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkExistOpenTalk(LocalDateTime dateTime) {
        return meetingRepository.findByScheduledDate(dateTime).isPresent();
    }

    @Override
    public void createScheduledOpenTalk() {
        List<UserDTO> availableUsers = userService.getAvailableUsersTobeHost();

        if (availableUsers.isEmpty()) {
            return;
        }

        UserDTO randomUser = availableUsers.get((int) (randomOpenTalkNumberGenerator.generateOpenTalkNumber() * availableUsers.size()));

        LocalDateTime scheduledDate = LocalDateTime.now().plusDays(redisService.getDaysUntilOpenTalk());
        if (checkExistOpenTalk(scheduledDate)) {
            return;
        }

        OpenTalkMeetingDTO newTopic = new OpenTalkMeetingDTO();
        newTopic.setMeetingName("");
        newTopic.setStatus(MeetingStatus.WAITING_TOPIC);
        newTopic.setScheduledDate(scheduledDate);
        newTopic.setCompanyBranch(randomUser.getCompanyBranch());
        createMeeting(newTopic);

        HostRegistrationDTO hostRegistrationDTO = new HostRegistrationDTO();
        hostRegistrationDTO.setUser(randomUser);
        hostRegistrationDTO.setMeeting(OpenTalkMeetingMapper.INSTANCE.toDto(meetingRepository.findByScheduledDate(scheduledDate).get()));
        hostRegistrationDTO.setStatus(HostRegistrationStatus.APPROVED);
        hostRegistrationService.registerOpenTalk(hostRegistrationDTO);
    }

    @Override
    @Async
    public void sendMailRemind(Long openTalkId) {
        List<UserDTO> allUsers = userService.getUsers();
        Mail mail = new Mail();
        mail.setMailTo(allUsers.stream().map(UserDTO::getEmail).toArray(String[]::new));
        mail.setMailSubject(MailSubjectFactory.getMailSubject(MailType.REMIND).toString());
        mail.setMailContent("Remind invite the Open Talk Topic " + getMeetingById(openTalkId).get().getMeetingName());
        mailService.sendMail(mail);
    }

    @Override
    public OpenTalkMeetingDTO findMeetingById(long meetingId) {
        return openTalkMeetingRepository.findByTopicId(meetingId).map(OpenTalkMeetingMapper.INSTANCE::toDto).orElse(null);
    }

    @Override
    public OpenTalkMeetingDTO findMeetingByTopicId(long topicId) {
        return openTalkMeetingRepository.findByTopicId(topicId).map(OpenTalkMeetingMapper.INSTANCE::toDto).orElse(null);
    }

    private OpenTalkMeetingDetailDTO convertToDetailDTO(OpenTalkMeeting meeting) {
        OpenTalkMeetingDetailDTO detail = new OpenTalkMeetingDetailDTO();
        detail.setId(meeting.getId());
        detail.setMeetingName(meeting.getMeetingName());
        detail.setMeetingLink(meeting.getMeetingLink());
        detail.setScheduledDate(meeting.getScheduledDate());
        detail.setStatus(String.valueOf(meeting.getStatus()));
        detail.setCompanyBranch(new OpenTalkMeetingDetailDTO.CompanyBranchDTO(meeting.getCompanyBranch().getName()));
        detail.setTopic(meeting.getTopic() == null ? null : new OpenTalkMeetingDetailDTO.TopicDTO(
                meeting.getTopic().getTitle(),
                meeting.getTopic().getDescription(),
                meeting.getTopic().getRemark(),
                new OpenTalkMeetingDetailDTO.UserDTO(
                        meeting.getTopic().getSuggestedBy().getUsername(),
                        meeting.getTopic().getSuggestedBy().getEmail(),
                        meeting.getTopic().getSuggestedBy().getFullName(),
                        new OpenTalkMeetingDetailDTO.CompanyBranchDTO(
                                meeting.getTopic().getSuggestedBy().getCompanyBranch().getName()
                        )
                ),
                new OpenTalkMeetingDetailDTO.UserDTO(
                        meeting.getTopic().getEvalutedBy() != null ? meeting.getTopic().getEvalutedBy().getUsername() : null,
                        meeting.getTopic().getEvalutedBy() != null ? meeting.getTopic().getEvalutedBy().getEmail() : null,
                        meeting.getTopic().getEvalutedBy() != null ? meeting.getTopic().getEvalutedBy().getFullName() : null,
                        new OpenTalkMeetingDetailDTO.CompanyBranchDTO(
                                meeting.getTopic().getEvalutedBy() != null ? meeting.getTopic().getEvalutedBy().getCompanyBranch().getName() : null
                        )
                )
        ));
        return detail;
    }
}
