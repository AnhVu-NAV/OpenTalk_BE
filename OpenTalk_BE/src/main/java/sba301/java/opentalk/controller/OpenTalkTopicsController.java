package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import sba301.java.opentalk.dto.OpenTalkRegistrationDTO;
import sba301.java.opentalk.dto.OpenTalkTopicDTO;
import sba301.java.opentalk.enums.OpenTalkRegistrationStatus;
import sba301.java.opentalk.model.request.OpenTalkCompletedRequest;
import sba301.java.opentalk.service.OpenTalkRegistrationService;
import sba301.java.opentalk.service.OpenTalkTopicService;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/opentalk-topic")
public class OpenTalkTopicsController {
    private final OpenTalkTopicService openTalkTopicService;
    private final OpenTalkRegistrationService openTalkRegistrationService;

    @GetMapping
    public ResponseEntity<List<OpenTalkTopicDTO>> getTopics() {
        List<OpenTalkTopicDTO> dtos = openTalkTopicService.getAllTopics();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{userId}/registered")
    public ResponseEntity<Page<OpenTalkTopicDTO>> getRegisteredOpenTalks(
            @PathVariable Long userId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<OpenTalkTopicDTO> dtos = openTalkTopicService.getRegisteredOpenTalks(userId, startDate, endDate, page, size);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/completed")
    public ResponseEntity<Page<OpenTalkTopicDTO>> getCompletedTopics(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Long companyBranchId,
            @RequestParam(required = false) String hostName,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) boolean enableOfHost,
            @RequestParam(required = false) boolean isOrganized) {

        OpenTalkCompletedRequest request = new OpenTalkCompletedRequest(
                page, size, companyBranchId, hostName, isOrganized, enableOfHost, startDate, endDate
        );
        Page<OpenTalkTopicDTO> dtos = openTalkTopicService.getTopicsCompleted(request);
        return ResponseEntity.ok(dtos);
    }


    @PostMapping
    public ResponseEntity<OpenTalkTopicDTO> createOpenTalkTopic(@RequestBody OpenTalkTopicDTO dto) {
        OpenTalkTopicDTO openTalkTopicDTO = openTalkTopicService.createTopic(dto);
        return ResponseEntity.ok(openTalkTopicDTO);
    }

    @PostMapping("/completedOpenTalk/create")
    public ResponseEntity<String> createCompletedOpenTalkTopic() {
        openTalkTopicService.createScheduledOpenTalk();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/employee/{openTalkTopicId}/register")
    public ResponseEntity<OpenTalkTopicDTO> registerOpenTalkTopicByEmployee(@RequestBody OpenTalkRegistrationDTO registrationDTO) {
        registrationDTO.setStatus(OpenTalkRegistrationStatus.PENDING);
        openTalkRegistrationService.registerOpenTalk(registrationDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/{openTalkTopicId}/register")
    public ResponseEntity<OpenTalkTopicDTO> registerOpenTalkTopicByAdmin(@RequestBody OpenTalkRegistrationDTO registrationDTO) {
        registrationDTO.setStatus(OpenTalkRegistrationStatus.APPROVED);
        Locale locale = LocaleContextHolder.getLocale();
        openTalkRegistrationService.registerOpenTalk(registrationDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{openTalkTopicId}")
    public ResponseEntity<OpenTalkTopicDTO> updateOpenTalkTopic(@RequestBody OpenTalkTopicDTO dto) {
        OpenTalkTopicDTO openTalkTopicDTO = openTalkTopicService.updateTopic(dto);
        return ResponseEntity.ok(openTalkTopicDTO);
    }

    @DeleteMapping("/{openTalkTopicId}")
    public ResponseEntity<Void> deleteOpenTalkTopic(@PathVariable Long openTalkTopicId) {
        return openTalkTopicService.deleteTopic(openTalkTopicId) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/register/{openTalkTopicId}")
    public ResponseEntity<List<OpenTalkRegistrationDTO>> getRegisteredOpenTalks(@PathVariable Long openTalkTopicId) {
        return ResponseEntity.ok().body(openTalkRegistrationService.findByOpenTalkTopicId(openTalkTopicId));
    }

    @GetMapping("/register/native-query/{openTalkTopicId}")
    public ResponseEntity<List<OpenTalkRegistrationDTO>> getRegisteredOpenTalksWithNativeQuery(@PathVariable Long openTalkTopicId) {
        return ResponseEntity.ok().body(openTalkRegistrationService.findByOpenTalkTopicIdWithNativeQuery(openTalkTopicId));
    }

    @GetMapping("/register/interface-projection/{openTalkTopicId}")
    public ResponseEntity<List<OpenTalkRegistrationDTO>> getRegisteredOpenTalksWithInterfaceProjection(@PathVariable Long openTalkTopicId) {
        return ResponseEntity.ok().body(openTalkRegistrationService.findByOpenTalkTopicIdWithInterfaceProjection(openTalkTopicId));
    }

    @GetMapping("mail/remind/{openTalkTopicId}")
    public ResponseEntity<String> sendRemindEmail(@PathVariable Long openTalkTopicId) {
        openTalkTopicService.sendMailRemind(openTalkTopicId);
        return ResponseEntity.ok().build();
    }
}
