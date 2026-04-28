package com.studentmarketplace.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageResponseDto(
        UUID messageId,
        UUID senderId,
        UUID receiverId,
        UUID listingId,
        String content,
        LocalDateTime sentAt
) {
}
