package tn.esprit.autoloc.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.userservice.model.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);
}