package com.studentmarketplace.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReportResponseDto(
        UUID reportId,
        UUID reporterId,
        UUID reportedUserId,
        UUID listingId,
        String reason,
        String status,
        LocalDateTime createdAt
) {
}
