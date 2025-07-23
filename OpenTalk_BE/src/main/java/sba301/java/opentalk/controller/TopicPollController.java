package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sba301.java.opentalk.dto.PollDTO;
import sba301.java.opentalk.dto.TopicPollDTO;
import sba301.java.opentalk.service.PollService;
import sba301.java.opentalk.service.TopicPollService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/topic-poll")
public class TopicPollController {
    private final TopicPollService topicPollService;
    private final PollService pollService;

    @GetMapping("/all")
    public List<TopicPollDTO> getAll() {
        return topicPollService.getAll();
    }

    @PostMapping
    public ResponseEntity<Boolean> createNewTopicPoll(@RequestParam(required = false) long topicId,
                                                      @RequestParam(required = false) long pollId) {
        Boolean result = topicPollService.createTopicPoll(topicId,  pollId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{pollId}")
    public List<TopicPollDTO> getTopicPollByPollId(@PathVariable("pollId") int pollId) {
        return topicPollService.getTopicPollByPoll(pollId);
    }

    @GetMapping("/detail/{id}")
    public PollDTO getPollById(@PathVariable("id") int id) {
        return pollService.findById(id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTopicPoll(@PathVariable int id) {
        return topicPollService.deleteTopicPoll(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }



}
