package com.mmt.tracker.market.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class PotentialOption {

    private static final String ITAL = "이탈";
    private static final String NOT_ITAL = "정옵";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PotentialGrade grade;

    @Column(nullable = false)
    private Short statPercent;

    @Column(nullable = false)
    private Boolean potentialItal;

    public PotentialOption(PotentialGrade grade, Short statPercent, Boolean potentialItal) {
        this.grade = grade;
        this.statPercent = statPercent;
        this.potentialItal = potentialItal;
    }

    public String toInfo() {
        return grade.getValue() + " " + statPercent + "% " + toItalInfo();
    }

    private String toItalInfo() {
        if (potentialItal) {
            return ITAL;
        }
        return NOT_ITAL;
    }
}
