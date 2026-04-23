package com.studentmarketplace.backend.service;

import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.repository.ListingRepository;
import com.studentmarketplace.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for Listing business logic.
 * Acts as an intermediary between ListingController and ListingRepository.
 *
 * @author Student Marketplace Team
 * @version 0.1.0
 */
@Service
public class ListingService {

    private final ListingRepository listingRepository;
    private final UserRepository userRepository;

    public ListingService(ListingRepository listingRepository, UserRepository userRepository) {
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
    }

    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    public Optional<Listing> getListingById(UUID id) {
        return listingRepository.findById(id);
    }

    public List<Listing> getListingsByUser(UUID userId) {
        return listingRepository.findByUserUserId(userId);
    }

    public List<Listing> getListingsByCategory(String category) {
        return listingRepository.findByCategory(category);
    }

    public List<Listing> getActiveListings() {
        return listingRepository.findByIsSold(false);
    }

    public Listing createListing(Listing listing) {
        if (listing.getUser() == null || listing.getUser().getUserId() == null) {
            throw new IllegalArgumentException("Listing must be associated with a valid user.");
        }
        User user = userRepository.findById(listing.getUser().getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + listing.getUser().getUserId()));
        listing.setUser(user);
        listing.setCreatedAt(LocalDateTime.now());
        if (listing.getIsSold() == null) listing.setIsSold(false);
        return listingRepository.save(listing);
    }

    public Listing updateListing(UUID id, Listing updatedListing) {
        Listing existing = listingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Listing not found with id: " + id));

        existing.setTitle(updatedListing.getTitle());
        existing.setDescription(updatedListing.getDescription());
        existing.setPrice(updatedListing.getPrice());
        existing.setCategory(updatedListing.getCategory());
        existing.setCondition(updatedListing.getCondition());
        existing.setImageUrl(updatedListing.getImageUrl());
        existing.setIsSold(updatedListing.getIsSold());
        return listingRepository.save(existing);
    }

    public Listing markAsSold(UUID id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Listing not found with id: " + id));
        listing.setIsSold(true);
        return listingRepository.save(listing);
    }

    public void deleteListing(UUID id) {
        if (!listingRepository.existsById(id)) {
            throw new NoSuchElementException("Listing not found with id: " + id);
        }
        listingRepository.deleteById(id);
    }
}
