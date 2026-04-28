package com.studentmarketplace.backend;

import com.studentmarketplace.backend.model.Favorite;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.Message;
import com.studentmarketplace.backend.model.Report;
import com.studentmarketplace.backend.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static User user(UUID userId, String email) {
        User user = new User();
        user.setUserId(userId);
        user.setName("Test User");
        user.setEmail(email);
        user.setRole("USER");
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));
        return user;
    }

    public static Listing listing(UUID listingId, User user) {
        Listing listing = new Listing();
        listing.setListingId(listingId);
        listing.setUser(user);
        listing.setTitle("Laptop");
        listing.setDescription("Good condition");
        listing.setPrice(new BigDecimal("499.99"));
        listing.setCategory("Electronics");
        listing.setCondition("Used");
        listing.setImageUrl("https://example.com/laptop.png");
        listing.setIsSold(false);
        listing.setCreatedAt(LocalDateTime.of(2026, 1, 2, 10, 0));
        return listing;
    }

    public static Message message(UUID messageId, User sender, User receiver, Listing listing) {
        Message message = new Message();
        message.setMessageId(messageId);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setListing(listing);
        message.setContent("Is this still available?");
        message.setSentAt(LocalDateTime.of(2026, 1, 3, 10, 0));
        return message;
    }

    public static Favorite favorite(UUID favoriteId, User user, Listing listing) {
        Favorite favorite = new Favorite();
        favorite.setFavoriteId(favoriteId);
        favorite.setUser(user);
        favorite.setListing(listing);
        favorite.setCreatedAt(LocalDateTime.of(2026, 1, 4, 10, 0));
        return favorite;
    }

    public static Report report(UUID reportId, User reporter, User reportedUser, Listing listing) {
        Report report = new Report();
        report.setReportId(reportId);
        report.setReporter(reporter);
        report.setReportedUser(reportedUser);
        report.setListing(listing);
        report.setReason("Spam");
        report.setStatus("PENDING");
        report.setCreatedAt(LocalDateTime.of(2026, 1, 5, 10, 0));
        return report;
    }
}
