package tn.esprit.autoloc.organizationservice.controller;

import tn.esprit.autoloc.organizationservice.exception.BadRequestException;
import tn.esprit.autoloc.organizationservice.exception.NotFoundException;
import tn.esprit.autoloc.organizationservice.model.Partnership;
import tn.esprit.autoloc.organizationservice.repository.OrganizationRepository;
import tn.esprit.autoloc.organizationservice.repository.PartnershipRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/partnerships")
public class PartnershipController {

    private final PartnershipRepository partnershipRepository;
    private final OrganizationRepository organizationRepository;

    public PartnershipController(PartnershipRepository partnershipRepository,
                                 OrganizationRepository organizationRepository) {
        this.partnershipRepository = partnershipRepository;
        this.organizationRepository = organizationRepository;
    }

    @PostMapping
    public ResponseEntity<Partnership> invite(@Valid @RequestBody Partnership p) {
        if (p.getOrganizationId().equals(p.getPartnerOrganizationId())) {
            throw new BadRequestException("Une organisation ne peut pas s'inviter elle-meme");
        }
        if (!organizationRepository.existsById(p.getOrganizationId())
                || !organizationRepository.existsById(p.getPartnerOrganizationId())) {
            throw new NotFoundException("Organisation ou partenaire introuvable");
        }
        if (partnershipRepository.existsByOrganizationIdAndPartnerOrganizationId(
                p.getOrganizationId(), p.getPartnerOrganizationId())
                || partnershipRepository.existsByOrganizationIdAndPartnerOrganizationId(
                p.getPartnerOrganizationId(), p.getOrganizationId())) {
            throw new BadRequestException("Un partenariat existe deja entre ces deux organisations");
        }
        p.setId(null);
        p.setStatus("PENDING");
        return ResponseEntity.status(HttpStatus.CREATED).body(partnershipRepository.save(p));
    }

    @GetMapping
    public List<Partnership> getAll() {
        return partnershipRepository.findAll();
    }

    @GetMapping("/{id}")
    public Partnership getById(@PathVariable UUID id) {
        return partnershipRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Partenariat introuvable : " + id));
    }

    @GetMapping("/organization/{orgId}")
    public List<Partnership> getByOrganization(@PathVariable UUID orgId) {
        return partnershipRepository.findByOrganizationIdOrPartnerOrganizationId(orgId, orgId);
    }

    @PatchMapping("/{id}/status")
    public Partnership updateStatus(@PathVariable UUID id, @RequestBody Map<String, String> body) {
        Partnership p = partnershipRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Partenariat introuvable : " + id));
        String status = body.get("status");
        if (!"ACCEPTED".equals(status) && !"REJECTED".equals(status)) {
            throw new BadRequestException("Statut invalide : utilisez ACCEPTED ou REJECTED");
        }
        p.setStatus(status);
        if ("ACCEPTED".equals(status)) p.setAcceptedAt(LocalDateTime.now());
        return partnershipRepository.save(p);
    }

    @GetMapping("/active")
    public Map<String, Boolean> isActive(@RequestParam UUID orgA, @RequestParam UUID orgB) {
        boolean active = partnershipRepository.findByOrganizationIdOrPartnerOrganizationId(orgA, orgA)
                .stream()
                .anyMatch(p -> "ACCEPTED".equals(p.getStatus())
                        && ((p.getOrganizationId().equals(orgA) && p.getPartnerOrganizationId().equals(orgB))
                        || (p.getOrganizationId().equals(orgB) && p.getPartnerOrganizationId().equals(orgA))));
        return Map.of("active", active);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!partnershipRepository.existsById(id)) {
            throw new NotFoundException("Partenariat introuvable : " + id);
        }
        partnershipRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}