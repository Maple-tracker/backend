package com.mmt.tracker.market.controller.dto.response;

import java.net.URI;

public record ItemBasicInfoDto(
        long id,
        String name,
        URI imageUrl,
        String category,
        int level,
        boolean tradable
) {

}
