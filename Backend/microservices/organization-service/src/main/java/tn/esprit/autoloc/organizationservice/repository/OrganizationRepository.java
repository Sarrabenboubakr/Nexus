package tn.esprit.autoloc.organizationservice.repository;

import tn.esprit.autoloc.organizationservice.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
}