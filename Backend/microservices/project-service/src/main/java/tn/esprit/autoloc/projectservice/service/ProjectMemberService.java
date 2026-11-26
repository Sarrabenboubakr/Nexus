package tn.esprit.autoloc.projectservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.projectservice.entity.ProjectMember;
import tn.esprit.autoloc.projectservice.repository.ProjectMemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {

    private final ProjectMemberRepository repo;
    private final ProjectService projectService;

    public ProjectMember create(Integer projectId, ProjectMember m) {
        m.setId(null);
        m.setProject(projectService.findById(projectId));
        return repo.save(m);
    }

    public List<ProjectMember> findByProject(Integer projectId) {
        projectService.findById(projectId);
        return repo.findByProjectId(projectId);
    }

    public ProjectMember findById(Integer id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Member " + id + " introuvable"));
    }

    public ProjectMember update(Integer id, ProjectMember data) {
        ProjectMember m = findById(id);
        m.setUserId(data.getUserId());
        m.setMemberId(data.getMemberId());
        m.setRole(data.getRole());
        m.setStatus(data.getStatus());
        return repo.save(m);
    }

    public void delete(Integer id) {
        repo.delete(findById(id));
    }
}