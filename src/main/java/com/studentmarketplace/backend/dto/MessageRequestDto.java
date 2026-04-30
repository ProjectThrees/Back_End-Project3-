package com.studentmarketplace.backend.dto;

import java.util.UUID;

public record MessageRequestDto(
        UUID senderId,
        UUID receiverId,
        UUID listingId,
        String content
) {
}
