package com.studentmarketplace.backend.controller;

import com.studentmarketplace.backend.dto.ApiMapper;
import com.studentmarketplace.backend.dto.FavoriteRequestDto;
import com.studentmarketplace.backend.dto.FavoriteResponseDto;
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
    public ResponseEntity<List<FavoriteResponseDto>> getAllFavorites() {
        return ResponseEntity.ok(favoriteService.getAllFavorites().stream().map(ApiMapper::toFavoriteResponse).toList());
    }

    @PostMapping("/{listingId}")
    public ResponseEntity<FavoriteResponseDto> addToFavorite(@RequestBody FavoriteRequestDto favoriteRequest, @PathVariable UUID listingId) {
        return ResponseEntity.ok(ApiMapper.toFavoriteResponse(favoriteService.addFavorite(favoriteRequest.userId(), listingId)));

    }

    @DeleteMapping("/{listingId}")
    public ResponseEntity<Void> deleteFavorite(@RequestBody FavoriteRequestDto favoriteRequest, @PathVariable UUID listingId) {
        favoriteService.removeFavorite(favoriteRequest.userId(), listingId);
        return ResponseEntity.noContent().build();
    }
}
