package com.mmt.tracker.market.controller.dto.response;

import java.util.List;

public record ItemOptionsGetResponse(
    List<AvailableItemOption> availableItemOptions
) {
}
