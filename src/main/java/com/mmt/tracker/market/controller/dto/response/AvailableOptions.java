package com.mmt.tracker.market.controller.dto.response;

import java.util.List;

public record AvailableOptions(
    List<String> starForce,
    List<String> upperPotential,
    List<String> lowerPotentialGrade,
    List<String> statType,
    boolean hasNoDrag
) {} 