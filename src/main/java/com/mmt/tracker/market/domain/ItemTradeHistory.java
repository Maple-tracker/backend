package com.mmt.tracker.market.domain;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemTradeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_option_id")
    private ItemOption itemOption;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private LocalDate date;

    @Column(columnDefinition = "SMALLINT", nullable = false)
    private Short cuttableCount;

    public ItemTradeHistory(
            ItemOption itemOption, Long amount, LocalDate date, Short cuttableCount) {
        this.itemOption = itemOption;
        this.amount = amount;
        this.date = date;
        this.cuttableCount = cuttableCount;
    }
}
