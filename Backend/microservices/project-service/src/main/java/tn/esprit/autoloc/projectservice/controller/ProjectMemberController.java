package tn.esprit.autoloc.projectservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.projectservice.entity.ProjectMember;
import tn.esprit.autoloc.projectservice.service.ProjectMemberService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectMemberController {

    private final ProjectMemberService service;

    @PostMapping("/api/projects/{projectId}/members")
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectMember create(@PathVariable Integer projectId, @Valid @RequestBody ProjectMember m) {
        return service.create(projectId, m);
    }

    @GetMapping("/api/projects/{projectId}/members")
    public List<ProjectMember> byProject(@PathVariable Integer projectId) {
        return service.findByProject(projectId);
    }

    @GetMapping("/api/members/{id}")
    public ProjectMember one(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PutMapping("/api/members/{id}")
    public ProjectMember update(@PathVariable Integer id, @Valid @RequestBody ProjectMember m) {
        return service.update(id, m);
    }

    @DeleteMapping("/api/members/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}