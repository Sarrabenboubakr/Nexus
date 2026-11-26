package tn.esprit.autoloc.organizationservice.repository;

import tn.esprit.autoloc.organizationservice.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {

    // Le tiret du bas (_) est OBLIGATOIRE ici.
    // Il dit à Spring : "Va dans l'attribut 'organization', puis prends son 'id'"
    List<Team> findByOrganization_Id(UUID organizationId);
}