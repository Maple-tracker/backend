package com.mmt.tracker.market.controller.dto.request;

public record ItemTradeGetRequest(
        String itemName,
        String itemSlot,
        Short starForce,
        String statType,
        PotentialOptionRequest potentialOption,
        AdditionalPotentialOptionRequest additionalPotentialOption,
        Boolean starforceScrollFlag,
        Boolean enchantedFlag
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
