package com.studentmarketplace.backend.repository;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ListingRepositoryTest {

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUserUserIdAndSoldFlagReturnMatchingListings() {
        User user = userRepository.save(TestDataFactory.user(UUID.randomUUID(), "seller@example.com"));

        Listing activeListing = TestDataFactory.listing(UUID.randomUUID(), user);
        listingRepository.save(activeListing);

        Listing soldListing = TestDataFactory.listing(UUID.randomUUID(), user);
        soldListing.setIsSold(true);
        soldListing.setTitle("Sold Laptop");
        listingRepository.save(soldListing);

        List<Listing> byUser = listingRepository.findByUserUserId(user.getUserId());
        List<Listing> active = listingRepository.findByIsSold(false);

        assertEquals(2, byUser.size());
        assertEquals(1, active.size());
        assertEquals("Laptop", active.get(0).getTitle());
    }
}
