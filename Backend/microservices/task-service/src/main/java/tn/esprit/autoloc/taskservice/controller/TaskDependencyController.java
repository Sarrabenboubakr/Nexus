
package tn.esprit.autoloc.taskservice.controller;

import tn.esprit.autoloc.taskservice.entity.TaskDependency;
import tn.esprit.autoloc.taskservice.service.TaskDependencyService;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/task-dependencies")
@CrossOrigin(origins = "*")
public class TaskDependencyController {

    private final TaskDependencyService service;

    public TaskDependencyController(TaskDependencyService service) {
        this.service = service;
    }

    @GetMapping
    public List<TaskDependency> getAll() {
        return service.getAllDependencies();
    }

    @GetMapping("/{id}")
    public TaskDependency getById(@PathVariable Long id) {
        return service.getDependencyById(id);
    }

    @PostMapping
    public TaskDependency create(@RequestBody TaskDependency dependency) {
        return service.createDependency(dependency);
    }

    @PutMapping("/{id}")
    public TaskDependency update(@PathVariable Long id,
                                 @RequestBody TaskDependency dependency) {
        return service.updateDependency(id, dependency);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteDependency(id);
    }
}
