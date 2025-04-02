package com.mmt.tracker.market.service;

import com.mmt.tracker.advice.BadRequestException;
import com.mmt.tracker.market.controller.dto.request.ItemTradeGetRequest;
import com.mmt.tracker.market.controller.dto.request.ItemTradePostRequest;
import com.mmt.tracker.market.controller.dto.response.ItemTradePostResponse;
import com.mmt.tracker.market.controller.dto.response.ItemTradeResponse;
import com.mmt.tracker.market.domain.*;
import com.mmt.tracker.market.repository.AdditionalPotentialOptionRepository;
import com.mmt.tracker.market.repository.ItemOptionRepository;
import com.mmt.tracker.market.repository.ItemTradeHistoryRepository;
import com.mmt.tracker.market.repository.PotentialOptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

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
                        .collect(Collectors.toList())
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
    

    private PotentialOption findPotentialOption(String grade, Short statPercent, Boolean potentialItal) {
        PotentialOption potentialOption = potentialOptionRepository.findByGradeAndStatPercentAndPotentialItal(grade, statPercent, potentialItal);
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
