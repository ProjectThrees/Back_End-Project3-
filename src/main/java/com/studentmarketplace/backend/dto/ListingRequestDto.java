package com.studentmarketplace.backend.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ListingRequestDto(
        UUID userId,
        String title,
        String description,
        BigDecimal price,
        String category,
        String condition,
        String imageUrl,
        Boolean isSold
) {
}
