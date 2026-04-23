package com.studentmarketplace.backend.controller;

import com.studentmarketplace.backend.model.Favorite;
import com.studentmarketplace.backend.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<List<Favorite>> getAllFavorites() {
        return ResponseEntity.ok(favoriteService.getAllFavorites());
    }

    @PostMapping("/{listingId}")
    public ResponseEntity<Favorite> addToFavorite(@RequestBody UUID userId, @PathVariable UUID listingId) {
        Favorite created = favoriteService.addFavorite(userId,listingId);
        return ResponseEntity.ok(created);

    }

    @DeleteMapping("/{listingId}")
    public ResponseEntity<Void> deleteFavorite(@RequestBody UUID userId, @PathVariable UUID listingId) {
        favoriteService.removeFavorite(userId,listingId);
        return ResponseEntity.noContent().build();
    }
}
