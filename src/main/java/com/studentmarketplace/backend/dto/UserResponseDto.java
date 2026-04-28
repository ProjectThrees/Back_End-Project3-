package com.studentmarketplace.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDto(
        UUID userId,
        String name,
        String email,
        String role,
        String status,
        LocalDateTime createdAt
) {
}
