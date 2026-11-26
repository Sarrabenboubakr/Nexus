package tn.esprit.autoloc.collaborationservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.autoloc.collaborationservice.entity.Message;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByDiscussionIdOrderBySentAtAsc(String discussionId);
    List<Message> findByMentionsMentionedUserId(String userId);
    void deleteByDiscussionId(String discussionId);
}