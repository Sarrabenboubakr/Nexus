package tn.esprit.autoloc.taskservice.repository;

import tn.esprit.autoloc.taskservice.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}