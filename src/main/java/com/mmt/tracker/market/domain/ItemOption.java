package com.mmt.tracker.market.domain;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "CHAR(40)", nullable = false)
    @NonNull
    private String itemName;

    @Column(columnDefinition = "CHAR(10)")
    @NonNull
    private String itemSlot;

    @Column(columnDefinition = "SMALLINT")
    @NonNull
    private Short starForce;

    @Column(columnDefinition = "CHAR(10)")
    @NonNull
    private String statType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "potential_option_id")
    @NonNull
    private PotentialOption potentialOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "additional_potential_option_id")
    private AdditionalPotentialOption additionalPotentialOption;

    @Column(columnDefinition = "BOOLEAN", nullable = false)
    @NonNull
    private Boolean starforceScrollFlag;

    @Column(columnDefinition = "BOOLEAN", nullable = false)
    @NonNull
    private Boolean enchantedFlag;

    public ItemOption(
            ItemName itemName,
            ItemSlot itemSlot,
            Short starForce,
            StatType statType,
            PotentialOption potentialOption,
            Boolean starforceScrollFlag,
            Boolean enchantedFlag) {
        this.itemName = itemName.getValue();
        this.itemSlot = itemSlot.getValue();
        this.starForce = starForce;
        this.statType = statType.getValue();
        this.potentialOption = potentialOption;
        this.starforceScrollFlag = starforceScrollFlag;
        this.enchantedFlag = enchantedFlag;
    }
}
