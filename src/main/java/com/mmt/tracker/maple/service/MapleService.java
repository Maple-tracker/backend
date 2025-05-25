package com.mmt.tracker.maple.service;

import com.mmt.tracker.maple.api.client.MapleApiClient;
import com.mmt.tracker.maple.controller.dto.request.CharacterInfoRequest;
import com.mmt.tracker.maple.api.dto.response.BasicInfoResponse;
import com.mmt.tracker.maple.controller.dto.response.CharacterInfoResponse;
import com.mmt.tracker.maple.api.dto.response.EquippedItem;
import com.mmt.tracker.maple.api.dto.response.OcidResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapleService {

    private final MapleApiClient mapleApiClient;

    public CharacterInfoResponse getCharacterInfo(CharacterInfoRequest request) throws Exception{
        OcidResponse ocidData = mapleApiClient.getCharacterOcid(request.characterName());

        BasicInfoResponse basicInfo = mapleApiClient.getCharacterBasicInfo(ocidData.ocid(), request.date());

        List<EquippedItem> equippedItems = mapleApiClient.getCharacterEquipmentInfo(ocidData.ocid(), request.date());

        return new CharacterInfoResponse(basicInfo, equippedItems);
    }
}
