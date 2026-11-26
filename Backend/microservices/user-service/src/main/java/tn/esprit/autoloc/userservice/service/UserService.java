package tn.esprit.autoloc.userservice.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.userservice.dto.UserRequest;
import tn.esprit.autoloc.userservice.model.User;
import tn.esprit.autoloc.userservice.repository.UserRepository;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User create(UserRequest request) {
        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmail(email)) {
            throw emailAlreadyExists();
        }

        try {
            return userRepository.save(new User(request.firstName().trim(), request.lastName().trim(), email,
                    normalizePhone(request.phone())));
        } catch (DataIntegrityViolationException exception) {
            throw emailAlreadyExists();
        }
    }

    public User update(UUID id, UserRequest request) {
        User user = findById(id);
        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmailAndIdNot(email, id)) {
            throw emailAlreadyExists();
        }

        user.update(request.firstName().trim(), request.lastName().trim(), email, normalizePhone(request.phone()));
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw emailAlreadyExists();
        }
    }

    public void delete(UUID id) {
        userRepository.delete(findById(id));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizePhone(String phone) {
        return phone == null || phone.isBlank() ? null : phone.trim();
    }

    private ResponseStatusException emailAlreadyExists() {
        return new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
    }
}