
package tn.esprit.autoloc.collaborationservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import tn.esprit.autoloc.collaborationservice.entity.Discussion;
import tn.esprit.autoloc.collaborationservice.entity.Message;
import tn.esprit.autoloc.collaborationservice.service.CollaborationService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CollaborationController {

    private final CollaborationService service;

    // =============== DISCUSSIONS ===============

    @PostMapping("/discussions")
    @ResponseStatus(HttpStatus.CREATED)
    public Discussion createDiscussion(
            @Valid @RequestBody Discussion discussion) {
        return service.createDiscussion(discussion);
    }

    @GetMapping("/discussions")
    public List<Discussion> getAllDiscussions() {
        return service.findAllDiscussions();
    }

    @GetMapping("/discussions/{id}")
    public Discussion getDiscussion(@PathVariable String id) {
        return service.findDiscussion(id);
    }

    @PutMapping("/discussions/{id}")
    public Discussion updateDiscussion(
            @PathVariable String id,
            @Valid @RequestBody Discussion discussion) {
        return service.updateDiscussion(id, discussion);
    }

    @DeleteMapping("/discussions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDiscussion(@PathVariable String id) {
        service.deleteDiscussion(id);
    }

    // =============== MESSAGES ===============

    @PostMapping("/discussions/{discussionId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public Message addMessage(
            @PathVariable String discussionId,
            @Valid @RequestBody Message message) {
        return service.addMessage(discussionId, message);
    }

    @GetMapping("/discussions/{discussionId}/messages")
    public List<Message> getMessages(
            @PathVariable String discussionId) {
        return service.findMessages(discussionId);
    }

    @GetMapping("/messages/{messageId}")
    public Message getMessage(@PathVariable String messageId) {
        return service.findMessage(messageId);
    }

    @PutMapping("/messages/{messageId}")
    public Message updateMessage(
            @PathVariable String messageId,
            @RequestBody Message message) {
        return service.updateMessage(messageId, message);
    }

    @DeleteMapping("/messages/{messageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable String messageId) {
        service.deleteMessage(messageId);
    }

    // =============== MENTIONS ===============

    @GetMapping("/mentions/{userId}")
    public List<Message> getMessagesMentioning(
            @PathVariable String userId) {
        return service.findMessagesMentioning(userId);
    }
}
