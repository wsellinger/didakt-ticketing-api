package com.didakt.ticketing.didaktticketingapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EventRequest(
        @NotBlank String name,
        String description,
        @NotBlank String venue,
        @NotNull LocalDateTime eventDate
) {
}
