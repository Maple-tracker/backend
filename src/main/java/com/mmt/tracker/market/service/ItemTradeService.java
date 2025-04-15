package com.mmt.tracker.market.service;

import com.mmt.tracker.advice.BadRequestException;
import com.mmt.tracker.advice.NotFoundException;
import com.mmt.tracker.market.controller.dto.request.ItemTradeGetRequest;
import com.mmt.tracker.market.controller.dto.request.ItemTradePostRequest;
import com.mmt.tracker.market.controller.dto.response.AvailableItemOption;
import com.mmt.tracker.market.controller.dto.response.ItemOptionsGetResponse;
import com.mmt.tracker.market.controller.dto.response.ItemTradePostResponse;
import com.mmt.tracker.market.controller.dto.response.ItemTradeResponse;
import com.mmt.tracker.market.domain.AdditionalPotentialOption;
import com.mmt.tracker.market.domain.ItemName;
import com.mmt.tracker.market.domain.ItemOption;
import com.mmt.tracker.market.domain.ItemTradeHistory;
import com.mmt.tracker.market.domain.PotentialOption;
import com.mmt.tracker.market.repository.AdditionalPotentialOptionRepository;
import com.mmt.tracker.market.repository.ItemOptionRepository;
import com.mmt.tracker.market.repository.ItemTradeHistoryRepository;
import com.mmt.tracker.market.repository.PotentialOptionRepository;
import java.util.List;
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

    @Transactional(readOnly = true)
    public ItemTradeResponse getItemTradeHistories(ItemTradeGetRequest request) {
        PotentialOption potentialOption = findPotentialOption(
                request.potentialOption().grade(),
                request.potentialOption().statPercent(),
                request.potentialOption().potentialItal()
        );

        AdditionalPotentialOption additionalPotentialOption = findAdditionalPotentialOption(
                request.additionalPotentialOption().grade(),
                request.additionalPotentialOption().lines(),
                request.additionalPotentialOption().percentLines()
        );

        ItemOption itemOption = findItemOption(
                request.itemName(),
                request.itemSlot(),
                request.starForce(),
                request.statType(),
                potentialOption,
                additionalPotentialOption,
                request.starforceScrollFlag(),
                request.enchantedFlag()
        );

        List<ItemTradeHistory> histories = itemTradeHistoryRepository.findByItemOption(itemOption);

        return new ItemTradeResponse(
                histories.stream()
                        .map(history -> new ItemTradeResponse.ItemTradeDetailResponse(
                                history.getAmount(),
                                history.getTimeStamp(),
                                history.getCuttableCount()
                        ))
                        .toList()
        );
    }

    @Transactional
    public ItemTradePostResponse postItemTradeHistory(ItemTradePostRequest request) {
        PotentialOption potentialOption = findPotentialOption(
                request.potentialOption().grade(),
                request.potentialOption().statPercent(),
                request.potentialOption().potentialItal()
        );

        AdditionalPotentialOption additionalPotentialOption = findAdditionalPotentialOption(
                request.additionalPotentialOption().grade(),
                request.additionalPotentialOption().lines(),
                request.additionalPotentialOption().percentLines()
        );

        ItemOption itemOption = findItemOption(
                request.itemName(),
                request.itemSlot(),
                request.starForce(),
                request.statType(),
                potentialOption,
                additionalPotentialOption,
                request.starforceScrollFlag(),
                request.enchantedFlag()
        );

        ItemTradeHistory itemTradeHistory = new ItemTradeHistory(
                itemOption,
                request.amount(),
                request.timeStamp(),
                request.cuttableCount()
        );

        ItemTradeHistory savedHistory = itemTradeHistoryRepository.save(itemTradeHistory);
        return new ItemTradePostResponse(savedHistory.getId());
    }

    @Transactional(readOnly = true)
    public ItemOptionsGetResponse getItemOptions(String itemName) {
        List<ItemOption> itemOptions = itemOptionRepository.findByItemName(ItemName.fromString(itemName));
        if (itemOptions.isEmpty()) {
            throw new NotFoundException("존재하지 않는 아이템명입니다.");
        }

        return new ItemOptionsGetResponse(itemOptions.stream()
                .map(itemOption -> new AvailableItemOption(
                        itemOption.getId(),
                        itemOption.getStarForce(),
                        itemOption.getStatType().getValue(),
                        itemOption.getPotentialOption().getGrade().getValue() + " " + itemOption.getPotentialOption()
                                .getStatPercent() + " " + itemOption.getPotentialOption().getPotentialItal(),
                        itemOption.getAdditionalPotentialOption().getGrade().getValue() + " "
                                + itemOption.getAdditionalPotentialOption().getLines() + " "
                                + itemOption.getAdditionalPotentialOption().getPercentLines(),
                        itemOption.getEnchantedFlag()
                ))
                .toList()
        );
    }

    private PotentialOption findPotentialOption(String grade, Short statPercent, Boolean potentialItal) {
        PotentialOption potentialOption = potentialOptionRepository.findByGradeAndStatPercentAndPotentialItal(
                grade,
                statPercent,
                potentialItal
        );
        if (potentialOption == null) {
            throw new BadRequestException("잠재능력 - 존재하지 않는 아이템 옵션");
        }
        return potentialOption;
    }

    private AdditionalPotentialOption findAdditionalPotentialOption(String grade, Short lines, Short percentLines) {
        AdditionalPotentialOption additionalPotentialOption =
                additionalPotentialOptionRepository.findByGradeAndLinesAndPercentLines(grade, lines, percentLines);
        if (additionalPotentialOption == null) {
            throw new BadRequestException("에디셔널 잠재능력 - 존재하지 않는 아이템 옵션");
        }
        return additionalPotentialOption;
    }

    private ItemOption findItemOption(
            String itemName,
            String itemSlot,
            Short starForce,
            String statType,
            PotentialOption potentialOption,
            AdditionalPotentialOption additionalPotentialOption,
            Boolean starforceScrollFlag,
            Boolean enchantedFlag
    ) {
        return itemOptionRepository.findByItemNameAndItemSlotAndStarForceAndStatTypeAndPotentialOptionAndAdditionalPotentialOptionAndStarforceScrollFlagAndEnchantedFlag(
                        itemName,
                        itemSlot,
                        starForce,
                        statType,
                        potentialOption,
                        additionalPotentialOption,
                        starforceScrollFlag,
                        enchantedFlag
                )
                .orElseThrow(() -> new BadRequestException("존재하지 않는 아이템 옵션"));
    }
} 
