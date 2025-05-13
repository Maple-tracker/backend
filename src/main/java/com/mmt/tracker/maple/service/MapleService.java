package com.mmt.tracker.maple.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    public CharacterInfoResponse getCharacterInfo(CharacterInfoRequest request) throws Exception {
        String ocidResponse = mapleApiClient.getCharacterOcid(request.characterName());
        OcidResponse ocidData = objectMapper.readValue(ocidResponse, OcidResponse.class);

        String basicInfoResponse = mapleApiClient.getCharacterBasicInfo(ocidData.ocid(), request.date());
        BasicInfoResponse basicInfo = objectMapper.readValue(basicInfoResponse, BasicInfoResponse.class);

        String equipmentResponse = mapleApiClient.getCharacterEquipmentInfo(ocidData.ocid(), request.date());
        
        JsonNode rootNode = objectMapper.readTree(equipmentResponse);
        JsonNode itemEquipmentNode = rootNode.get("item_equipment");
        List<EquippedItem> equippedItems = objectMapper.readValue(
            itemEquipmentNode.toString(), 
            objectMapper.getTypeFactory().constructCollectionType(List.class, EquippedItem.class)
        );

        return new CharacterInfoResponse(basicInfo, equippedItems);
    }
}
