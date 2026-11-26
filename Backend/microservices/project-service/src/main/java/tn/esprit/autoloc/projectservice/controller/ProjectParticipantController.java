package tn.esprit.autoloc.projectservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.projectservice.entity.ProjectParticipant;
import tn.esprit.autoloc.projectservice.service.ProjectParticipantService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectParticipantController {

    private final ProjectParticipantService service;

    @PostMapping("/api/projects/{projectId}/participants")
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectParticipant create(@PathVariable Integer projectId, @Valid @RequestBody ProjectParticipant p) {
        return service.create(projectId, p);
    }

    @GetMapping("/api/projects/{projectId}/participants")
    public List<ProjectParticipant> byProject(@PathVariable Integer projectId) {
        return service.findByProject(projectId);
    }

    @GetMapping("/api/participants/{id}")
    public ProjectParticipant one(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PutMapping("/api/participants/{id}")
    public ProjectParticipant update(@PathVariable Integer id, @Valid @RequestBody ProjectParticipant p) {
        return service.update(id, p);
    }

    @DeleteMapping("/api/participants/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}