package com.mmt.tracker.market.domain;

import com.mmt.tracker.advice.BadRequestException;

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

    public static ItemSlot fromItemName(ItemName itemName) {
        String enumName = itemName.name();

        if (enumName.startsWith("EYE_ECC")) {
            return ItemSlot.EYE_ECC;
        } else if (enumName.startsWith("FACE_ECC")) {
            return ItemSlot.FACE_ECC;
        } else if (enumName.startsWith("PENDANT")) {
            return ItemSlot.PENDANT;
        } else if (enumName.startsWith("BELT")) {
            return ItemSlot.BELT;
        } else if (enumName.startsWith("RING")) {
            return ItemSlot.RING;
        } else if (enumName.startsWith("EARRING")) {
            return ItemSlot.EARRINGS;
        } else {
            throw new BadRequestException("존재하지 않는 슬롯: " + itemName.getValue());
        }
    }
}
