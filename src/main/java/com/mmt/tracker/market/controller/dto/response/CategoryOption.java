package com.mmt.tracker.market.controller.dto.response;

import java.util.List;
import java.util.Map;

public record CategoryOption(
        String name,
        Map<String, CategoryOption> subCategories,
        List<Long> optionIds
) {

}
