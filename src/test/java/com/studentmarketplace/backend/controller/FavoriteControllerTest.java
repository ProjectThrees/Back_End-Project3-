package com.studentmarketplace.backend.controller;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.dto.FavoriteRequestDto;
import com.studentmarketplace.backend.model.Favorite;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.service.FavoriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FavoriteControllerTest {

    @Mock
    private FavoriteService favoriteService;

    private FavoriteController favoriteController;

    @BeforeEach
    void setUp() {
        favoriteController = new FavoriteController(favoriteService);
    }

    @Test
    void getAllFavorites_returnsOk() {
        User user = TestDataFactory.user(UUID.randomUUID(), "favorite@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), user);
        Favorite favorite = TestDataFactory.favorite(UUID.randomUUID(), user, listing);
        when(favoriteService.getAllFavorites()).thenReturn(List.of(favorite));

        ResponseEntity<?> response = favoriteController.getAllFavorites();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addFavorite_returnsOk() {
        User user = TestDataFactory.user(UUID.randomUUID(), "favorite@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), user);
        Favorite favorite = TestDataFactory.favorite(UUID.randomUUID(), user, listing);
        when(favoriteService.addFavorite(eq(user.getUserId()), eq(listing.getListingId()))).thenReturn(favorite);

        ResponseEntity<?> response = favoriteController.addToFavorite(new FavoriteRequestDto(user.getUserId()), listing.getListingId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteFavorite_returnsNoContent() {
        UUID userId = UUID.randomUUID();
        UUID listingId = UUID.randomUUID();
        doNothing().when(favoriteService).removeFavorite(userId, listingId);

        ResponseEntity<Void> response = favoriteController.deleteFavorite(new FavoriteRequestDto(userId), listingId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
