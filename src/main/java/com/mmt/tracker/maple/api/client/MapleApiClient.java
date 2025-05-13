package com.mmt.tracker.maple.api.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmt.tracker.advice.BadRequestException;
import com.mmt.tracker.advice.InternalServerException;
import com.mmt.tracker.config.MapleApiClientConfiguration;

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

@Component
@RequiredArgsConstructor
public class MapleApiClient {

    private static final String NXOPEN_API_KEY_HEADER = "x-nxopen-api-key";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${MAPLE_API_KEY}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final MapleApiClientConfiguration apiConfig;

    public String getCharacterOcid(String characterName) {
        String url =
                MapleApiUrl.BASE_URL.getUrl()
                        + MapleApiUrl.GET_CHARACTER_OCID_BY_NAME.getUrl().formatted(characterName);
        return executeApiRequest(url);
    }

    public String getCharacterBasicInfo(String ocid, LocalDate date) {
        String url = MapleApiUrl.BASE_URL.getUrl() + 
                    MapleApiUrl.GET_CHARACTER_BASIC_BY_OCID.getUrl().formatted(ocid);
        
        if (date != null) {
            url += "&date=" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        
        return executeApiRequest(url);
    }

    public String getCharacterEquipmentInfo(String ocid, LocalDate date) {
        String url = MapleApiUrl.BASE_URL.getUrl() + 
                    MapleApiUrl.GET_CHARACTER_ITEM_EQUIPMENT_BY_OCID.getUrl().formatted(ocid);
        
        if (date != null) {
            url += "&date=" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        
        return executeApiRequest(url);
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

            handleErrorResponse(response, url);
            return null;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            handleErrorResponse(
                    ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString()),
                    url);
            return null;
        } catch (ResourceAccessException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private void handleErrorResponse(ResponseEntity<String> response, String url) {
        int statusCode = response.getStatusCode().value();
        String errorMessage = "API 요청 실패";

        switch (statusCode) {
            case 400:
                try {
                    JsonNode errorNode = objectMapper.readTree(response.getBody()).get("error");
                    String errorCode = errorNode.get("name").asText();

                    switch (errorCode) {
                        case "OPENAPI00003":
                            throw new BadRequestException("유효하지 않은 OCID");
                        case "OPENAPI00004":
                            if (url.contains("item-equipment")) {
                                throw new BadRequestException("조회 불가능한 날짜");
                            }
                            throw new BadRequestException("존재하지 않는 캐릭터명");
                        default:
                            throw new BadRequestException("잘못된 요청입니다");
                    }
                } catch (BadRequestException e) {
                    throw e;
                } catch (JsonProcessingException e) {
                    throw new InternalServerException("응답을 파싱하는 중 오류 발생");
                } catch (Exception e) {
                    throw new InternalServerException("예상하지 못한 오류 발생");
                }

            case 403:
                throw new InternalServerException("유효하지 않은 API 키");
            case 429:
                throw new InternalServerException("API 호출 한도를 초과");
            case 500:
                throw new InternalServerException("메이플스토리 API 서버 오류 발생");
            default:
                throw new InternalServerException(errorMessage);
        }
    }
}
