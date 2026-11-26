
package tn.esprit.autoloc.notificationservice.service;

import tn.esprit.autoloc.notificationservice.entity.Notification;
import tn.esprit.autoloc.notificationservice.repository.NotificationRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // CREATE
    public Notification createNotification(Notification notification) {
        notification.setId(null);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    // READ ALL
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    // READ BY ID
    public Notification getNotificationById(String id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Notification introuvable : " + id
                ));
    }

    // READ BY USER
    public List<Notification> getNotificationsByUser(String userId) {
        return notificationRepository.findByUserId(userId);
    }

    // READ UNREAD BY USER
    public List<Notification> getUnreadNotificationsByUser(String userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }

    // UPDATE
    public Notification updateNotification(String id, Notification updatedNotification) {
        Notification existing = getNotificationById(id);

        existing.setUserId(updatedNotification.getUserId());
        existing.setTitle(updatedNotification.getTitle());
        existing.setMessage(updatedNotification.getMessage());
        existing.setType(updatedNotification.getType());

        return notificationRepository.save(existing);
    }

    // MARK AS READ
    public Notification markAsRead(String id) {
        Notification notification = getNotificationById(id);
        notification.setRead(true);

        return notificationRepository.save(notification);
    }

    // DELETE
    public void deleteNotification(String id) {
        Notification notification = getNotificationById(id);
        notificationRepository.delete(notification);
    }
}
