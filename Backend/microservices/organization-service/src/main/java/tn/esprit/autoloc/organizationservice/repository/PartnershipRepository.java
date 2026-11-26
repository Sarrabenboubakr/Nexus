package tn.esprit.autoloc.organizationservice.repository;

import tn.esprit.autoloc.organizationservice.model.Partnership;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PartnershipRepository extends JpaRepository<Partnership, UUID> {
    List<Partnership> findByOrganizationIdOrPartnerOrganizationId(UUID a, UUID b);
    boolean existsByOrganizationIdAndPartnerOrganizationId(UUID a, UUID b);
}