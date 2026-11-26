package tn.esprit.autoloc.organizationservice.repository;

import tn.esprit.autoloc.organizationservice.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    // Le tiret du bas (_) est OBLIGATOIRE pour lier l'organisation
    List<Member> findByOrganization_Id(UUID organizationId);

    // Ici, on combine l'organisation et le userId (qui est un champ direct dans Member)
    boolean existsByOrganization_IdAndUserId(UUID organizationId, UUID userId);
}