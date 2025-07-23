package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sba301.java.opentalk.dto.HostRegistrationDTO;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.model.response.HostFrequencyResponse;
import sba301.java.opentalk.service.HostRegistrationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hosts")
@RequiredArgsConstructor
@Slf4j
public class HostRegistrationController {
    private final HostRegistrationService hostRegistrationService;

    @GetMapping("/register/{openTalkMeetingId}")
    public ResponseEntity<List<HostRegistrationDTO>> getRegisteredOpenTalks(@PathVariable Long openTalkMeetingId) {
        return ResponseEntity.ok().body(hostRegistrationService.findByOpenTalkMeetingId(openTalkMeetingId));
    }

    @GetMapping("/register/native-query/{openTalkMeetingId}")
    public ResponseEntity<List<HostRegistrationDTO>> getRegisteredOpenTalksWithNativeQuery(@PathVariable Long openTalkMeetingId) {
        return ResponseEntity.ok().body(hostRegistrationService.findByOpenTalkMeetingIdWithNativeQuery(openTalkMeetingId));
    }

    @GetMapping("/register/interface-projection/{openTalkMeetingId}")
    public ResponseEntity<List<HostRegistrationDTO>> getRegisteredOpenTalksWithInterfaceProjection(@PathVariable Long openTalkMeetingId) {
        return ResponseEntity.ok().body(hostRegistrationService.findByOpenTalkMeetingIdWithInterfaceProjection(openTalkMeetingId));
    }

    @GetMapping("/auto-select-host/{openTalkMeetingId}")
    public ResponseEntity<UserDTO> getRandomHost(@PathVariable Long openTalkMeetingId) {
        return ResponseEntity.ok().body(hostRegistrationService.findRandomHost(openTalkMeetingId));
    }

    @PostMapping("/request-counts")
    public ResponseEntity<Map<Long, Long>> getRequestCounts(@RequestBody List<Long> meetingIds) {
        Map<Long, Long> map = hostRegistrationService.getRequestCountForMeetings(meetingIds);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/approve/{registrationId}")
    public ResponseEntity<?> approveHostRegistration(@PathVariable Long registrationId) {
        hostRegistrationService.approveHostRegistration(registrationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject/{registrationId}")
    public ResponseEntity<?> rejectHostRegistration(@PathVariable Long registrationId) {
        hostRegistrationService.rejectHostRegistration(registrationId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/native-query/frequency")
    public ResponseEntity<List<HostFrequencyResponse>> getUserFrequency() {
        List<HostFrequencyResponse> list = hostRegistrationService.getUserHostFrequency();
        return ResponseEntity.ok().body(list);
    }
}
