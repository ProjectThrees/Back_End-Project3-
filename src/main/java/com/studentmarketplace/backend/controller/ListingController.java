package com.studentmarketplace.backend.controller;

import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.service.ListingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/listings")
public class ListingController {

    private final ListingService listingService;

    ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    // GET /listing
    @GetMapping
    public ResponseEntity<List<Listing>> getAllListings() {
        return ResponseEntity.ok(listingService.getAllListings());
    }

    // GET /listing/{listingId}
    @GetMapping("/{listingId}")
    public ResponseEntity<Optional<Listing>> getListing(@PathVariable UUID listingId) {
        return ResponseEntity.ok(listingService.getListingById(listingId));
    }

    // GET /users/{userId}/listings
    @GetMapping("/user/{userId}/listings")
    public ResponseEntity<List<Listing>> getListingsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(listingService.getListingsByUser(userId));
    }

    // POST /listing  - For creating new listing
    @PostMapping
    public ResponseEntity<Listing> createListing(@RequestBody Listing listing) {
        Listing created = listingService.createListing(listing);
        return ResponseEntity.ok(created);
    }

    // PUT /listing/{listingId}
    @PutMapping("/{listingId}")
    public ResponseEntity<Listing> updateListing(@PathVariable UUID listingId, @RequestBody Listing listing) {
        return ResponseEntity.ok(listingService.updateListing(listingId, listing));
    }

    // PUT /listing/{listingId}/sold
    @PatchMapping("/{listingId}/sold")
    public ResponseEntity<Listing> markAsSold(@PathVariable UUID listingId) {
        return ResponseEntity.ok(listingService.markAsSold(listingId));
    }

    // DELETE /listing/{listingId}
    @DeleteMapping("/{listingId}")
    public ResponseEntity<Void> deleteListing(@PathVariable UUID listingId) {
        listingService.deleteListing(listingId);
        return ResponseEntity.noContent().build();
    }
}
