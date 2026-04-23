package com.studentmarketplace.backend.service;

import com.studentmarketplace.backend.model.Favorite;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.repository.FavoriteRepository;
import com.studentmarketplace.backend.repository.ListingRepository;
import com.studentmarketplace.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for Favorite business logic.
 * Acts as an intermediary between FavoriteController and FavoriteRepository.
 *
 * @author Student Marketplace Team
 * @version 0.1.0
 */
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ListingRepository listingRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           UserRepository userRepository,
                           ListingRepository listingRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
    }

    public List<Favorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }

    public List<Favorite> getFavoritesByUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
        return favoriteRepository.findByUser(user);
    }

    public List<Favorite> getFavoritesByListing(UUID listingId) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new NoSuchElementException("Listing not found with id: " + listingId));
        return favoriteRepository.findByListing(listing);
    }

    public Optional<Favorite> getFavoriteById(UUID id) {
        return favoriteRepository.findById(id);
    }

    public boolean isFavorited(UUID userId, UUID listingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new NoSuchElementException("Listing not found with id: " + listingId));
        return favoriteRepository.existsByUserAndListing(user, listing);
    }

    public Favorite addFavorite(UUID userId, UUID listingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new NoSuchElementException("Listing not found with id: " + listingId));

        if (favoriteRepository.existsByUserAndListing(user, listing)) {
            throw new IllegalStateException("This listing is already in the user's favorites.");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setListing(listing);
        favorite.setCreatedAt(LocalDateTime.now());
        return favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavorite(UUID userId, UUID listingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new NoSuchElementException("Listing not found with id: " + listingId));

        if (!favoriteRepository.existsByUserAndListing(user, listing)) {
            throw new NoSuchElementException("Favorite not found for this user and listing.");
        }
        favoriteRepository.deleteByUserAndListing(user, listing);
    }
}
