package tn.esprit.autoloc.projectservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.projectservice.entity.Project;
import tn.esprit.autoloc.projectservice.repository.MilestoneRepository;
import tn.esprit.autoloc.projectservice.repository.ProjectMemberRepository;
import tn.esprit.autoloc.projectservice.repository.ProjectParticipantRepository;
import tn.esprit.autoloc.projectservice.repository.ProjectRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository repo;
    private final MilestoneRepository milestoneRepo;
    private final ProjectMemberRepository memberRepo;
    private final ProjectParticipantRepository participantRepo;

    public Project create(Project p) {
        p.setId(null);
        return repo.save(p);
    }

    public List<Project> findAll() {
        return repo.findAll();
    }

    public List<Project> findByOrganizationId(Integer organizationId) {
        return repo.findByOrganizationId(organizationId);
    }

    public Project findById(Integer id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Project " + id + " introuvable"));
    }

    public Project update(Integer id, Project data) {
        Project p = findById(id);
        p.setOrganizationId(data.getOrganizationId());
        p.setName(data.getName());
        p.setDescription(data.getDescription());
        p.setStartDate(data.getStartDate());
        p.setEndDate(data.getEndDate());
        p.setStatus(data.getStatus());
        p.setPriority(data.getPriority());
        return repo.save(p);
    }

    @Transactional
    public void delete(Integer id) {
        Project p = findById(id);
        milestoneRepo.deleteByProjectId(id);
        memberRepo.deleteByProjectId(id);
        participantRepo.deleteByProjectId(id);
        repo.delete(p);
    }
}