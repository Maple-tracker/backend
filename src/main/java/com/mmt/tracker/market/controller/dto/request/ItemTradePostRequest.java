package com.mmt.tracker.market.controller.dto.request;

import java.time.LocalDateTime;

public record ItemTradePostRequest(
        String itemName,
        String itemSlot,
        Short starForce,
        String statType,
        PotentialOptionRequest potentialOption,
        AdditionalPotentialOptionRequest additionalPotentialOption,
        Boolean starforceScrollFlag,
        Boolean enchantedFlag,
        Long amount,
        LocalDateTime timeStamp,
        Short cuttableCount
) {
    public record PotentialOptionRequest(
            String grade,
            Short statPercent,
            Boolean potentialItal
    ) {}

    public record AdditionalPotentialOptionRequest(
            String grade,
            Short lines,
            Short percentLines
    ) {}
} 
