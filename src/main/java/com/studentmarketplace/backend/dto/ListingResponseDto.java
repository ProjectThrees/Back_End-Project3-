package com.studentmarketplace.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ListingResponseDto(
        UUID listingId,
        UUID userId,
        String title,
        String description,
        BigDecimal price,
        String category,
        String condition,
        String imageUrl,
        Boolean isSold,
        LocalDateTime createdAt
) {
}
