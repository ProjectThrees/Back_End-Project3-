package com.studentmarketplace.backend.dto;

public record UserCreateRequestDto(
        String name,
        String email,
        String role,
        String status
) {
}
