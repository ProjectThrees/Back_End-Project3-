package com.studentmarketplace.backend.controller;

import com.studentmarketplace.backend.dto.ApiMapper;
import com.studentmarketplace.backend.dto.ListingRequestDto;
import com.studentmarketplace.backend.dto.ListingResponseDto;
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
    public ResponseEntity<List<ListingResponseDto>> getAllListings() {
        return ResponseEntity.ok(listingService.getAllListings().stream().map(ApiMapper::toListingResponse).toList());
    }

    // GET /listing/{listingId}
    @GetMapping("/{listingId}")
    public ResponseEntity<Optional<ListingResponseDto>> getListing(@PathVariable UUID listingId) {
        return ResponseEntity.ok(listingService.getListingById(listingId).map(ApiMapper::toListingResponse));
    }

    // GET /users/{userId}/listings
    @GetMapping("/user/{userId}/listings")
    public ResponseEntity<List<ListingResponseDto>> getListingsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(listingService.getListingsByUser(userId).stream().map(ApiMapper::toListingResponse).toList());
    }

    // POST /listing  - For creating new listing
    @PostMapping
    public ResponseEntity<ListingResponseDto> createListing(@RequestBody ListingRequestDto listingRequest) {
        return ResponseEntity.ok(ApiMapper.toListingResponse(listingService.createListing(ApiMapper.toListing(listingRequest))));
    }

    // PUT /listing/{listingId}
    @PutMapping("/{listingId}")
    public ResponseEntity<ListingResponseDto> updateListing(@PathVariable UUID listingId, @RequestBody ListingRequestDto listingRequest) {
        return ResponseEntity.ok(ApiMapper.toListingResponse(listingService.updateListing(listingId, ApiMapper.toListing(listingRequest))));
    }

    // PUT /listing/{listingId}/sold
    @PatchMapping("/{listingId}/sold")
    public ResponseEntity<ListingResponseDto> markAsSold(@PathVariable UUID listingId) {
        return ResponseEntity.ok(ApiMapper.toListingResponse(listingService.markAsSold(listingId)));
    }

    // DELETE /listing/{listingId}
    @DeleteMapping("/{listingId}")
    public ResponseEntity<Void> deleteListing(@PathVariable UUID listingId) {
        listingService.deleteListing(listingId);
        return ResponseEntity.noContent().build();
    }
}
