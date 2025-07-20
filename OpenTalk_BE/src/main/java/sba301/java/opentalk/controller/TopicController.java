package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sba301.java.opentalk.dto.TopicDTO;
import sba301.java.opentalk.enums.TopicStatus;
import sba301.java.opentalk.model.request.DecisionRequest;
import sba301.java.opentalk.service.TopicService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/topic-idea")
public class TopicController {
    private final TopicService topicService;


    @GetMapping("/{id}")
    public ResponseEntity<TopicDTO> getTopic(@PathVariable long id) {
        return topicService.getTopic(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/suggestedBy/{id}")
    public List<TopicDTO> getTopicByUser(@PathVariable long id) {
        return topicService.getTopicsByUser(id);
    }

    @GetMapping("/status")
    public List<String> getStatus() {
        List<String> status = new ArrayList<>();
        Arrays.stream(TopicStatus.values()).map(Enum::toString).forEach(status::add);
        return status;
    }

    // GET /api/topics → trả về 200 + danh sách DTO (có thể rỗng)
    @GetMapping("/")
    public ResponseEntity<Page<TopicDTO>> getAllTopics(@RequestParam(defaultValue = "1") int pageNo,
                                                        @RequestParam(defaultValue = "8") int pageSize,
                                                        @RequestParam(defaultValue = "") String status,
                                                       @RequestParam(defaultValue = "") String title ) {
        Page<TopicDTO> page = topicService.findByStatusAndTitle(status, PageRequest.of(pageNo - 1, pageSize), title);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<TopicDTO> addTopic(@RequestBody TopicDTO dto) {
            topicService.addTopic(dto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/")
    public ResponseEntity<TopicDTO> updateTopic(
            @RequestBody TopicDTO dto) {
        TopicDTO updated = topicService.updateTopic(dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TopicDTO> deleteTopic(@PathVariable long id) {
        TopicDTO deleted = topicService.deleteTopic(id);
        return ResponseEntity.ok(deleted);
    }

    @PutMapping("/decision")
    public ResponseEntity<TopicDTO> decision(@RequestBody DecisionRequest decisionRequest) {
        TopicDTO dto = topicService.evaluteTopic(decisionRequest.getTopicId(), decisionRequest.getDecision(), decisionRequest.getUserId(), decisionRequest.getRemark());
        return ResponseEntity.ok(dto);
    }

}
