package sba301.java.opentalk.service;

import sba301.java.opentalk.entity.User;

public interface NotificationService {
    public void sendNotification(User receiver, String content);
    public String countSentNotifications();
    public String getNotificationsSent();
}
