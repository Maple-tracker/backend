package com.mmt.tracker.market.domain;

import com.mmt.tracker.advice.BadRequestException;

import java.util.Arrays;

public enum PotentialGrade {
    NONE("없음"),
    EPIC("에픽"),
    UNIQUE("유니크"),
    LEGENDARY("레전드리");

    private final String value;

    PotentialGrade(String value) {this.value = value;}

    public String getValue() {return value;}

    public static PotentialGrade fromString(String grade) {
        return Arrays.stream(PotentialGrade.values())
                .filter(potentialGrade -> potentialGrade.name().equals(grade) || potentialGrade.getValue().equals(grade))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("존재하지 않는 잠재능력 등급:" + grade));
    }
}
