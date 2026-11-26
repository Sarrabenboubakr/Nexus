package tn.esprit.autoloc.projectservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.projectservice.entity.ProjectMember;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Integer> {

    List<ProjectMember> findByProjectId(Integer projectId);

    void deleteByProjectId(Integer projectId);
}