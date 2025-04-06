package com.mmt.tracker.market.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ItemTradeResponse(
        List<ItemTradeDetailResponse> tradeHistories
) {
    public record ItemTradeDetailResponse(
            Long amount,
            LocalDateTime timeStamp,
            Short cuttableCount
    ) {}
} 
