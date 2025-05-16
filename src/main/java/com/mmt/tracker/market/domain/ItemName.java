package com.mmt.tracker.market.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mmt.tracker.advice.BadRequestException;

public enum ItemName {
    // 눈장식
    EYE_ECC1("샤이니 레드 워리어 마이스터 심볼", "https://github.com/Maple-tracker/asset/blob/main/face_acc/twilightmark.png?raw=true"),
    EYE_ECC2("샤이니 레드 매지션 마이스터 심볼", "https://github.com/Maple-tracker/asset/blob/main/face_acc/twilightmark.png?raw=true"),
    EYE_ECC3("샤이니 레드 아처 마이스터 심볼", "https://github.com/Maple-tracker/asset/blob/main/face_acc/twilightmark.png?raw=true"),
    EYE_ECC4("샤이니 레드 시프 마이스터 심볼", "https://github.com/Maple-tracker/asset/blob/main/face_acc/twilightmark.png?raw=true"),
    EYE_ECC5("샤이니 레드 파이렛 마이스터 심볼", "https://github.com/Maple-tracker/asset/blob/main/face_acc/twilightmark.png?raw=true"),
    EYE_ECC6("트와일라이트 마크", "https://github.com/Maple-tracker/asset/blob/main/face_acc/twilightmark.png?raw=true"),
    EYE_ECC7("루즈 컨트롤 머신 마크", "https://github.com/Maple-tracker/asset/blob/main/face_acc/loosecontrolmachinemark.png?raw=true"),

    // 얼굴장식
    FACE_ECC1("블랙빈 마크", "https://github.com/Maple-tracker/asset/blob/main/eye_acc/papulatusmark.png?raw=true"),
    FACE_ECC2("파풀라투스 마크", "https://github.com/Maple-tracker/asset/blob/main/eye_acc/papulatusmark.png?raw=true"),
    FACE_ECC3("마력이 깃든 안대", "https://raw.githubusercontent.com/Maple-tracker/asset/refs/heads/main/eye_acc/magiceyepatch.webp"),

    // 펜던트
    PENDANT_ECC1("데이브레이크 펜던트", "https://raw.githubusercontent.com/Maple-tracker/asset/refs/heads/main/pendant/daybreakpendant.webp"),
    PENDANT_ECC2("도미네이터 펜던트", "https://github.com/Maple-tracker/asset/blob/main/pendant/dominatorpendant.png?raw=true"),
    PENDANT_ECC3("고통의 근원", "https://github.com/Maple-tracker/asset/blob/main/pendant/sourceofsuffering.png?raw=true"),
    PENDANT_ECC4("죽음의 맹세", "https://github.com/Maple-tracker/asset/blob/main/pendant/sourceofsuffering.png?raw=true"),

    // 벨트
    BELT_ECC1("골든 클로버 벨트", "https://github.com/Maple-tracker/asset/blob/main/belt/dreamybelt.png?raw=true"),
    BELT_ECC2("타일런트 히아데스 벨트", "https://github.com/Maple-tracker/asset/blob/main/belt/dreamybelt.png?raw=true"),
    BELT_ECC3("타일런트 헤르메스 벨트", "https://github.com/Maple-tracker/asset/blob/main/belt/dreamybelt.png?raw=true"),
    BELT_ECC4("타일런트 케이론 벨트", "https://github.com/Maple-tracker/asset/blob/main/belt/dreamybelt.png?raw=true"),
    BELT_ECC5("타일런트 리카온 벨트", "https://github.com/Maple-tracker/asset/blob/main/belt/dreamybelt.png?raw=true"),
    BELT_ECC6("타일런트 알테어 벨트", "https://github.com/Maple-tracker/asset/blob/main/belt/dreamybelt.png?raw=true"),
    BELT_ECC7("분노한 자쿰의 벨트", "https://github.com/Maple-tracker/asset/blob/main/belt/dreamybelt.png?raw=true"),
    BELT_ECC8("몽환의 벨트", "https://github.com/Maple-tracker/asset/blob/main/belt/dreamybelt.png?raw=true"),

    // 반지
    RING_ECC1("스칼렛 링", "https://github.com/Maple-tracker/asset/blob/main/ring/meisterring.png?raw=true"),
    RING_ECC2("마이스터링", "https://github.com/Maple-tracker/asset/blob/main/ring/meisterring.png?raw=true"),
    RING_ECC3("가디언 엔젤 링", "https://github.com/Maple-tracker/asset/blob/main/ring/guardianangelring.png?raw=true"),
    RING_ECC4("여명의 가디언 엔젤 링", "https://github.com/Maple-tracker/asset/blob/main/ring/guardianangelring.png?raw=true"),
    RING_ECC5("거대한 공포", "https://raw.githubusercontent.com/Maple-tracker/asset/refs/heads/main/ring/greatterror.webp"),
    RING_ECC6("근원의 속삭임", "https://raw.githubusercontent.com/Maple-tracker/asset/refs/heads/main/ring/greatterror.webp"),

    // 귀걸이
    EARRING_ECC1("데아 시두스 이어링", "https://github.com/Maple-tracker/asset/blob/main/earrings/meisterearrings.png?raw=true"),
    EARRING_ECC2("지옥의 불꽃", "https://github.com/Maple-tracker/asset/blob/main/earrings/meisterearrings.png?raw=true"),
    EARRING_ECC3("스칼렛 이어링", "https://github.com/Maple-tracker/asset/blob/main/earrings/meisterearrings.png?raw=true"),
    EARRING_ECC4("샤먼 이어링", "https://github.com/Maple-tracker/asset/blob/main/earrings/meisterearrings.png?raw=true"),
    EARRING_ECC5("마이스터 이어링", "https://github.com/Maple-tracker/asset/blob/main/earrings/meisterearrings.png?raw=true"),
    EARRING_ECC6("오션 글로우 이어링", "https://github.com/Maple-tracker/asset/blob/main/earrings/oceanglowearrings.png?raw=true"),
    EARRING_ECC7("에스텔라 이어링", "https://github.com/Maple-tracker/asset/blob/main/earrings/estellaearrings.png?raw=true"),
    EARRING_ECC8("커맨더 포스 이어링", "https://raw.githubusercontent.com/Maple-tracker/asset/refs/heads/main/earrings/commanderforceearrings.webp"),
    ;

    private final String value;
    private final String url;

    ItemName(String value, String url) {
        this.value = value;
        this.url = url;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public String getUrl() {
        return url;
    }

    @JsonCreator
    public static ItemName fromString(String text) {
        if (text == null) {
            throw new BadRequestException("Item name cannot be null");
        }

        String normalizedText = text.trim();

        for (ItemName item : ItemName.values()) {
            if (item.value.equals(normalizedText)) {
                return item;
            }
        }

        String enumStyleText = normalizedText.replace(" ", "_").toUpperCase();
        try {
            return ItemName.valueOf(enumStyleText);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Unknown item name: " + text);
        }
    }

    public boolean isNameEquals(String name) {
        return value.equals(name);
    }
}
