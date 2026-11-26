package tn.esprit.autoloc.organizationservice.controller;

import tn.esprit.autoloc.organizationservice.exception.BadRequestException;
import tn.esprit.autoloc.organizationservice.exception.NotFoundException;
import tn.esprit.autoloc.organizationservice.model.Member;
import tn.esprit.autoloc.organizationservice.model.Organization;
import tn.esprit.autoloc.organizationservice.model.Team;
import tn.esprit.autoloc.organizationservice.repository.MemberRepository;
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
@Transactional // <-- AJOUT IMPORTANT pour éviter les erreurs 500 au chargement
public class MemberController {

    private final MemberRepository memberRepository;
    private final OrganizationRepository organizationRepository;
    private final TeamRepository teamRepository;

    public MemberController(MemberRepository memberRepository,
                            OrganizationRepository organizationRepository,
                            TeamRepository teamRepository) {
        this.memberRepository = memberRepository;
        this.organizationRepository = organizationRepository;
        this.teamRepository = teamRepository;
    }

    @PostMapping("/organizations/{orgId}/members")
    public ResponseEntity<Member> create(@PathVariable UUID orgId,
                                         @RequestParam(required = false) UUID teamId,
                                         @Valid @RequestBody Member member) {
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new NotFoundException("Organisation introuvable : " + orgId));

        // CORRECTION ICI : ajout du tiret du bas (_)
        if (memberRepository.existsByOrganization_IdAndUserId(orgId, member.getUserId())) {
            throw new BadRequestException("Cet utilisateur est deja membre de cette organisation");
        }

        member.setId(null);
        member.setOrganization(org);

        if (teamId != null) {
            Team team = teamRepository.findById(teamId)
                    .orElseThrow(() -> new NotFoundException("Equipe introuvable : " + teamId));
            if (!team.getOrganizationId().equals(orgId)) {
                throw new BadRequestException("Cette equipe n'appartient pas a l'organisation");
            }
            member.setTeam(team);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(memberRepository.save(member));
    }

    @GetMapping("/organizations/{orgId}/members")
    @Transactional(readOnly = true)
    public List<Member> getByOrganization(@PathVariable UUID orgId) {
        if (!organizationRepository.existsById(orgId)) {
            throw new NotFoundException("Organisation introuvable : " + orgId);
        }
        // CORRECTION ICI : ajout du tiret du bas (_)
        return memberRepository.findByOrganization_Id(orgId);
    }

    @GetMapping("/members/{id}")
    @Transactional(readOnly = true)
    public Member getById(@PathVariable UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Membre introuvable : " + id));
    }

    @PutMapping("/members/{id}")
    public Member update(@PathVariable UUID id, @RequestBody Member data) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Membre introuvable : " + id));
        if (data.getJobTitle() != null) member.setJobTitle(data.getJobTitle());
        if (data.getStatus() != null) member.setStatus(data.getStatus());
        return memberRepository.save(member);
    }

    @DeleteMapping("/organizations/{orgId}/members/{memberId}")
    public ResponseEntity<Void> delete(@PathVariable UUID orgId, @PathVariable UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Membre introuvable : " + memberId));

        if (member.getOrganization() == null || !member.getOrganization().getId().equals(orgId)) {
            throw new BadRequestException("Ce membre n'appartient pas a cette organisation");
        }
        memberRepository.delete(member);
        return ResponseEntity.noContent().build();
    }
}