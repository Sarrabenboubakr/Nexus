
package tn.esprit.autoloc.taskservice.service;

import tn.esprit.autoloc.taskservice.entity.TaskAssignment;
import tn.esprit.autoloc.taskservice.repository.TaskAssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskAssignmentService {

    private final TaskAssignmentRepository repository;

    public TaskAssignmentService(TaskAssignmentRepository repository) {
        this.repository = repository;
    }

    public List<TaskAssignment> getAllAssignments() {
        return repository.findAll();
    }

    public TaskAssignment getAssignmentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
    }

    public TaskAssignment createAssignment(TaskAssignment assignment) {
        return repository.save(assignment);
    }

    public TaskAssignment updateAssignment(Long id, TaskAssignment updated) {
        TaskAssignment assignment = getAssignmentById(id);

        assignment.setTask(updated.getTask());
        assignment.setUserId(updated.getUserId());
        assignment.setAssignedBy(updated.getAssignedBy());
        assignment.setStatus(updated.getStatus());

        return repository.save(assignment);
    }

    public void deleteAssignment(Long id) {
        repository.delete(getAssignmentById(id));
    }
}
