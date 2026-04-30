package com.studentmarketplace.backend.dto;

import java.util.UUID;

public record ReportRequestDto(
        UUID reporterId,
        UUID reportedUserId,
        UUID listingId,
        String reason
) {
}
