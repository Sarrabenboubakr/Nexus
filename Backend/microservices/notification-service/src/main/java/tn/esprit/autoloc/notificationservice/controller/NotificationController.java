
package tn.esprit.autoloc.notificationservice.controller;

import tn.esprit.autoloc.notificationservice.entity.Notification;
import tn.esprit.autoloc.notificationservice.service.NotificationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Notification> createNotification(
            @RequestBody Notification notification) {

        Notification created =
                notificationService.createNotification(notification);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(
                notificationService.getAllNotifications()
        );
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(
            @PathVariable String id) {

        return ResponseEntity.ok(
                notificationService.getNotificationById(id)
        );
    }

    // READ BY USER
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUser(
            @PathVariable String userId) {

        return ResponseEntity.ok(
                notificationService.getNotificationsByUser(userId)
        );
    }

    // READ UNREAD BY USER
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(
            @PathVariable String userId) {

        return ResponseEntity.ok(
                notificationService.getUnreadNotificationsByUser(userId)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Notification> updateNotification(
            @PathVariable String id,
            @RequestBody Notification notification) {

        return ResponseEntity.ok(
                notificationService.updateNotification(id, notification)
        );
    }

    // MARK AS READ
    @PatchMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(
            @PathVariable String id) {

        return ResponseEntity.ok(
                notificationService.markAsRead(id)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable String id) {

        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
