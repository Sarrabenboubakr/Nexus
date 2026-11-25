package tn.esprit.autoloc.collaborationservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.autoloc.collaborationservice.entity.Discussion;

import java.util.List;

public interface DiscussionRepository extends MongoRepository<Discussion, String> {
    List<Discussion> findByClosed(boolean closed);
}