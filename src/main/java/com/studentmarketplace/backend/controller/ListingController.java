package com.studentmarketplace.backend.controller;

import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.service.ListingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * REST controller for Listing endpoints.
 * Delegates all business logic to ListingService.
 *
 * @author Student Marketplace Team
 * @version 0.1.0
 */
@RestController
@RequestMapping("/api/listings")
public class ListingController {

    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @GetMapping
    public ResponseEntity<List<Listing>> getAllListings() {
        return ResponseEntity.ok(listingService.getAllListings());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Listing>> getActiveListings() {
        return ResponseEntity.ok(listingService.getActiveListings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Listing> getListingById(@PathVariable UUID id) {
        return listingService.getListingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Listing>> getListingsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(listingService.getListingsByUser(userId));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Listing>> getListingsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(listingService.getListingsByCategory(category));
    }

    @PostMapping
    public ResponseEntity<?> createListing(@RequestBody Listing listing) {
        try {
            Listing created = listingService.createListing(listing);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateListing(@PathVariable UUID id, @RequestBody Listing listing) {
        try {
            return ResponseEntity.ok(listingService.updateListing(id, listing));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/sold")
    public ResponseEntity<?> markAsSold(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(listingService.markAsSold(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable UUID id) {
        try {
            listingService.deleteListing(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
