package com.studentmarketplace.backend.repository;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.model.Favorite;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FavoriteRepositoryTest {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListingRepository listingRepository;

    @Test
    void existsFindAndDeleteByUserAndListingWork() {
        User user = userRepository.save(TestDataFactory.user(UUID.randomUUID(), "favorite@example.com"));
        Listing listing = listingRepository.save(TestDataFactory.listing(UUID.randomUUID(), user));
        Favorite favorite = favoriteRepository.save(TestDataFactory.favorite(UUID.randomUUID(), user, listing));

        assertTrue(favoriteRepository.existsByUserAndListing(user, listing));

        List<Favorite> byUser = favoriteRepository.findByUser(user);
        assertEquals(1, byUser.size());
        assertEquals(favorite.getFavoriteId(), byUser.get(0).getFavoriteId());

        favoriteRepository.deleteByUserAndListing(user, listing);
        assertFalse(favoriteRepository.existsByUserAndListing(user, listing));
    }
}
