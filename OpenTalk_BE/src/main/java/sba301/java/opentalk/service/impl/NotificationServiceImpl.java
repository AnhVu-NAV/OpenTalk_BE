package sba301.java.opentalk.service.impl;

import lombok.RequiredArgsConstructor;
import sba301.java.opentalk.entity.User;
import sba301.java.opentalk.model.Mail.Mail;
import sba301.java.opentalk.service.MailService;
import sba301.java.opentalk.service.NotificationService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    MailService mailService;
    private final AtomicInteger numSentNotifications = new AtomicInteger(0);
    private int numIntegerSentNotifications = 0;

    @Override
    @Async
    public void sendNotification(User receiver, String content) {
        for (int i = 0; i < 1000; i++) {
            numSentNotifications.incrementAndGet();
            numIntegerSentNotifications++;
        }
        System.out.println("Sending email to: " + receiver.getEmail() + " on thread: " + Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        sendNotificationByMail(receiver, content);
    }

    @Override
    public String countSentNotifications() {
        return "Atomic variable: " + numSentNotifications;
    }

    @Override
    public String getNotificationsSent() {
        return "Integer variable: " + numIntegerSentNotifications;
    }

    public void sendNotificationByMail(User receiver, String content) {
        Mail mail = Mail.builder()
                .mailTo(new String[]{receiver.getEmail()})
                .mailSubject(content)
                .build();

        mailService.sendMail(mail);
    }
}
