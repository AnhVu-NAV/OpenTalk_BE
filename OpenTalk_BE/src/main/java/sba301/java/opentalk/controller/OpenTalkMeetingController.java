package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sba301.java.opentalk.dto.HostRegistrationDTO;
import sba301.java.opentalk.dto.OpenTalkMeetingDTO;
import sba301.java.opentalk.dto.OpenTalkMeetingDetailDTO;
import sba301.java.opentalk.enums.HostRegistrationStatus;
import sba301.java.opentalk.enums.MeetingStatus;
import sba301.java.opentalk.model.request.OpenTalkCompletedRequest;
import sba301.java.opentalk.model.response.OpenTalkMeetingWithStatusDTO;
import sba301.java.opentalk.service.HostRegistrationService;
import sba301.java.opentalk.service.OpenTalkMeetingService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/opentalk-meeting")
public class OpenTalkMeetingController {
    private final OpenTalkMeetingService openTalkMeetingService;
    private final HostRegistrationService hostRegistrationService;

    @GetMapping
    public ResponseEntity<Page<OpenTalkMeetingDTO>> getMeetings(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long companyBranchId,
            @RequestParam(required = false) MeetingStatus status,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<OpenTalkMeetingDTO> dtos = openTalkMeetingService.getAllMeetings(
                name, companyBranchId, status, date, fromDate, toDate, page, size
        );
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/details")
    public ResponseEntity<List<OpenTalkMeetingDetailDTO>> getMeetingDetails(
            @RequestParam(required = false, defaultValue = "") String meetingName,
            @RequestParam(required = false) Long branchId
    ) {
        List<OpenTalkMeetingDetailDTO> dtos = openTalkMeetingService.getOpenTalkMeetingWithDetails(meetingName,
                branchId);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/details-for-host")
    public ResponseEntity<List<OpenTalkMeetingDetailDTO>> getMeetingDetailsForHost(
            @RequestParam(required = false, defaultValue = "") String meetingName,
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false, defaultValue = "") String username
    ) {
        return ResponseEntity.ok(
                openTalkMeetingService.getOpenTalkMeetingWithDetailsForHost(meetingName, branchId, username));
    }

    @GetMapping("/{userId}/registered")
    public ResponseEntity<Page<OpenTalkMeetingDTO>> getRegisteredOpenTalks(
            @PathVariable Long userId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<OpenTalkMeetingDTO> dtos = openTalkMeetingService.getRegisteredOpenTalks(userId, startDate, endDate, page, size);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/completed")
    public ResponseEntity<Page<OpenTalkMeetingDTO>> getCompletedMeetings(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Long companyBranchId,
            @RequestParam(required = false) String hostName,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) boolean enableOfHost,
            @RequestParam(required = false) boolean isOrganized) {

        OpenTalkCompletedRequest request = new OpenTalkCompletedRequest(
                page, size, companyBranchId, hostName, isOrganized, enableOfHost, startDate, endDate
        );
        Page<OpenTalkMeetingDTO> dtos = openTalkMeetingService.getMeetingsCompleted(request);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<OpenTalkMeetingDTO> createOpenTalkMeeting(@RequestBody OpenTalkMeetingDTO dto) {
        OpenTalkMeetingDTO openTalkMeetingDTO = openTalkMeetingService.createMeeting(dto);
        return ResponseEntity.ok(openTalkMeetingDTO);
    }

    @PostMapping("/completedOpenTalk/create")
    public ResponseEntity<String> createCompletedOpenTalkMeeting() {
        openTalkMeetingService.createScheduledOpenTalk();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/employee/register")
    public ResponseEntity<OpenTalkMeetingDTO> registerOpenTalkMeetingByEmployee(@RequestBody HostRegistrationDTO hostRegistrationDTO) {
        hostRegistrationDTO.setStatus(HostRegistrationStatus.PENDING);
        hostRegistrationService.registerOpenTalk(hostRegistrationDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/{openTalkMeetingId}/register")
    public ResponseEntity<OpenTalkMeetingDTO> registerOpenTalkMeetingByAdmin(@RequestBody HostRegistrationDTO registrationDTO) {
        registrationDTO.setStatus(HostRegistrationStatus.APPROVED);
//        Locale locale = LocaleContextHolder.getLocale();
        hostRegistrationService.registerOpenTalk(registrationDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{openTalkMeetingId}")
    public ResponseEntity<OpenTalkMeetingDTO> updateOpenTalkMeeting(@RequestBody OpenTalkMeetingDTO dto, @PathVariable Long openTalkMeetingId) {
        OpenTalkMeetingDTO openTalkMeetingDTO = openTalkMeetingService.updateMeeting(dto, openTalkMeetingId);
        return ResponseEntity.ok(openTalkMeetingDTO);
    }

    @DeleteMapping("/{openTalkMeetingId}")
    public ResponseEntity<Void> deleteOpenTalkMeeting(@PathVariable Long openTalkMeetingId) {
        return openTalkMeetingService.deleteMeeting(openTalkMeetingId) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/mail/remind/{openTalkMeetingId}")
    public ResponseEntity<String> sendRemindEmail(@PathVariable Long openTalkMeetingId) {
        openTalkMeetingService.sendMailRemind(openTalkMeetingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{openTalkMeetingId}")
    public OpenTalkMeetingDTO getMeetingByID(@PathVariable Long openTalkMeetingId) {
        return openTalkMeetingService.findMeetingById(openTalkMeetingId);
    }

    @GetMapping("/meeting/{topicId}")
    public ResponseEntity<OpenTalkMeetingDTO> getMeetingByTopic(@PathVariable Long topicId) {
        OpenTalkMeetingDTO openTalkMeetingDTO = openTalkMeetingService.findMeetingByTopicId(topicId);
        return ResponseEntity.ok(openTalkMeetingDTO);
    }

    @GetMapping("/meeting-available-to-checkin")
    public ResponseEntity<List<OpenTalkMeetingDTO>> getMeetingAvailableToCheckin() {
        return ResponseEntity.ok(openTalkMeetingService.getMeetingsByCheckinCodesInRedis());
    }

    @GetMapping("/recent-with-status")
    public ResponseEntity<List<OpenTalkMeetingWithStatusDTO>> getRecentMeetingsWithStatus(
            @RequestParam Long userId,
            @RequestParam Long companyBranchId
    ) {
        return ResponseEntity.ok(openTalkMeetingService.getRecentMeetingsWithStatusAttendance(userId, companyBranchId));
    }
}
