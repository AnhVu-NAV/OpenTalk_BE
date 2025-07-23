package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sba301.java.opentalk.enums.CronKey;
import sba301.java.opentalk.model.CronjobItemDTO;
import sba301.java.opentalk.service.CronExpressionService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cron")
@RequiredArgsConstructor
public class CronController {

    private final CronExpressionService cronExpressionService;

    @PutMapping("/update")
    public ResponseEntity<CronjobItemDTO> updateCron(
            @RequestParam CronKey key,
            @RequestParam String expression
    ) {
        cronExpressionService.saveCronExpression(key, expression);

        CronjobItemDTO dto = CronjobItemDTO.builder()
                .cronjobKey(key.getKey())
                .cronjobValue(expression)
                .build();

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/get")
    public ResponseEntity<CronjobItemDTO> getCron(@RequestParam CronKey key) {
        String expression = cronExpressionService.getCronExpression(key);

        CronjobItemDTO dto = CronjobItemDTO.builder()
                .cronjobKey(key.getKey())
                .cronjobValue(expression)
                .build();

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CronjobItemDTO>> getAllCrons() {
        Map<String, String> all = cronExpressionService.getAllCronExpressions();
        List<CronjobItemDTO> list = all.entrySet().stream()
                .map(e -> CronjobItemDTO.builder()
                        .cronjobKey(e.getKey())
                        .cronjobValue(e.getValue())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

}