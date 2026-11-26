package tn.esprit.autoloc.projectservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.projectservice.entity.ProjectParticipant;

import java.util.List;

public interface ProjectParticipantRepository extends JpaRepository<ProjectParticipant, Integer> {

    List<ProjectParticipant> findByProjectId(Integer projectId);

    void deleteByProjectId(Integer projectId);
}