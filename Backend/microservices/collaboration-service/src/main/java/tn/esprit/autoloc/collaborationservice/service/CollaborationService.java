package tn.esprit.autoloc.collaborationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.collaborationservice.entity.Discussion;
import tn.esprit.autoloc.collaborationservice.entity.Mention;
import tn.esprit.autoloc.collaborationservice.entity.Message;
import tn.esprit.autoloc.collaborationservice.repository.DiscussionRepository;
import tn.esprit.autoloc.collaborationservice.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollaborationService {

    private final DiscussionRepository discussionRepository;
    private final MessageRepository messageRepository;

    // ===================== Discussions =====================

    public Discussion createDiscussion(Discussion discussion) {
        discussion.setId(null);
        discussion.setCreatedAt(LocalDateTime.now());
        return discussionRepository.save(discussion);
    }

    public List<Discussion> findAllDiscussions() {
        return discussionRepository.findAll();
    }

    public Discussion findDiscussion(String id) {
        return discussionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Discussion introuvable : " + id));
    }

    public Discussion updateDiscussion(String id, Discussion input) {
        Discussion existing = findDiscussion(id);
        existing.setTitre(input.getTitre());
        existing.setDescription(input.getDescription());
        existing.setClosed(input.isClosed());
        return discussionRepository.save(existing);
    }

    public void deleteDiscussion(String id) {
        findDiscussion(id);
        messageRepository.deleteByDiscussionId(id);   // suppression en cascade
        discussionRepository.deleteById(id);
    }

    // ===================== Messages =====================

    public Message addMessage(String discussionId, Message message) {
        Discussion discussion = findDiscussion(discussionId);
        if (discussion.isClosed()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La discussion est fermée");
        }
        message.setId(null);
        message.setDiscussionId(discussionId);
        message.setSentAt(LocalDateTime.now());
        if (message.getMentions() != null) {
            for (Mention mention : message.getMentions()) {
                mention.setMentionedAt(LocalDateTime.now());
            }
        }
        return messageRepository.save(message);
    }

    public List<Message> findMessages(String discussionId) {
        findDiscussion(discussionId);
        return messageRepository.findByDiscussionIdOrderBySentAtAsc(discussionId);
    }

    public Message findMessage(String messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Message introuvable : " + messageId));
    }

    public Message updateMessage(String messageId, Message input) {
        Message existing = findMessage(messageId);
        existing.setContent(input.getContent());
        if (input.getMentions() != null) {
            input.getMentions().forEach(m -> m.setMentionedAt(LocalDateTime.now()));
            existing.setMentions(input.getMentions());
        }
        return messageRepository.save(existing);
    }

    public void deleteMessage(String messageId) {
        findMessage(messageId);
        messageRepository.deleteById(messageId);
    }

    // ===================== Mentions =====================

    public List<Message> findMessagesMentioning(String userId) {
        return messageRepository.findByMentionsMentionedUserId(userId);
    }
}