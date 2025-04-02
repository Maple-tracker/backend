package com.mmt.tracker.market.domain;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import com.mmt.tracker.advice.BadRequestException;

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
    private LocalDateTime timeStamp;

    @Column(columnDefinition = "SMALLINT", nullable = false)
    private Short cuttableCount;

    public ItemTradeHistory(
            ItemOption itemOption, Long amount, LocalDateTime timeStamp, Short cuttableCount) {
        validateAmount(amount);
        validateCuttableCount(cuttableCount);
        
        this.itemOption = itemOption;
        this.amount = amount;
        this.timeStamp = timeStamp;
        this.cuttableCount = cuttableCount;
    }
    
    private void validateAmount(Long amount) {
        if (amount <= 0) {
            throw new BadRequestException("금액은 0보다 커야합니다.");
        }
    }
    
    private void validateCuttableCount(Short cuttableCount) {
        if (cuttableCount < 0) {
            throw new BadRequestException("가위 사용 가능 횟수는 0 이상이어야 합니다.");
        }
    }
}
