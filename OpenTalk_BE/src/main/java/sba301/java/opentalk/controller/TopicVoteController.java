package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sba301.java.opentalk.dto.TopicVoteDTO;

import sba301.java.opentalk.model.request.UserVoteAbilityRequest;
import sba301.java.opentalk.model.response.TopicVoteResultResponse;
import sba301.java.opentalk.service.PollService;
import sba301.java.opentalk.service.TopicVoteService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/topic-vote")
public class TopicVoteController {
    private final TopicVoteService topicVoteService;
    private final PollService pollService;

    @PostMapping("/")
    public ResponseEntity<TopicVoteDTO> addTopicVote(@RequestBody TopicVoteDTO topicVoteDTO) {
        TopicVoteDTO response = topicVoteService.saveTopicVote(topicVoteDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check/")
    public boolean checkVoteAbility(@RequestParam long userId, @RequestParam long pollId) {
        return pollService.checkVoteAbility(pollId, userId);
    }

    @GetMapping("/result/{pollId}")
    public ResponseEntity<List<TopicVoteResultResponse>> getPollResult(@PathVariable long pollId) {
        List<TopicVoteResultResponse> result = topicVoteService.getResultPoll(pollId);
        return ResponseEntity.ok(result);
    }
}
