package com.mmt.tracker.maple.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CharacterInfoResponse(
        BasicInfoResponse basicInfo,
        @JsonProperty("item_equipment") List<EquippedItem> equippedItems
) {
}
