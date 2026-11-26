package tn.esprit.autoloc.projectservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.projectservice.entity.Milestone;
import tn.esprit.autoloc.projectservice.repository.MilestoneRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MilestoneService {

    private final MilestoneRepository repo;
    private final ProjectService projectService;

    public Milestone create(Integer projectId, Milestone m) {
        m.setId(null);
        m.setProject(projectService.findById(projectId)); // 404 si le projet n'existe pas
        return repo.save(m);
    }

    public List<Milestone> findByProject(Integer projectId) {
        projectService.findById(projectId);
        return repo.findByProjectId(projectId);
    }

    public Milestone findById(Integer id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Milestone " + id + " introuvable"));
    }

    public Milestone update(Integer id, Milestone data) {
        Milestone m = findById(id);
        m.setName(data.getName());
        m.setDescription(data.getDescription());
        m.setDueDate(data.getDueDate());
        m.setStatus(data.getStatus());
        return repo.save(m);
    }

    public void delete(Integer id) {
        repo.delete(findById(id));
    }
}