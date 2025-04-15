package com.mmt.tracker.market.domain;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ItemName itemName;

    @Column
    private String itemSlot;

    @Column
    private Short starForce;

    @Column
    private String statType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "potential_option_id")
    private PotentialOption potentialOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "additional_potential_option_id")
    private AdditionalPotentialOption additionalPotentialOption;

    @Column(nullable = false)
    private Boolean starforceScrollFlag;

    @Column(columnDefinition = "BOOLEAN", nullable = false)
    private Boolean enchantedFlag;
}
