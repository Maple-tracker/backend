package com.mmt.tracker.market.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "CHAR(40)", nullable = false)
    private String itemName;

    @Column(columnDefinition = "CHAR(10)", nullable = false)
    private String statType;

    @Column(columnDefinition = "SMALLINT", nullable = false)
    private Short starForce;

    @Column(columnDefinition = "SMALLINT", nullable = false)
    private Short statPercent;

    @Column(nullable = false)
    private Long amount;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDate date;

    public Price(
            String itemName,
            String statType,
            short starForce,
            short statPercent,
            long amount,
            LocalDate date) {
        this.itemName = itemName;
        this.statType = statType;
        this.starForce = starForce;
        this.statPercent = statPercent;
        this.amount = amount;
        this.date = date;
    }
}
