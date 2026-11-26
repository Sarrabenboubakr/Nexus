package tn.esprit.autoloc.projectservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.projectservice.entity.Project;
import tn.esprit.autoloc.projectservice.service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Project create(@Valid @RequestBody Project p) {
        return service.create(p);
    }

    @GetMapping
    public List<Project> all(@RequestParam(required = false) Integer organizationId) {
        return organizationId != null ? service.findByOrganizationId(organizationId) : service.findAll();
    }

    @GetMapping("/{id}")
    public Project one(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public Project update(@PathVariable Integer id, @Valid @RequestBody Project p) {
        return service.update(id, p);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}