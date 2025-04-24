package com.mmt.tracker.market.controller.dto.response;

import java.time.LocalDate;

public record DailyPriceStats(
        LocalDate date,
        long price,
        long highPrice,
        long lowPrice,
        long volume
) {

}
