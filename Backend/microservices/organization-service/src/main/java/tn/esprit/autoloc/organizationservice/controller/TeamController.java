package tn.esprit.autoloc.organizationservice.controller;

import tn.esprit.autoloc.organizationservice.exception.NotFoundException;
import tn.esprit.autoloc.organizationservice.model.Organization;
import tn.esprit.autoloc.organizationservice.model.Team;
import tn.esprit.autoloc.organizationservice.repository.OrganizationRepository;
import tn.esprit.autoloc.organizationservice.repository.TeamRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Transactional // <-- AJOUT TRÈS IMPORTANT ICI
public class TeamController {

    private final TeamRepository teamRepository;
    private final OrganizationRepository organizationRepository;

    public TeamController(TeamRepository teamRepository, OrganizationRepository organizationRepository) {
        this.teamRepository = teamRepository;
        this.organizationRepository = organizationRepository;
    }

    @PostMapping("/organizations/{orgId}/teams")
    public ResponseEntity<Team> create(@PathVariable UUID orgId, @Valid @RequestBody Team team) {
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new NotFoundException("Organisation introuvable : " + orgId));
        team.setId(null);
        team.setOrganization(org);
        return ResponseEntity.status(HttpStatus.CREATED).body(teamRepository.save(team));
    }

    @GetMapping("/organizations/{orgId}/teams")
    @Transactional(readOnly = true) // <-- Ajouté ici aussi pour la lecture
    public List<Team> getByOrganization(@PathVariable UUID orgId) {
        if (!organizationRepository.existsById(orgId)) {
            throw new NotFoundException("Organisation introuvable : " + orgId);
        }
        return teamRepository.findByOrganization_Id(orgId);
    }

    @GetMapping("/teams/{id}")
    @Transactional(readOnly = true) // <-- Ajouté ici aussi pour la lecture
    public Team getById(@PathVariable UUID id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipe introuvable : " + id));
    }

    @PutMapping("/teams/{id}")
    public Team update(@PathVariable UUID id, @Valid @RequestBody Team data) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipe introuvable : " + id));
        team.setName(data.getName());
        team.setDescription(data.getDescription());
        return teamRepository.save(team);
    }

    @DeleteMapping("/teams/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!teamRepository.existsById(id)) {
            throw new NotFoundException("Equipe introuvable : " + id);
        }
        teamRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}