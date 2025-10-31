package com.microinternship.skillbridge.controller;

import com.microinternship.skillbridge.entity.Notification;
import com.microinternship.skillbridge.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<Notification> getNotifications(@RequestParam String userType, @RequestParam Long userId) {
        return notificationService.getNotifications(userType, userId);
    }

    @GetMapping("/unread")
    public List<Notification> getUnreadNotifications(@RequestParam String userType, @RequestParam Long userId) {
        return notificationService.getUnreadNotifications(userType, userId);
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@RequestParam String userType, @RequestParam Long userId) {
        Long count = notificationService.getUnreadCount(userType, userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(@RequestParam String userType, @RequestParam Long userId) {
        notificationService.markAllAsRead(userType, userId);
        return ResponseEntity.ok().build();
    }
}
