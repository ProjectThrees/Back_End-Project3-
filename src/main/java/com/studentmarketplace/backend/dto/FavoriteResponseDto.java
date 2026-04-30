package com.studentmarketplace.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record FavoriteResponseDto(
        UUID favoriteId,
        UUID userId,
        UUID listingId,
        LocalDateTime createdAt
) {
}
