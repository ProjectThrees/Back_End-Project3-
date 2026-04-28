package com.studentmarketplace.backend.repository;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListingRepositoryTest {

    @Mock
    private ListingRepository listingRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = TestDataFactory.user(UUID.randomUUID(), "seller@example.com");
    }

    @Test
    void findByUserUserIdAndSoldFlagReturnMatchingListings() {
        Listing activeListing = TestDataFactory.listing(UUID.randomUUID(), user);
        Listing soldListing = TestDataFactory.listing(UUID.randomUUID(), user);
        soldListing.setIsSold(true);
        soldListing.setTitle("Sold Laptop");
        when(listingRepository.findByUserUserId(user.getUserId())).thenReturn(List.of(activeListing, soldListing));
        when(listingRepository.findByIsSold(false)).thenReturn(List.of(activeListing));

        List<Listing> byUser = listingRepository.findByUserUserId(user.getUserId());
        List<Listing> active = listingRepository.findByIsSold(false);

        assertEquals(2, byUser.size());
        assertEquals(1, active.size());
        assertEquals("Laptop", active.get(0).getTitle());
        verify(listingRepository).findByUserUserId(user.getUserId());
        verify(listingRepository).findByIsSold(false);
    }
}
