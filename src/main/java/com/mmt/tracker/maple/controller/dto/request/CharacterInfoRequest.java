package com.mmt.tracker.maple.controller.dto.request;

import java.time.LocalDate;

public record CharacterInfoRequest(
        String characterName,
        LocalDate date
) {
}
