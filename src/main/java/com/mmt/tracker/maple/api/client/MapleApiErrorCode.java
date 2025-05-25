package com.mmt.tracker.maple.api.client;

import com.mmt.tracker.advice.BadRequestException;
import com.mmt.tracker.advice.InternalServerException;
import com.mmt.tracker.advice.TrackerGlobalException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum MapleApiErrorCode {
    // 400 Bad Request errors
    INVALID_OCID("OPENAPI00003", "유효하지 않은 OCID", BadRequestException::new),
    INVALID_DATE("OPENAPI00004", "유효하지 않은 날짜", BadRequestException::new),
    INVALID_API_KEY("OPENAPI00005", "유효하지 않은 API 키", BadRequestException::new),
    BAD_REQUEST_DEFAULT(null, "잘못된 요청입니다", BadRequestException::new),
    
    // Server errors
    RATE_LIMIT_EXCEEDED(null, "API 호출 한도를 초과", InternalServerException::new),
    SERVER_ERROR(null, "메이플스토리 API 서버 오류 발생", InternalServerException::new),
    PARSING_ERROR(null, "응답을 파싱하는 중 오류 발생", InternalServerException::new),
    UNEXPECTED_ERROR(null, "예상하지 못한 오류 발생", InternalServerException::new),
    DEFAULT_ERROR(null, "API 요청 실패", InternalServerException::new);

    @Getter
    private final String code;
    @Getter
    private final String message;
    private final Function<String, TrackerGlobalException> exceptionSupplier;
    
    private static final Map<String, MapleApiErrorCode> BY_CODE = 
            Arrays.stream(values())
                  .filter(e -> e.code != null)
                  .collect(Collectors.toMap(MapleApiErrorCode::getCode, Function.identity()));

    MapleApiErrorCode(String code, String message, Function<String, TrackerGlobalException> exceptionSupplier) {
        this.code = code;
        this.message = message;
        this.exceptionSupplier = exceptionSupplier;
    }

    public TrackerGlobalException createException() {
        return exceptionSupplier.apply(message);
    }

    public static MapleApiErrorCode findByCode(String code) {
        return BY_CODE.getOrDefault(code, BAD_REQUEST_DEFAULT);
    }

    public static MapleApiErrorCode forStatusCode(int statusCode) {
        return switch (statusCode) {
            case 403 -> INVALID_API_KEY;
            case 429 -> RATE_LIMIT_EXCEEDED;
            case 500 -> SERVER_ERROR;
            default -> DEFAULT_ERROR;
        };
    }
}