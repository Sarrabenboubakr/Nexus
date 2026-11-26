package tn.esprit.autoloc.projectservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.projectservice.entity.Milestone;
import tn.esprit.autoloc.projectservice.service.MilestoneService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MilestoneController {

    private final MilestoneService service;

    @PostMapping("/api/projects/{projectId}/milestones")
    @ResponseStatus(HttpStatus.CREATED)
    public Milestone create(@PathVariable Integer projectId, @Valid @RequestBody Milestone m) {
        return service.create(projectId, m);
    }

    @GetMapping("/api/projects/{projectId}/milestones")
    public List<Milestone> byProject(@PathVariable Integer projectId) {
        return service.findByProject(projectId);
    }

    @GetMapping("/api/milestones/{id}")
    public Milestone one(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PutMapping("/api/milestones/{id}")
    public Milestone update(@PathVariable Integer id, @Valid @RequestBody Milestone m) {
        return service.update(id, m);
    }

    @DeleteMapping("/api/milestones/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}