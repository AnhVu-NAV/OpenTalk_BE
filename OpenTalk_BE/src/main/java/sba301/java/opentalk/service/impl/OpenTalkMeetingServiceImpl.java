package sba301.java.opentalk.service.impl;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import sba301.java.opentalk.common.RandomOpenTalkNumberGenerator;
import sba301.java.opentalk.common.StatusMeetingUpdateJob;
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
import sba301.java.opentalk.repository.CompanyBranchRepository;
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
import java.time.format.DateTimeFormatter;
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
    private final CompanyBranchRepository companyBranchRepository;
    private final JedisPool jedisPool;
    private final Scheduler scheduler;

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
    public List<OpenTalkMeetingDetailDTO> getOpenTalkMeetingWithDetailsForHost(String meetingName, Long branchId, String username) {
        return this.getOpenTalkMeetingWithDetails(meetingName, branchId).stream()
                .filter(meeting -> meeting.getHost() != null && meeting.getHost().getUsername().equals(username))
                .toList();
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

    @Override
    public void createEmptyOpenTalk() {
        LocalDateTime scheduledDate = LocalDateTime.now().plusDays(redisService.getDaysUntilOpenTalk());
        companyBranchRepository.findAll().forEach(companyBranch -> {
            OpenTalkMeeting openTalkMeeting = new OpenTalkMeeting();
            openTalkMeeting.setMeetingName("Opentalk Meeting at " + scheduledDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
            openTalkMeeting.setStatus(MeetingStatus.WAITING_TOPIC);
            openTalkMeeting.setScheduledDate(scheduledDate);
            openTalkMeeting.setCompanyBranch(companyBranch);
            openTalkMeeting.setDuration(2);
            openTalkMeetingRepository.save(openTalkMeeting);
            mailService.sendMailUpdateInfoMeetingForMeetingManager(OpenTalkMeetingMapper.INSTANCE.toDto(openTalkMeeting));
            this.scheduleMeetingStatusUpdate(openTalkMeeting);
        });
    }

    @Override
    public List<OpenTalkMeetingDTO> getAllMeetingsByStatus(MeetingStatus status) {
        return openTalkMeetingRepository.findAllByStatus(status).stream().map(OpenTalkMeetingMapper.INSTANCE::toDto).toList();
    }

    @Override
    public void scheduleMeetingStatusUpdate(OpenTalkMeeting openTalkMeeting) {
        LocalDateTime scheduledDate = openTalkMeeting.getScheduledDate();
        LocalDateTime endDateTime = scheduledDate.plusHours((long) openTalkMeeting.getDuration());

        String cronExpressionForUpcomingToOngoing = buildCronExpression(scheduledDate);
        scheduleStatusUpdateJob(openTalkMeeting, cronExpressionForUpcomingToOngoing, "UPCOMING_TO_ONGOING");

        String cronExpressionForOngoingToCompleted = buildCronExpression(endDateTime);
        scheduleStatusUpdateJob(openTalkMeeting, cronExpressionForOngoingToCompleted, "ONGOING_TO_COMPLETED");
    }

    @Override
    public void updateMeetingStatus(Long meetingId, String jobType) {
        OpenTalkMeeting meeting = openTalkMeetingRepository.findById(meetingId).orElseThrow(() -> new RuntimeException("Meeting not found"));

        if ("UPCOMING_TO_ONGOING".equals(jobType) && meeting.getStatus() == MeetingStatus.UPCOMING) {
            meeting.setStatus(MeetingStatus.ONGOING);
        } else if ("ONGOING_TO_COMPLETED".equals(jobType) && meeting.getStatus() == MeetingStatus.ONGOING) {
            meeting.setStatus(MeetingStatus.COMPLETED);
        }

        openTalkMeetingRepository.save(meeting);
    }

    private void scheduleStatusUpdateJob(OpenTalkMeeting openTalkMeeting, String cronExpression, String jobType) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set("meeting:" + openTalkMeeting.getId() + ":" + jobType, cronExpression);
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(openTalkMeeting.getId() + ":" + jobType);
        JobDetail jobDetail = JobBuilder.newJob(StatusMeetingUpdateJob.class)
                .withIdentity(openTalkMeeting.getId() + ":" + jobType)
                .usingJobData("jobType", jobType)
                .build();
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .forJob(jobDetail)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to schedule status update job for meeting " + openTalkMeeting.getId(), e);
        }
    }

    private String buildCronExpression(LocalDateTime endDateTime) {
        return String.format("0 %d %d %d %d ? %d",
                endDateTime.getMinute(),
                endDateTime.getHour(),
                endDateTime.getDayOfMonth(),
                endDateTime.getMonthValue(),
                endDateTime.getYear());
    }

    @Override
    public OpenTalkMeetingDTO updateStatusAfterCreatePoll(Long id) {
        OpenTalkMeeting meeting = openTalkMeetingRepository.findById(id).get();
        meeting.setStatus(MeetingStatus.WAITING_HOST_REGISTER);
        openTalkMeetingRepository.save(meeting);
        return OpenTalkMeetingMapper.INSTANCE.toDto(meeting);
    }

    @Override
    public List<OpenTalkMeetingDTO> findAllMeeting(String name) {
        return openTalkMeetingRepository.findByMeetingName(name).stream().map(OpenTalkMeetingMapper.INSTANCE::toDto).toList();
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
                        meeting.getTopic().getSuggestedBy() != null ? meeting.getTopic().getSuggestedBy().getAvatarUrl() : null,
                        meeting.getTopic().getSuggestedBy() != null ? meeting.getTopic().getSuggestedBy().getUsername() : null,
                        meeting.getTopic().getSuggestedBy() != null ? meeting.getTopic().getSuggestedBy().getEmail() : null,
                        meeting.getTopic().getSuggestedBy() != null ? meeting.getTopic().getSuggestedBy().getFullName() : null,
                        new OpenTalkMeetingDetailDTO.CompanyBranchDTO(
                                meeting.getTopic().getSuggestedBy() != null ? meeting.getTopic().getSuggestedBy().getCompanyBranch().getName() : null
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
