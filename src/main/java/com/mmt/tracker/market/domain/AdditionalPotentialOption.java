package com.mmt.tracker.market.domain;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class AdditionalPotentialOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "CHAR(10)", nullable = false)
    private String grade;

    @Column(columnDefinition = "SMALLINT", nullable = false)
    private Short lines;

    @Column(columnDefinition = "SMALLINT", nullable = false)
    private Short percentLines;
}
