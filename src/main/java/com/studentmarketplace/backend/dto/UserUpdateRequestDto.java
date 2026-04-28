package com.studentmarketplace.backend.dto;

public record UserUpdateRequestDto(
        String name,
        String email,
        String role,
        String status
) {
}
