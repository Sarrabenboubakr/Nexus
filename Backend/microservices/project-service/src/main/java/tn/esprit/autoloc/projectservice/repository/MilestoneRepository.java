package tn.esprit.autoloc.projectservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.projectservice.entity.Milestone;

import java.util.List;

public interface MilestoneRepository extends JpaRepository<Milestone, Integer> {

    List<Milestone> findByProjectId(Integer projectId);

    void deleteByProjectId(Integer projectId);
}