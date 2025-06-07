package com.mmt.tracker.market.service;

import com.mmt.tracker.market.controller.dto.request.ItemOptionIdsPostRequest;
import com.mmt.tracker.market.controller.dto.request.HistoryPostRequest;
import com.mmt.tracker.market.controller.dto.response.DailyPriceStats;
import com.mmt.tracker.market.controller.dto.response.ItemBasicInfoDto;
import com.mmt.tracker.market.controller.dto.response.ItemOptionDto;
import com.mmt.tracker.market.controller.dto.response.ItemPriceHistoryResponse;
import com.mmt.tracker.market.controller.dto.response.PriceStats;
import com.mmt.tracker.market.domain.AdditionalPotentialOption;
import com.mmt.tracker.market.domain.ItemName;
import com.mmt.tracker.market.domain.ItemOption;
import com.mmt.tracker.market.domain.ItemTradeHistory;
import com.mmt.tracker.market.domain.PotentialOption;
import com.mmt.tracker.market.domain.StatType;
import com.mmt.tracker.market.exception.ItemOptionNotFound;
import com.mmt.tracker.market.repository.*;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemTradeService {

    private final ItemOptionRepository itemOptionRepository;
    private final ItemTradeHistoryRepository itemTradeHistoryRepository;
    private final PotentialOptionRepository potentialOptionRepository;
    private final AdditionalPotentialOptionRepository additionalPotentialOptionRepository;

   @Transactional
   public void postItemTradeHistory(HistoryPostRequest request) {
        ItemName itemName = ItemName.fromString(request.itemName());
        StatType statType = StatType.fromString(request.statType());

        PotentialOption potentialOption = PotentialOption.findOrCreate(
                request.potentialGrade(),
                request.statPercent(),
                request.potentialItal(),
                potentialOptionRepository
        );

        AdditionalPotentialOption additionalPotentialOption = AdditionalPotentialOption.findOrCreate(
                request.additionalPotentialGrade(),
                request.additionalLines(),
                request.additionalPercentLines(),
                additionalPotentialOptionRepository
        );

        ItemOption itemOption = ItemOption.findOrCreate(
                itemName,
                statType,
                request.starForce(),
                potentialOption,
                additionalPotentialOption,
                request.starforceScrollFlag(),
                request.enchantedFlag(),
                itemOptionRepository
        );

        ItemTradeHistory.createAndSave(
                itemOption,
                request.amount(),
                request.date(),
                request.cuttableCount(),
                itemTradeHistoryRepository
        );
   }

    @Transactional(readOnly = true)
    public ItemPriceHistoryResponse readItemPriceHistory(String itemName, ItemOptionIdsPostRequest request) {
        List<Long> optionIds = request.optionIds();
        List<ItemOption> targetOptions = optionIds.stream()
                .map(id -> itemOptionRepository.findById(id)
                        .orElseThrow(ItemOptionNotFound::new))
                .toList();

        if (!targetOptions.stream().allMatch(itemOption -> itemOption.getItemName().isNameEquals(itemName))) {
            throw new ItemOptionNotFound();
        }

        List<ItemTradeHistory> histories = targetOptions.stream()
                .flatMap(itemOption -> itemTradeHistoryRepository.findByItemOption(itemOption)
                        .stream())
                .toList();

        ItemBasicInfoDto basicInfoDto = extractItemBasicInfoDto(targetOptions.get(0));

        List<ItemOptionDto> itemOptionDtos = extractItemOptionDto(targetOptions);

        PriceStats priceStats = calculateStats(histories);
        List<DailyPriceStats> dailyPriceStats = extractPriceDataHistories(histories);

        return new ItemPriceHistoryResponse(basicInfoDto, itemOptionDtos, priceStats, dailyPriceStats, List.of());
    }

    private ItemBasicInfoDto extractItemBasicInfoDto(ItemOption targetOption) {
        return new ItemBasicInfoDto(
                targetOption.getId(),
                targetOption.getItemName().getValue(),
                URI.create(targetOption.getItemName().getUrl()),
                targetOption.getItemSlot().getValue(),
                0,
                true
        );
    }

    private List<ItemOptionDto> extractItemOptionDto(List<ItemOption> targetOptions) {
        return targetOptions.stream()
                .map(targetOption -> new ItemOptionDto(
                        targetOption.getId(),
                        targetOption.getStarForce() + "성",
                        targetOption.getPotentialOption().toInfo(),
                        targetOption.getAdditionalPotentialOption().toInfo(),
                        targetOption.getStatType().getValue(),
                        targetOption.getEnchantedFlag()
                ))
                .toList();
    }

    private PriceStats calculateStats(List<ItemTradeHistory> histories) {

        if (histories == null || histories.isEmpty()) {
            throw new IllegalArgumentException("거래 내역이 비어있습니다.");
        }

        // 가장 최근 가격
        long currentPrice = histories.stream().max(Comparator.comparing(ItemTradeHistory::getTimeStamp))
                .map(ItemTradeHistory::getAmount)
                .orElseThrow(() -> new IllegalArgumentException("거래 내역에 데이터가 없습니다."));

        // 평균 가격
        long averagePrice = (long) histories.stream().mapToDouble(ItemTradeHistory::getAmount).average()
                .orElse(0.0);

        // 최저 가격
        long lowestPrice = (long) histories.stream().mapToDouble(ItemTradeHistory::getAmount).min()
                .orElse(0.0);

        // 최고 가격
        long highestPrice = (long) histories.stream().mapToDouble(ItemTradeHistory::getAmount).max()
                .orElse(0.0);

        // 가장 최근 날짜 및 전날 데이터
        LocalDate latestDate = histories.stream()
                .map(trade -> trade.getTimeStamp().toLocalDate()).max(LocalDate::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("거래 내역에 날짜 데이터가 없습니다."));

        LocalDate previousDate = latestDate.minusDays(1);
        OptionalDouble currentDayPrice = histories.stream()
                .filter(trade -> trade.getTimeStamp().toLocalDate().equals(latestDate))
                .mapToDouble(ItemTradeHistory::getAmount).average();

        OptionalDouble previousDayPrice = histories.stream()
                .filter(trade -> trade.getTimeStamp().toLocalDate().equals(previousDate))
                .mapToDouble(ItemTradeHistory::getAmount).average();

        long priceChange = (long) (currentDayPrice.orElse(0.0) - previousDayPrice.orElse(0.0));
        double priceChangePercentage = previousDayPrice.isPresent() && previousDayPrice.getAsDouble() != 0.0 ?
                (priceChange / previousDayPrice.getAsDouble()) * 100 : 0.0;

        // 가장 최신 업데이트 시간
        LocalDateTime lastUpdated = histories.stream().max(Comparator.comparing(ItemTradeHistory::getTimeStamp))
                .map(ItemTradeHistory::getTimeStamp)
                .orElseThrow(() -> new IllegalArgumentException("거래 내역에 타임스탬프 데이터가 없습니다."));

        return new PriceStats(
                currentPrice,
                averagePrice,
                lowestPrice,
                highestPrice,
                priceChange,
                priceChangePercentage,
                lastUpdated
        );
    }

    private List<DailyPriceStats> extractPriceDataHistories(List<ItemTradeHistory> histories) {
        Map<LocalDate, List<ItemTradeHistory>> historyGroupedByDate = histories.stream()
                .collect(Collectors.groupingBy(history -> history.getTimeStamp().toLocalDate()));

        return historyGroupedByDate.entrySet()
                .stream()
                .map(entry -> {
                    PriceStats dailyStats = calculateStats(entry.getValue());
                    return new DailyPriceStats(
                            entry.getKey(),
                            dailyStats.averagePrice(),
                            dailyStats.highestPrice(),
                            dailyStats.lowestPrice(),
                            entry.getValue().size()
                    );
                })
                .toList();
    }
}
