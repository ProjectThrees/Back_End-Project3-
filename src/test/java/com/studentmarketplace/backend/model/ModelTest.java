package com.studentmarketplace.backend.model;

import com.studentmarketplace.backend.TestDataFactory;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ModelTest {

    @Test
    void userModelStoresAssignedFields() {
        User user = TestDataFactory.user(UUID.randomUUID(), "user@example.com");

        assertEquals("Test User", user.getName());
        assertEquals("user@example.com", user.getEmail());
        assertEquals("USER", user.getRole());
        assertEquals("ACTIVE", user.getStatus());
    }

    @Test
    void listingModelDefaultSoldFlagIsFalse() {
        Listing listing = new Listing();

        assertFalse(listing.getIsSold());
    }

    @Test
    void messageModelStoresRelations() {
        User sender = TestDataFactory.user(UUID.randomUUID(), "sender@example.com");
        User receiver = TestDataFactory.user(UUID.randomUUID(), "receiver@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), sender);
        Message message = TestDataFactory.message(UUID.randomUUID(), sender, receiver, listing);

        assertEquals(sender.getUserId(), message.getSender().getUserId());
        assertEquals(receiver.getUserId(), message.getReceiver().getUserId());
        assertEquals(listing.getListingId(), message.getListing().getListingId());
    }

    @Test
    void favoriteModelStoresUserAndListing() {
        User user = TestDataFactory.user(UUID.randomUUID(), "favorite@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), user);
        Favorite favorite = TestDataFactory.favorite(UUID.randomUUID(), user, listing);

        assertEquals(user.getUserId(), favorite.getUser().getUserId());
        assertEquals(listing.getListingId(), favorite.getListing().getListingId());
    }

    @Test
    void reportModelDefaultStatusIsPending() {
        Report report = new Report();

        assertEquals("PENDING", report.getStatus());
    }
}
