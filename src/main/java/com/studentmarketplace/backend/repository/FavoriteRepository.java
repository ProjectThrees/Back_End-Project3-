package com.studentmarketplace.backend.repository;

import com.studentmarketplace.backend.model.Favorite;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Favorite entity.
 * Provides CRUD operations and custom query methods via Spring Data JPA.
 *
 * @author Student Marketplace Team
 * @version 0.1.0
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {

    List<Favorite> findByUser(User user);

    List<Favorite> findByUserUserId(UUID userId);

    List<Favorite> findByListing(Listing listing);

    Optional<Favorite> findByUserAndListing(User user, Listing listing);

    boolean existsByUserAndListing(User user, Listing listing);

    void deleteByUserAndListing(User user, Listing listing);
}
