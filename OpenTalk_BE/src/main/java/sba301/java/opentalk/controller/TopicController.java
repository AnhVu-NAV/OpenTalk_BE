package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sba301.java.opentalk.dto.TopicDTO;
import sba301.java.opentalk.service.TopicService;

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

    // GET /api/topics → trả về 200 + danh sách DTO (có thể rỗng)
    @GetMapping("/")
    public ResponseEntity<List<TopicDTO>> getAllTopics() {
        List<TopicDTO> list = topicService.getAllTopics();
        return ResponseEntity.ok(list);
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
}
