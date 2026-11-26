
package tn.esprit.autoloc.taskservice.service;

import tn.esprit.autoloc.taskservice.entity.TaskDependency;
import tn.esprit.autoloc.taskservice.repository.TaskDependencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskDependencyService {

    private final TaskDependencyRepository repository;

    public TaskDependencyService(TaskDependencyRepository repository) {
        this.repository = repository;
    }

    public List<TaskDependency> getAllDependencies() {
        return repository.findAll();
    }

    public TaskDependency getDependencyById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dependency not found"));
    }

    public TaskDependency createDependency(TaskDependency dependency) {
        return repository.save(dependency);
    }

    public TaskDependency updateDependency(Long id, TaskDependency updated) {
        TaskDependency dependency = getDependencyById(id);

        dependency.setTask(updated.getTask());
        dependency.setDependsOnTask(updated.getDependsOnTask());
        dependency.setDependencyType(updated.getDependencyType());

        return repository.save(dependency);
    }

    public void deleteDependency(Long id) {
        repository.delete(getDependencyById(id));
    }
}
