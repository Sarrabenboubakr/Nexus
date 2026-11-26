package tn.esprit.autoloc.taskservice.repository;

import tn.esprit.autoloc.taskservice.entity.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {
}