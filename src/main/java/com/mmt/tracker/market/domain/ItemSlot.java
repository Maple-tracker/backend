package com.mmt.tracker.market.domain;

public enum ItemSlot {
    EYE_ECC("눈장식"),
    FACE_ECC("얼굴장식"),
    PENDANT("펜던트"),
    BELT("벨트"),
    RING("반지"),
    EARRINGS("귀고리");

    private final String value;

    ItemSlot(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
