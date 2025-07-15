package sba301.java.opentalk.service;

import sba301.java.opentalk.entity.User;

public interface NotificationService {
    void sendNotification(User receiver, String content);
    String countSentNotifications();
    String getNotificationsSent();
}