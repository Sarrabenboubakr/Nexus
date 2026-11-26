package tn.esprit.autoloc.projectservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.projectservice.entity.ProjectParticipant;
import tn.esprit.autoloc.projectservice.repository.ProjectParticipantRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectParticipantService {

    private final ProjectParticipantRepository repo;
    private final ProjectService projectService;

    public ProjectParticipant create(Integer projectId, ProjectParticipant p) {
        p.setId(null);
        p.setProject(projectService.findById(projectId));
        return repo.save(p);
    }

    public List<ProjectParticipant> findByProject(Integer projectId) {
        projectService.findById(projectId);
        return repo.findByProjectId(projectId);
    }

    public ProjectParticipant findById(Integer id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant " + id + " introuvable"));
    }

    public ProjectParticipant update(Integer id, ProjectParticipant data) {
        ProjectParticipant p = findById(id);
        p.setOrganizationId(data.getOrganizationId());
        p.setPartnershipId(data.getPartnershipId());
        p.setStatus(data.getStatus());
        p.setJoinedAt(data.getJoinedAt());
        return repo.save(p);
    }

    public void delete(Integer id) {
        repo.delete(findById(id));
    }
}