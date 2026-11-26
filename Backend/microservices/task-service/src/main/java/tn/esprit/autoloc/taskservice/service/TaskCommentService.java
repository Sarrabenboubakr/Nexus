
package tn.esprit.autoloc.taskservice.service;

import tn.esprit.autoloc.taskservice.entity.TaskComment;
import tn.esprit.autoloc.taskservice.repository.TaskCommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskCommentService {

    private final TaskCommentRepository repository;

    public TaskCommentService(TaskCommentRepository repository) {
        this.repository = repository;
    }

    public List<TaskComment> getAllComments() {
        return repository.findAll();
    }

    public TaskComment getCommentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    public TaskComment createComment(TaskComment comment) {
        return repository.save(comment);
    }

    public TaskComment updateComment(Long id, TaskComment updated) {
        TaskComment comment = getCommentById(id);

        comment.setTask(updated.getTask());
        comment.setUserId(updated.getUserId());
        comment.setContent(updated.getContent());

        return repository.save(comment);
    }

    public void deleteComment(Long id) {
        repository.delete(getCommentById(id));
    }
}
