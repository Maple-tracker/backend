package com.mmt.tracker.market.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.mmt.tracker.market.controller.dto.request.ItemOptionIdsPostRequest;
import com.mmt.tracker.market.controller.dto.response.DailyPriceStats;
import com.mmt.tracker.market.controller.dto.response.ItemBasicInfoDto;
import com.mmt.tracker.market.controller.dto.response.ItemOptionDto;
import com.mmt.tracker.market.controller.dto.response.ItemPriceHistoryResponse;
import com.mmt.tracker.market.controller.dto.response.PriceStats;
import com.mmt.tracker.market.domain.AdditionalPotentialOption;
import com.mmt.tracker.market.domain.ItemName;
import com.mmt.tracker.market.domain.ItemOption;
import com.mmt.tracker.market.domain.ItemSlot;
import com.mmt.tracker.market.domain.ItemTradeHistory;
import com.mmt.tracker.market.domain.PotentialGrade;
import com.mmt.tracker.market.domain.PotentialOption;
import com.mmt.tracker.market.domain.StatType;
import com.mmt.tracker.market.exception.ItemOptionNotFound;
import com.mmt.tracker.market.repository.AdditionalPotentialOptionRepository;
import com.mmt.tracker.market.repository.ItemOptionRepository;
import com.mmt.tracker.market.repository.ItemTradeHistoryRepository;
import com.mmt.tracker.market.repository.PotentialOptionRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ItemTradeServiceTest {

    @Autowired
    private ItemTradeService itemTradeService;

    @Autowired
    private ItemOptionRepository itemOptionRepository;

    @Autowired
    private PotentialOptionRepository potentialOptionRepository;

    @Autowired
    private AdditionalPotentialOptionRepository additionalPotentialOptionRepository;

    @Autowired
    private ItemTradeHistoryRepository itemTradeHistoryRepository;

    @BeforeEach
    void setUp() {
        itemTradeHistoryRepository.deleteAll();
        itemOptionRepository.deleteAll();
        potentialOptionRepository.deleteAll();
        additionalPotentialOptionRepository.deleteAll();
    }

    @DisplayName("존재하지 않는 옵션 ID 로 조회 시 ItemOptionNotFound 발생")
    @Test
    void readItemPriceHistory_ShouldThrow_WhenOptionNotExists() {
        assertThatThrownBy(() -> itemTradeService.readItemPriceHistory(
                ItemName.PENDANT_ECC1.getValue(),
                new ItemOptionIdsPostRequest(List.of(999L))
        )).isInstanceOf(ItemOptionNotFound.class).hasMessage("존재하지 않는 아이템입니다.");
    }

    @DisplayName("itemName 불일치 시 ItemOptionNotFound 발생")
    @Test
    void readItemPriceHistory_ShouldThrow_WhenNameNotMatch() {
        // --- Given: 올바른 ItemOption 저장 ---
        PotentialOption po = potentialOptionRepository.save(new PotentialOption(PotentialGrade.EPIC, (short) 10, true));
        AdditionalPotentialOption apo = additionalPotentialOptionRepository.save(new AdditionalPotentialOption(
                PotentialGrade.UNIQUE,
                (short) 2,
                (short) 5
        ));
        ItemOption option = itemOptionRepository.save(new ItemOption(
                ItemName.PENDANT_ECC1,
                ItemSlot.PENDANT,
                (short) 5,
                StatType.STR,
                po,
                apo,
                false,
                true
        ));

        // --- When & Then ---
        assertThatThrownBy(() -> itemTradeService.readItemPriceHistory(
                "잘못된 이름", new ItemOptionIdsPostRequest(List.of(option.getId())))).isInstanceOf(
                ItemOptionNotFound.class).hasMessage("존재하지 않는 아이템입니다.");
    }

    @DisplayName("거래 내역이 없을 때 IllegalArgumentException 발생")
    @Test
    void readItemPriceHistory_ShouldThrow_WhenHistoriesEmpty() {
        // --- Given: 올바른 ItemOption 저장 (하지만 TradeHistory 는 저장하지 않음) ---
        PotentialOption po = potentialOptionRepository.save(new PotentialOption(PotentialGrade.EPIC, (short) 10, true));
        AdditionalPotentialOption apo = additionalPotentialOptionRepository.save(new AdditionalPotentialOption(
                PotentialGrade.UNIQUE,
                (short) 2,
                (short) 5
        ));
        ItemOption option = itemOptionRepository.save(new ItemOption(
                ItemName.PENDANT_ECC1,
                ItemSlot.PENDANT,
                (short) 5,
                StatType.STR,
                po,
                apo,
                false,
                true
        ));

        // --- When & Then ---
        assertThatThrownBy(() -> itemTradeService.readItemPriceHistory(
                ItemName.PENDANT_ECC1.getValue(),
                new ItemOptionIdsPostRequest(List.of(option.getId()))
        )).isInstanceOf(IllegalArgumentException.class).hasMessage("거래 내역이 비어있습니다.");
    }

    @DisplayName("거래 내역이 있을 때 올바른 ItemPriceHistoryResponse 반환")
    @Test
    void readItemPriceHistory_ShouldReturnCorrectResponse_WhenHistoriesExist() {
        // --- Given: ItemOption 및 TradeHistory 저장 ---
        PotentialOption po = potentialOptionRepository.save(new PotentialOption(PotentialGrade.EPIC, (short) 10, true));
        AdditionalPotentialOption apo = additionalPotentialOptionRepository.save(new AdditionalPotentialOption(
                PotentialGrade.UNIQUE,
                (short) 2,
                (short) 5
        ));
        ItemOption option = itemOptionRepository.save(new ItemOption(
                ItemName.PENDANT_ECC1,
                ItemSlot.PENDANT,
                (short) 5,
                StatType.STR,
                po,
                apo,
                false,
                true
        ));
        LocalDateTime t1 = LocalDateTime.of(2025, 4, 22, 10, 0);
        LocalDateTime t2 = LocalDateTime.of(2025, 4, 23, 11, 0);
        LocalDateTime t3 = LocalDateTime.of(2025, 4, 23, 12, 0);

        itemTradeHistoryRepository.saveAll(List.of(
                new ItemTradeHistory(option, 100L, t1, (short) 1),
                new ItemTradeHistory(option, 200L, t2, (short) 1),
                new ItemTradeHistory(option, 300L, t3, (short) 1)
        ));

        // --- When ---
        ItemPriceHistoryResponse response = itemTradeService.readItemPriceHistory(
                ItemName.PENDANT_ECC1.getValue(),
                new ItemOptionIdsPostRequest(List.of(option.getId()))
        );

        // --- Then: BasicInfoDto 검증 ---
        ItemBasicInfoDto basic = response.item();
        assertThat(basic.id()).isEqualTo(option.getId());
        assertThat(basic.name()).isEqualTo(ItemName.PENDANT_ECC1.getValue());
        assertThat(basic.category()).isEqualTo(ItemSlot.PENDANT.getValue());
        assertThat(basic.level()).isZero();
        assertThat(basic.tradable()).isTrue();

        // --- Then: ItemOptionDto 검증 ---
        ItemOptionDto optDto = response.itemOptions().get(0);
        assertThat(optDto.id()).isEqualTo(option.getId());
        assertThat(optDto.starForce()).isEqualTo(option.getStarForce() + "성");
        assertThat(optDto.potentialOption()).isEqualTo(po.toInfo());
        assertThat(optDto.additionalPotentialOption()).isEqualTo(apo.toInfo());
        assertThat(optDto.statType()).isEqualTo(option.getStatType().getValue());
        assertThat(optDto.hasNoDrag()).isEqualTo(option.getEnchantedFlag());

        // --- Then: PriceStats 검증 ---
        PriceStats stats = response.priceStats();
        assertThat(stats.currentPrice()).isEqualTo(300L);
        assertThat(stats.averagePrice()).isEqualTo((long) ((100 + 200 + 300) / 3.0));
        assertThat(stats.lowestPrice()).isEqualTo(100L);
        assertThat(stats.highestPrice()).isEqualTo(300L);

        long expectedChange = 150L;
        assertThat(stats.priceChange()).isEqualTo(expectedChange);
        double expectedPct = expectedChange / 100.0 * 100;
        assertThat(stats.priceChangePercentage()).isEqualTo(expectedPct);
        assertThat(stats.lastUpdated()).isEqualTo(t3);

        // --- Then: DailyPriceStats 검증 ---
        List<DailyPriceStats> daily = response.priceHistory();
        assertThat(daily).hasSize(2);

        DailyPriceStats d1 = daily.stream()
                .filter(d -> d.date().equals(t1.toLocalDate()))
                .findFirst()
                .orElseThrow();
        assertThat(d1.price()).isEqualTo(100L);
        assertThat(d1.highPrice()).isEqualTo(100L);
        assertThat(d1.lowPrice()).isEqualTo(100L);
        assertThat(d1.volume()).isEqualTo(1L);

        DailyPriceStats d2 = daily.stream()
                .filter(d -> d.date().equals(t2.toLocalDate()))
                .findFirst()
                .orElseThrow();
        assertThat(d2.price()).isEqualTo((long) ((200 + 300) / 2.0));
        assertThat(d2.highPrice()).isEqualTo(300L);
        assertThat(d2.lowPrice()).isEqualTo(200L);
        assertThat(d2.volume()).isEqualTo(2L);
    }
}
