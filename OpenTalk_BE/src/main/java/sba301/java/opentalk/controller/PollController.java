package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sba301.java.opentalk.dto.PollDTO;
import sba301.java.opentalk.service.PollService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/poll")
public class PollController {
    private final PollService pollService;

    @PostMapping("/{meetingId}")
    public ResponseEntity<PollDTO> createPollForMeeting(@PathVariable long meetingId) {
            PollDTO dto = pollService.createPoll(meetingId);
            return ResponseEntity.ok().body(dto);
    }
    @GetMapping("/{meetingId}")
    public ResponseEntity<PollDTO> getPollByMeetingId(@PathVariable long meetingId) {
        PollDTO dto = pollService.getPollByMeeting(meetingId);
        return ResponseEntity.ok().body(dto);
    }
}
