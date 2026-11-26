
package tn.esprit.autoloc.taskservice.controller;

import tn.esprit.autoloc.taskservice.entity.TaskComment;
import tn.esprit.autoloc.taskservice.service.TaskCommentService;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/task-comments")
@CrossOrigin(origins = "*")
public class TaskCommentController {

    private final TaskCommentService service;

    public TaskCommentController(TaskCommentService service) {
        this.service = service;
    }

    @GetMapping
    public List<TaskComment> getAll() {
        return service.getAllComments();
    }

    @GetMapping("/{id}")
    public TaskComment getById(@PathVariable Long id) {
        return service.getCommentById(id);
    }

    @PostMapping
    public TaskComment create(@RequestBody TaskComment comment) {
        return service.createComment(comment);
    }

    @PutMapping("/{id}")
    public TaskComment update(@PathVariable Long id,
                              @RequestBody TaskComment comment) {
        return service.updateComment(id, comment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteComment(id);
    }
}
