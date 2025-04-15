package com.mmt.tracker.market.domain;

public enum PotentialGrade {
    NONE("없음"),
    EPIC("에픽"),
    UNIQUE("유니크"),
    LEGENDARY("레전드리");

    private final String value;

    PotentialGrade(String value) {this.value = value;}

    public String getValue() {return value;}
}
