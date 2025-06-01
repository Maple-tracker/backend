package com.mmt.tracker.market.controller.dto.request;

import java.time.LocalDate;

public record HistoryPostRequest(
        String itemName,
        Short starForce,
        LocalDate date,
        Long amount,
        String statType,
        String potentialGrade,
        Short statPercent,
        Boolean potentialItal,
        String additionalPotentialGrade,
        Short additionalLines,
        Short additionalPercentLines,
        Short cuttableCount,
        Boolean starforceScrollFlag,
        Boolean enchantedFlag
) {
} 
