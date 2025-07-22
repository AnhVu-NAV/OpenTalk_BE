package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sba301.java.opentalk.dto.FeedbackDTO;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.service.FeedbackService;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
@Slf4j
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping("/{meetingId}")
    public ResponseEntity<List<FeedbackDTO>> getAllFeedbacksByMeetingId(@PathVariable Long meetingId) {
        return ResponseEntity.ok(feedbackService.getFeedbacksOfMeeting(meetingId));
    }

    @PostMapping
    public ResponseEntity<Void> createFeedback(@RequestBody FeedbackDTO feedbackDTO) throws AppException {
        if (feedbackDTO.getUser() == null || feedbackDTO.getMeeting() == null) {
            return ResponseEntity.badRequest().build();
        }
        feedbackService.createFeedback(feedbackDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long feedbackId) {
        return feedbackService.deleteFeedback(feedbackId) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping()
    public ResponseEntity<FeedbackDTO> updateFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        return feedbackService.updateFeedback(feedbackDTO) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
