package com.mmt.tracker.market.controller.dto.response;

public record ItemOptionCombination(
    Long id,
    Short starForce,
    String upperPotential,
    String lowerPotentialGrade,
    String statType,
    Boolean hasNoDrag
) {} 