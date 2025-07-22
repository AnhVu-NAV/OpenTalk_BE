package sba301.java.opentalk.service.impl;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sba301.java.opentalk.common.RandomOpenTalkNumberGenerator;
import sba301.java.opentalk.dto.BaseDTO;
import sba301.java.opentalk.dto.HostRegistrationDTO;
import sba301.java.opentalk.dto.OpenTalkMeetingDTO;
import sba301.java.opentalk.dto.OpenTalkMeetingDetailDTO;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.entity.OpenTalkMeeting;
import sba301.java.opentalk.entity.User;
import sba301.java.opentalk.enums.HostRegistrationStatus;
import sba301.java.opentalk.enums.MailType;
import sba301.java.opentalk.enums.MeetingStatus;
import sba301.java.opentalk.mapper.OpenTalkMeetingMapper;
import sba301.java.opentalk.model.Mail.Mail;
import sba301.java.opentalk.model.Mail.MailSubjectFactory;
import sba301.java.opentalk.model.request.OpenTalkCompletedRequest;
import sba301.java.opentalk.model.response.OpenTalkMeetingWithStatusDTO;
import sba301.java.opentalk.repository.AttendanceRepository;
import sba301.java.opentalk.repository.FeedbackRepository;
import sba301.java.opentalk.repository.HostRegistrationRepository;
import sba301.java.opentalk.repository.OpenTalkMeetingRepository;
import sba301.java.opentalk.service.HostRegistrationService;
import sba301.java.opentalk.service.MailService;
import sba301.java.opentalk.service.OpenTalkMeetingService;
import sba301.java.opentalk.service.RedisService;
import sba301.java.opentalk.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    private final AttendanceRepository attendanceRepository;
    private final FeedbackRepository feedbackRepository;

    @Override
    public OpenTalkMeetingDTO createMeeting(OpenTalkMeetingDTO topic) {
        meetingRepository.save(OpenTalkMeetingMapper.INSTANCE.toEntity(topic));
        return topic;
    }

    @Override
    public OpenTalkMeetingDTO updateMeeting(OpenTalkMeetingDTO topic, Long topicId) {
        if (meetingRepository.existsById(topicId)) {
            topic.setId(topicId);
            meetingRepository.save(OpenTalkMeetingMapper.INSTANCE.toEntity(topic));
            return topic;
        }
        return null;
    }

    @Override
    public Page<OpenTalkMeetingDTO> getAllMeetings(String name,
                                                   Long companyBranchId,
                                                   MeetingStatus status,
                                                   String dateStr,
                                                   String fromDateStr,
                                                   String toDateStr,
                                                   int page,
                                                   int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Parse ngày lọc đúng định dạng
        LocalDate date = (dateStr != null && !dateStr.isEmpty())
                ? LocalDate.parse(dateStr) : null;

        // Khoảng từ ngày (00:00:00) đến ngày (23:59:59.999)
        LocalDateTime fromDateTime = (fromDateStr != null && !fromDateStr.isEmpty())
                ? LocalDate.parse(fromDateStr).atStartOfDay()
                : null;
        LocalDateTime toDateTime = (toDateStr != null && !toDateStr.isEmpty())
                ? LocalDate.parse(toDateStr).atTime(LocalTime.MAX)
                : null;

        Page<OpenTalkMeeting> meetingPage = meetingRepository.findWithFilter(
                name, companyBranchId, status, date, fromDateTime, toDateTime, pageable
        );
        return meetingPage.map(OpenTalkMeetingMapper.INSTANCE::toDto);
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

        List<Long> meetingIdsHasHost = meetingDetails.stream().filter(
                meetingDetail -> !Objects.equals(meetingDetail.getStatus(), String.valueOf(MeetingStatus.WAITING_HOST_REGISTER))
        ).map(BaseDTO::getId).toList();

        List<Long> meetingIdsNoHost = meetingDetails.stream().filter(
                meetingDetail -> Objects.equals(meetingDetail.getStatus(), String.valueOf(MeetingStatus.WAITING_HOST_REGISTER))
        ).map(BaseDTO::getId).toList();

        Map<Long, List<Long>> mapMeetingWithPendingHosts = hostRegistrationRepository
                .findUserIdAndOpenTalkMeetingIdsByOpenTalkMeetingId(meetingIdsNoHost)
                .stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get("openTalkMeetingId", Long.class),
                        Collectors.mapping(tuple -> tuple.get("userId", Long.class), Collectors.toList())
                ));

        meetingDetails.forEach(meetingDetail -> {
            meetingDetail.setRegisteredHostUserIds(mapMeetingWithPendingHosts.get(meetingDetail.getId()));
        });

        List<Tuple> hostData = hostRegistrationRepository.findHostByOpenTalkMeetingIds(meetingIdsHasHost);
        meetingDetails.forEach(meetingDetail -> {
            OpenTalkMeetingDetailDTO.UserDTO userHost = hostData
                    .stream().filter(t -> Objects.equals(t.get("meetingId", Long.class), meetingDetail.getId()))
                    .findFirst()
                    .map(t -> t.get("host", User.class))
                    .map(u -> new OpenTalkMeetingDetailDTO.UserDTO(
                            u.getAvatarUrl(),
                            u.getUsername(),
                            u.getEmail(),
                            u.getFullName(),
                            new OpenTalkMeetingDetailDTO.CompanyBranchDTO(u.getCompanyBranch().getName())
                    )).orElse(null);
            meetingDetail.setHost(userHost);
            if (Objects.equals(meetingDetail.getStatus(), String.valueOf(MeetingStatus.COMPLETED))) {
                Double avg = feedbackRepository.calculateAverageRateByMeetingId(meetingDetail.getId());
                meetingDetail.setAvgRating(avg != null ? (int) Math.round(avg) : null);
            }
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
        return openTalkMeetingRepository.findById(meetingId).map(OpenTalkMeetingMapper.INSTANCE::toDto).orElse(null);
    }

    @Override
    public OpenTalkMeetingDTO findMeetingByTopicId(long topicId) {
        return openTalkMeetingRepository.findByTopicId(topicId).map(OpenTalkMeetingMapper.INSTANCE::toDto).orElse(null);
    }

    @Override
    public List<OpenTalkMeetingDTO> getMeetingsByCheckinCodesInRedis() {
        List<String> keys = redisService.getKeysByPattern("checkin_code:*");

        if (keys.isEmpty()) return Collections.emptyList();

        List<String> meetingIdStrList = keys.stream()
                .map(redisService::get)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (meetingIdStrList.isEmpty()) return Collections.emptyList();

        List<Long> meetingIds = meetingIdStrList.stream()
                .map(Long::parseLong)
                .toList();

        List<OpenTalkMeeting> meetings = openTalkMeetingRepository.findAllById(meetingIds);

        return meetings.stream()
                .map(OpenTalkMeetingMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OpenTalkMeetingWithStatusDTO> getRecentMeetingsWithStatusAttendance(Long userId, Long companyBranchId) {
        LocalDateTime startOfMonth = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = YearMonth.now().atEndOfMonth().atTime(LocalTime.MAX);

        return openTalkMeetingRepository.findByIdAndScheduledDateBetween(companyBranchId, startOfMonth, endOfMonth)
                .stream()
                .map(meeting -> {
                    boolean attended = attendanceRepository.existsAttendanceByUserIdAndOpenTalkMeetingId(userId, meeting.getId());
                    return new OpenTalkMeetingWithStatusDTO(meeting, attended);
                })
                .collect(Collectors.toList());
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
                        meeting.getTopic().getSuggestedBy().getAvatarUrl(),
                        meeting.getTopic().getSuggestedBy().getUsername(),
                        meeting.getTopic().getSuggestedBy().getEmail(),
                        meeting.getTopic().getSuggestedBy().getFullName(),
                        new OpenTalkMeetingDetailDTO.CompanyBranchDTO(
                                meeting.getTopic().getSuggestedBy().getCompanyBranch().getName()
                        )
                ),
                new OpenTalkMeetingDetailDTO.UserDTO(
                        meeting.getTopic().getEvalutedBy() != null ? meeting.getTopic().getEvalutedBy().getAvatarUrl() : null,
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
