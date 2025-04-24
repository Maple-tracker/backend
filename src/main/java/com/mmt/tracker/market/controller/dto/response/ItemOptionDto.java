package com.mmt.tracker.market.controller.dto.response;

public record ItemOptionDto(
        long id,
        String starForce,
        String potentialOption,
        String additionalPotentialOption,
        String statType,
        boolean hasNoDrag
) {

}
