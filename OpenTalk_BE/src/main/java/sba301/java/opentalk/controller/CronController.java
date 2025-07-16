package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sba301.java.opentalk.enums.CronKey;
import sba301.java.opentalk.service.CronExpressionService;

import java.util.Map;

@RestController
@RequestMapping("/api/cron")
@RequiredArgsConstructor
public class CronController {

    private final CronExpressionService cronExpressionService;

    @PutMapping("/update")
    public ResponseEntity<String> updateCron(@RequestParam CronKey key, @RequestParam String expression) {
        cronExpressionService.saveCronExpression(key, expression);
        return ResponseEntity.ok("Updated " + key.getKey());
    }

    @GetMapping("/get")
    public ResponseEntity<String> getCron(@RequestParam CronKey key) {
        String expression = cronExpressionService.getCronExpression(key);
        return ResponseEntity.ok(expression);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, String>> getAllCrons() {
        return ResponseEntity.ok(cronExpressionService.getAllCronExpressions());
    }
}