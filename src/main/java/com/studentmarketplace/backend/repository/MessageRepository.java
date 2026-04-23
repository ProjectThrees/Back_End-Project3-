package com.studentmarketplace.backend.repository;

import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.Message;
import com.studentmarketplace.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Message entity.
 * Provides CRUD operations and custom query methods via Spring Data JPA.
 *
 * @author Student Marketplace Team
 * @version 0.1.0
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findBySender(User sender);

    List<Message> findByReceiver(User receiver);

    List<Message> findByListing(Listing listing);

    List<Message> findByListingListingId(UUID listingId);

    List<Message> findBySenderAndReceiver(User sender, User receiver);

    List<Message> findBySenderUserIdOrReceiverUserId(UUID senderId, UUID receiverId);
}
