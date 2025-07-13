package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import sba301.java.opentalk.service.RedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cron")
@RequiredArgsConstructor
public class CronController {
    private final RedisService redisService;

    @PutMapping("/update-daysUntilTalk")
    public ResponseEntity<Integer> updateDaysUntilTalk(@RequestParam int daysUntilTalk) {
        redisService.saveDaysUntilOpenTalk(daysUntilTalk);
        return ResponseEntity.ok(daysUntilTalk);
    }

    @PutMapping("/update-randomDate")
    public ResponseEntity<String> updateRandomCron(@RequestParam String cron) {
        redisService.saveRandomDateCron(cron);
        return ResponseEntity.ok(cron);
    }
}
