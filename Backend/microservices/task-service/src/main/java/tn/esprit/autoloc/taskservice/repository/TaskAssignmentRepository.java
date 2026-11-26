package tn.esprit.autoloc.taskservice.repository;

import tn.esprit.autoloc.taskservice.entity.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Long> {
}