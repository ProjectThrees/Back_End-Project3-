package com.studentmarketplace.backend.controller;

import com.studentmarketplace.backend.model.Favorite;
import com.studentmarketplace.backend.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * REST controller for Favorite endpoints.
 * Delegates all business logic to FavoriteService.
 *
 * @author Student Marketplace Team
 * @version 0.1.0
 */
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getFavoritesByUser(@PathVariable UUID userId) {
        try {
            return ResponseEntity.ok(favoriteService.getFavoritesByUser(userId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listing/{listingId}")
    public ResponseEntity<?> getFavoritesByListing(@PathVariable UUID listingId) {
        try {
            return ResponseEntity.ok(favoriteService.getFavoritesByListing(listingId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> isFavorited(@RequestParam UUID userId, @RequestParam UUID listingId) {
        try {
            boolean result = favoriteService.isFavorited(userId, listingId);
            return ResponseEntity.ok(result);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addFavorite(@RequestParam UUID userId, @RequestParam UUID listingId) {
        try {
            Favorite favorite = favoriteService.addFavorite(userId, listingId);
            return ResponseEntity.status(HttpStatus.CREATED).body(favorite);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<?> removeFavorite(@RequestParam UUID userId, @RequestParam UUID listingId) {
        try {
            favoriteService.removeFavorite(userId, listingId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
