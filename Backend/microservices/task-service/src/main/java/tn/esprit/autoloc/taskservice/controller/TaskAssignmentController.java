
package tn.esprit.autoloc.taskservice.controller;

import tn.esprit.autoloc.taskservice.entity.TaskAssignment;
import tn.esprit.autoloc.taskservice.service.TaskAssignmentService;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/task-assignments")
@CrossOrigin(origins = "*")
public class TaskAssignmentController {

    private final TaskAssignmentService service;

    public TaskAssignmentController(TaskAssignmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<TaskAssignment> getAll() {
        return service.getAllAssignments();
    }

    @GetMapping("/{id}")
    public TaskAssignment getById(@PathVariable Long id) {
        return service.getAssignmentById(id);
    }

    @PostMapping
    public TaskAssignment create(@RequestBody TaskAssignment assignment) {
        return service.createAssignment(assignment);
    }

    @PutMapping("/{id}")
    public TaskAssignment update(@PathVariable Long id,
                                 @RequestBody TaskAssignment assignment) {
        return service.updateAssignment(id, assignment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteAssignment(id);
    }
}
