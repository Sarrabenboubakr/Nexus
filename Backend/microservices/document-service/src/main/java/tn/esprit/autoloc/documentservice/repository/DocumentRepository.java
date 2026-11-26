package tn.esprit.autoloc.documentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.documentservice.model.Document;

import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
}