package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sba301.java.opentalk.dto.TopicPollDTO;
import sba301.java.opentalk.service.TopicPollService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/topic-idea")
public class TopicPollController {
    private final TopicPollService topicPollService;

    @GetMapping("/topic-poll")
    public ResponseEntity<TopicPollDTO> createNewTopicPoll(@RequestBody TopicPollDTO topicPollDTO) {
        topicPollService.createTopicPoll(topicPollDTO);
        return ResponseEntity.ok().build();
    }
}
