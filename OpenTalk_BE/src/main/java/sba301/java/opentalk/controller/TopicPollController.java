package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<TopicPollDTO> createNewTopicPoll(@RequestBody TopicPollDTO topicPollDTO) {
        topicPollService.createTopicPoll(topicPollDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{pollId}")
    public List<TopicPollDTO> getTopicPollByPollId(@PathVariable("pollId") int pollId) {
        return topicPollService.getTopicPollByPoll(pollId);
    }

    @GetMapping("/detail/{id}")
    public PollDTO getPollById(@PathVariable("id") int id) {
        return pollService.findById(id);
    }

}
