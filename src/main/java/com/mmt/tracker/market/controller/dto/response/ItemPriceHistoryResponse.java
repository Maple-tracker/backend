package com.mmt.tracker.market.controller.dto.response;

import java.util.List;

public record ItemPriceHistoryResponse(
        ItemBasicInfoDto item,
        ItemOptionDto itemOption,
        PriceStats priceStats,
        List<DailyPriceStats> priceHistory,
        List<ItemOptionDto> relatedOptions
) {

}
