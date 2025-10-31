package com.microinternship.skillbridge.service;

import com.microinternship.skillbridge.entity.Notification;
import com.microinternship.skillbridge.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification createNotification(String userType, Long userId, String message, String notificationType, Long referenceId) {
        Notification notification = new Notification(userType, userId, message, notificationType, referenceId);
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotifications(String userType, Long userId) {
        return notificationRepository.findByUserTypeAndUserIdOrderByCreatedAtDesc(userType, userId);
    }

    public List<Notification> getUnreadNotifications(String userType, Long userId) {
        return notificationRepository.findByUserTypeAndUserIdAndIsReadOrderByCreatedAtDesc(userType, userId, false);
    }

    public Long getUnreadCount(String userType, Long userId) {
        return notificationRepository.countByUserTypeAndUserIdAndIsRead(userType, userId, false);
    }

    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        });
    }

    public void markAllAsRead(String userType, Long userId) {
        List<Notification> notifications = notificationRepository.findByUserTypeAndUserIdAndIsReadOrderByCreatedAtDesc(userType, userId, false);
        notifications.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(notifications);
    }
}
