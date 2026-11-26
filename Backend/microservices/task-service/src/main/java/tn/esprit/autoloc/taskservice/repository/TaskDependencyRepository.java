package tn.esprit.autoloc.taskservice.repository;

import tn.esprit.autoloc.taskservice.entity.TaskDependency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskDependencyRepository extends JpaRepository<TaskDependency, Long> {
}