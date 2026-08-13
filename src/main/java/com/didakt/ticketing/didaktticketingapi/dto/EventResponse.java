package com.didakt.ticketing.didaktticketingapi.dto;

import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        String name,
        String description,
        String venue,
        LocalDateTime eventDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
