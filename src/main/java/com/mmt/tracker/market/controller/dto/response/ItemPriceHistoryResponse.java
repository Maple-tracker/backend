package com.mmt.tracker.market.controller.dto.response;

import java.util.List;

public record ItemPriceHistoryResponse(
        ItemBasicInfoDto item,
        List<ItemOptionDto> itemOptions,
        PriceStats priceStats,
        List<DailyPriceStats> priceHistory,
        List<ItemOptionDto> relatedOptions
) {

}
