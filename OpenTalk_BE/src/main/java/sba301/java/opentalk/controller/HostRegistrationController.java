package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sba301.java.opentalk.dto.HostRegistrationDTO;
import sba301.java.opentalk.service.HostRegistrationService;

import java.util.List;

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
}
