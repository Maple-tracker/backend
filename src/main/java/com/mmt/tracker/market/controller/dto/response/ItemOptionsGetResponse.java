package com.mmt.tracker.market.controller.dto.response;

import java.util.List;
import java.util.Map;

public record ItemOptionsGetResponse(
        List<ItemOptionCombination> combinations,
        AvailableOptions availableOptions,
        Map<String, CategoryOption> categorizedOptions
) {

}
