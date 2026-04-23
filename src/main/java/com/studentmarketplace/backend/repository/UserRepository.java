package com.studentmarketplace.backend.repository;

import com.studentmarketplace.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for User entity.
 * Provides CRUD operations and custom query methods via Spring Data JPA.
 *
 * @author Student Marketplace Team
 * @version 0.1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(String role);

    List<User> findByStatus(String status);
}
