package com.studentmarketplace.backend.service;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.exception.BadRequestException;
import com.studentmarketplace.backend.exception.NotFoundException;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.repository.ListingRepository;
import com.studentmarketplace.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListingServiceTest {

    @Mock
    private ListingRepository listingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ListingService listingService;

    private UUID userId;
    private UUID listingId;
    private User user;
    private Listing listing;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        listingId = UUID.randomUUID();
        user = TestDataFactory.user(userId, "seller@example.com");
        listing = TestDataFactory.listing(listingId, user);
    }

    @Test
    void createListingRejectsMissingUser() {
        Listing invalidListing = new Listing();

        assertThrows(BadRequestException.class, () -> listingService.createListing(invalidListing));
        verify(listingRepository, never()).save(any());
    }

    @Test
    void createListingSetsCreatedAtAndDefaultsSoldFlag() {
        Listing newListing = TestDataFactory.listing(listingId, user);
        newListing.setCreatedAt(null);
        newListing.setIsSold(null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(listingRepository.save(any(Listing.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Listing created = listingService.createListing(newListing);

        assertNotNull(created.getCreatedAt());
        assertFalse(created.getIsSold());
        assertEquals(userId, created.getUser().getUserId());
    }

    @Test
    void updateListingUpdatesFields() {
        Listing updated = new Listing();
        updated.setTitle("Updated title");
        updated.setDescription("Updated description");
        updated.setPrice(listing.getPrice());
        updated.setCategory("Books");
        updated.setCondition("New");
        updated.setImageUrl("https://example.com/book.png");
        updated.setIsSold(true);

        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));
        when(listingRepository.save(any(Listing.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Listing result = listingService.updateListing(listingId, updated);

        assertEquals("Updated title", result.getTitle());
        assertEquals("Books", result.getCategory());
        assertTrue(result.getIsSold());
    }

    @Test
    void markAsSoldSetsFlag() {
        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));
        when(listingRepository.save(any(Listing.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Listing result = listingService.markAsSold(listingId);

        assertTrue(result.getIsSold());
    }

    @Test
    void deleteListingThrowsWhenMissing() {
        when(listingRepository.existsById(listingId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> listingService.deleteListing(listingId));
    }
}
