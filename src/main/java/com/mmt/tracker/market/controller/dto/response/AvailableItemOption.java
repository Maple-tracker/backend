package com.mmt.tracker.market.controller.dto.response;

public record AvailableItemOption(
    Long id,
    Short starForce,
    String potentialOption,
    String additionalPotentialOption,
    Boolean enchantedFlag
) {
    
}
