package com.mmt.tracker.market.controller.dto.response;

import java.util.List;

public record AvailableOptions(
    List<String> starForce,
    List<String> potentialOption,
    List<String> additionalPotentialOption,
    List<String> statType
) {}
