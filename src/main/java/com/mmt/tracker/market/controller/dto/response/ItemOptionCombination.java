package com.mmt.tracker.market.controller.dto.response;

public record ItemOptionCombination(
    Long id,
    Short starForce,
    String potentialOption,
    String additionalPotentialOption,
    String statType,
    Boolean enchantedFlag
) {}
