package com.studentmarketplace.backend.service;

import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for User business logic.
 * Acts as an intermediary between UserController and UserRepository.
 *
 * @author Student Marketplace Team
 * @version 0.1.0
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("A user with email " + user.getEmail() + " already exists.");
        }
        user.setCreatedAt(LocalDateTime.now());
        if (user.getRole() == null) user.setRole("USER");
        if (user.getStatus() == null) user.setStatus("ACTIVE");
        return userRepository.save(user);
    }

    public User updateUser(UUID id, User updatedUser) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));

        existing.setName(updatedUser.getName());
        existing.setEmail(updatedUser.getEmail());
        existing.setRole(updatedUser.getRole());
        existing.setStatus(updatedUser.getStatus());
        return userRepository.save(existing);
    }

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public List<User> getUsersByStatus(String status) {
        return userRepository.findByStatus(status);
    }

    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }
}
