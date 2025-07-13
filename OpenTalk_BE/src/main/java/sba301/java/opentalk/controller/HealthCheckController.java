package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import sba301.java.opentalk.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/health-check")
public class HealthCheckController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<String> getSentMails() {
        return ResponseEntity.ok(notificationService.countSentNotifications() + " - " + notificationService.getNotificationsSent());
    }
}
