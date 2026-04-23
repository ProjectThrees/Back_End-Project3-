package com.studentmarketplace.backend.repository;

import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Listing entity.
 * Provides CRUD operations and custom query methods via Spring Data JPA.
 *
 * @author Student Marketplace Team
 * @version 0.1.0
 */
@Repository
public interface ListingRepository extends JpaRepository<Listing, UUID> {

    List<Listing> findByUser(User user);

    List<Listing> findByUserUserId(UUID userId);

    List<Listing> findByCategory(String category);

    List<Listing> findByIsSold(Boolean isSold);

    List<Listing> findByUserAndIsSold(User user, Boolean isSold);

    List<Listing> findByCategoryAndIsSold(String category, Boolean isSold);
}
