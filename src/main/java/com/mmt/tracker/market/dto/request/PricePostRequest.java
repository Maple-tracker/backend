package com.mmt.tracker.market.dto.request;

import java.time.LocalDate;

public record PricePostRequest(
        String itemName,
        String statType,
        Short starForce,
        Short statPercent,
        Long amount,
        LocalDate date) {}
