package com.studentmarketplace.backend.service;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.repository.FavoriteRepository;
import com.studentmarketplace.backend.repository.ListingRepository;
import com.studentmarketplace.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ListingRepository listingRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    private UUID userId;
    private UUID listingId;
    private User user;
    private Listing listing;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        listingId = UUID.randomUUID();
        user = TestDataFactory.user(userId, "favorite@example.com");
        listing = TestDataFactory.listing(listingId, user);
    }

    @Test
    void addFavoriteRejectsDuplicateFavorite() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));
        when(favoriteRepository.existsByUserAndListing(user, listing)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> favoriteService.addFavorite(userId, listingId));
    }

    @Test
    void addFavoriteCreatesFavorite() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));
        when(favoriteRepository.existsByUserAndListing(user, listing)).thenReturn(false);
        when(favoriteRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var favorite = favoriteService.addFavorite(userId, listingId);

        assertEquals(userId, favorite.getUser().getUserId());
        assertEquals(listingId, favorite.getListing().getListingId());
        assertNotNull(favorite.getCreatedAt());
    }

    @Test
    void removeFavoriteDeletesExistingFavorite() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));
        when(favoriteRepository.existsByUserAndListing(user, listing)).thenReturn(true);

        favoriteService.removeFavorite(userId, listingId);

        verify(favoriteRepository).deleteByUserAndListing(user, listing);
    }

    @Test
    void removeFavoriteThrowsWhenMissing() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));
        when(favoriteRepository.existsByUserAndListing(user, listing)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> favoriteService.removeFavorite(userId, listingId));
    }
}
