
package tn.esprit.autoloc.notificationservice.repository;

import tn.esprit.autoloc.notificationservice.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository
        extends MongoRepository<Notification, String> {

    List<Notification> findByUserId(String userId);

    List<Notification> findByUserIdAndIsReadFalse(String userId);

    List<Notification> findByIsReadFalse();
}
