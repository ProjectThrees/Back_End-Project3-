package com.studentmarketplace.backend.controller;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.dto.ListingRequestDto;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.service.ListingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ListingControllerTest {

    @Mock
    private ListingService listingService;

    private ListingController listingController;

    @BeforeEach
    void setUp() {
        listingController = new ListingController(listingService);
    }

    @Test
    void getAllListings_returnsOk() {
        User user = TestDataFactory.user(UUID.randomUUID(), "seller@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), user);
        when(listingService.getAllListings()).thenReturn(List.of(listing));

        ResponseEntity<?> response = listingController.getAllListings();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getListing_returnsOk() {
        User user = TestDataFactory.user(UUID.randomUUID(), "seller@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), user);
        when(listingService.getListingById(listing.getListingId())).thenReturn(Optional.of(listing));

        ResponseEntity<?> response = listingController.getListing(listing.getListingId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createListing_returnsOk() {
        User user = TestDataFactory.user(UUID.randomUUID(), "seller@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), user);
        when(listingService.createListing(any(Listing.class))).thenReturn(listing);

        ResponseEntity<?> response = listingController.createListing(
                new ListingRequestDto(user.getUserId(), "Laptop", "Good condition", new BigDecimal("499.99"), "Electronics", "Used", "https://example.com/laptop.png", false)
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void markAsSold_returnsOk() {
        User user = TestDataFactory.user(UUID.randomUUID(), "seller@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), user);
        listing.setIsSold(true);
        when(listingService.markAsSold(listing.getListingId())).thenReturn(listing);

        ResponseEntity<?> response = listingController.markAsSold(listing.getListingId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteListing_returnsNoContent() {
        UUID listingId = UUID.randomUUID();
        doNothing().when(listingService).deleteListing(listingId);

        ResponseEntity<Void> response = listingController.deleteListing(listingId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
