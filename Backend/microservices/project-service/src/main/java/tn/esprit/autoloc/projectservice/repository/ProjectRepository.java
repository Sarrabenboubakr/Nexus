package tn.esprit.autoloc.projectservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.projectservice.entity.Project;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findByOrganizationId(Integer organizationId);
}