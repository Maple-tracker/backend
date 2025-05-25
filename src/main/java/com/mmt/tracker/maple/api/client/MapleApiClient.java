package com.mmt.tracker.maple.api.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.mmt.tracker.advice.BadRequestException;
import com.mmt.tracker.advice.InternalServerException;
import com.mmt.tracker.advice.TrackerGlobalException;
import com.mmt.tracker.config.MapleApiClientConfiguration;
import com.mmt.tracker.maple.api.dto.response.BasicInfoResponse;
import com.mmt.tracker.maple.api.dto.response.EquippedItem;
import com.mmt.tracker.maple.api.dto.response.OcidResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MapleApiClient {

    private static final String NXOPEN_API_KEY_HEADER = "x-nxopen-api-key";
    private final ObjectMapper objectMapper;

    @Value("${MAPLE_API_KEY}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final MapleApiClientConfiguration apiConfig;

    public OcidResponse getCharacterOcid(String characterName) {
        String url =
                MapleApiUrl.BASE_URL.getUrl()
                        + MapleApiUrl.GET_CHARACTER_OCID_BY_NAME.getUrl().formatted(characterName);
        String response = executeApiRequest(url);
        try {
            return objectMapper.readValue(response, OcidResponse.class);
        } catch (JsonProcessingException e) {
            throw new InternalServerException("Failed to parse OCID response: " + e.getMessage());
        }
    }

    public BasicInfoResponse getCharacterBasicInfo(String ocid, LocalDate date) {
        String url = MapleApiUrl.BASE_URL.getUrl() + 
                    MapleApiUrl.GET_CHARACTER_BASIC_BY_OCID.getUrl().formatted(ocid);

        if (date != null) {
            url += "&date=" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        String response = executeApiRequest(url);
        try {
            return objectMapper.readValue(response, BasicInfoResponse.class);
        } catch (JsonProcessingException e) {
            throw new InternalServerException("Failed to parse basic info response: " + e.getMessage());
        }
    }

    public List<EquippedItem> getCharacterEquipmentInfo(String ocid, LocalDate date) {
        String url = MapleApiUrl.BASE_URL.getUrl() + 
                    MapleApiUrl.GET_CHARACTER_ITEM_EQUIPMENT_BY_OCID.getUrl().formatted(ocid);

        if (date != null) {
            url += "&date=" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        String response = executeApiRequest(url);
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode itemEquipmentNode = rootNode.get("item_equipment");
            CollectionType listType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, EquippedItem.class);
            return objectMapper.readValue(itemEquipmentNode.toString(), listType);
        } catch (JsonProcessingException e) {
            throw new InternalServerException("Failed to parse equipment response: " + e.getMessage());
        }
    }

    private HttpEntity<String> buildHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(NXOPEN_API_KEY_HEADER, apiKey);
        return new HttpEntity<>(headers);
    }
    
    private String executeApiRequest(String url) {
        HttpEntity<String> entity = buildHttpEntity();

        try {
            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            // Handle non-2xx responses
            throw handleApiError(response, url);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle Spring's HTTP exceptions
            throw handleApiError(
                    ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString()),
                    url);
        } catch (ResourceAccessException e) {
            // Handle connection issues
            throw MapleApiErrorCode.SERVER_ERROR.createException();
        } catch (TrackerGlobalException e) {
            // Re-throw our custom exceptions
            throw e;
        } catch (Exception e) {
            // Handle unexpected errors
            throw MapleApiErrorCode.UNEXPECTED_ERROR.createException();
        }
    }

    private TrackerGlobalException handleApiError(ResponseEntity<String> response, String url) {
        int statusCode = response.getStatusCode().value();

        if (statusCode == 400) {
            return handleBadRequestError(response, url);
        }

        return MapleApiErrorCode.forStatusCode(statusCode).createException();
    }

    private TrackerGlobalException handleBadRequestError(ResponseEntity<String> response, String url) {
        try {
            JsonNode errorNode = objectMapper.readTree(response.getBody()).get("error");
            String errorCode = errorNode.get("name").asText();

            // Special case for OPENAPI00004 with character_name endpoint
            if ("OPENAPI00004".equals(errorCode) && url.contains("character_name")) {
                return new BadRequestException("유효하지 않은 캐릭터명");
            }

            return MapleApiErrorCode.findByCode(errorCode).createException();
        } catch (JsonProcessingException e) {
            return MapleApiErrorCode.PARSING_ERROR.createException();
        } catch (Exception e) {
            return MapleApiErrorCode.UNEXPECTED_ERROR.createException();
        }
    }
}
