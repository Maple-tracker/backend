package com.mmt.tracker.market.controller.dto.request;

import java.util.List;

public record ItemOptionIdsPostRequest(
        List<Long> optionIds
) {

}
