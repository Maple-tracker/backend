package com.mmt.tracker.market.controller.dto.response;

import java.time.LocalDateTime;

public record PriceStats(
        long currentPrice,
        long averagePrice,
        long lowestPrice,
        long highestPrice,
        long priceChange,
        double priceChangePercentage,
        LocalDateTime lastUpdated
) {
}
