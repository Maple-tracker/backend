package com.mmt.tracker.maple.controller.dto.response;

import com.mmt.tracker.maple.api.dto.response.BasicInfoResponse;
import com.mmt.tracker.maple.api.dto.response.EquippedItem;

import java.util.List;

public record CharacterInfoResponse(
        BasicInfoResponse basicInfo,
        List<EquippedItem> equippedItems
) {
}
