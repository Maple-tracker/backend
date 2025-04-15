package com.mmt.tracker.market.service;

import com.mmt.tracker.advice.NotFoundException;
import com.mmt.tracker.market.controller.dto.request.CompleteItemNameGetRequest;
import com.mmt.tracker.market.controller.dto.response.AvailableOptions;
import com.mmt.tracker.market.controller.dto.response.CompleteItemNameResponse;
import com.mmt.tracker.market.controller.dto.response.ItemOptionCombination;
import com.mmt.tracker.market.controller.dto.response.ItemOptionsGetResponse;
import com.mmt.tracker.market.domain.ItemName;
import com.mmt.tracker.market.domain.ItemOption;
import com.mmt.tracker.market.repository.ItemOptionRepository;
import com.mmt.tracker.market.util.HangulUtil;

import java.util.List;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemSearchService {

    // 완성된 한글 글자 확인을 위한 정규표현식
    private static final String COMPLETE_SYLLABLE_REGEX = "^[가-힣]+$";

    private final ItemOptionRepository itemOptionRepository;

    public CompleteItemNameResponse getCompleteItemNames(CompleteItemNameGetRequest request) {
        String userInput = request.input();
        String trimmedUserInput = HangulUtil.removeSpaces(userInput);

        if (trimmedUserInput.matches(COMPLETE_SYLLABLE_REGEX)) {
            return new CompleteItemNameResponse(getItemNamesFromPiece(trimmedUserInput));
        }

        return new CompleteItemNameResponse(getItemNamesFromInitial(HangulUtil.getInitial(trimmedUserInput)));
    }

    private List<String> getItemNamesFromPiece(String pieces) {
        return Stream.of(ItemName.values())
                .map(ItemName::getValue)
                .filter(value -> HangulUtil.removeSpaces(value).contains(pieces))
                .toList();
    }

    private List<String> getItemNamesFromInitial(String initials) {
        return Stream.of(ItemName.values())
                .map(ItemName::getValue)
                .filter(value -> HangulUtil.getInitial(HangulUtil.removeSpaces(value)).contains(initials))
                .toList();
    }

    @Transactional(readOnly = true)
    public ItemOptionsGetResponse getItemOptions(String itemName) {
        List<ItemOption> itemOptions = itemOptionRepository.findByItemName(ItemName.fromString(itemName));
        if (itemOptions.isEmpty()) {
            throw new NotFoundException("존재하지 않는 아이템명입니다.");
        }

        List<ItemOptionCombination> combinations = itemOptions.stream()
                .map(itemOption -> new ItemOptionCombination(
                        itemOption.getId(),
                        itemOption.getStarForce(),
                        itemOption.getPotentialOption().getGrade().getValue() + " " + itemOption.getPotentialOption().getStatPercent() + "% " + (itemOption.getPotentialOption().getPotentialItal()?"이탈":"정옵"),
                        itemOption.getAdditionalPotentialOption().getGrade().getValue() + " " + itemOption.getAdditionalPotentialOption().getLines() + " " + itemOption.getAdditionalPotentialOption().getPercentLines(),
                        itemOption.getStatType().getValue(),
                        !itemOption.getEnchantedFlag()
                ))
                .toList();
        AvailableOptions availableOptions = new AvailableOptions(
                combinations.stream().map(c -> c.starForce() + "성").distinct().toList(),
                combinations.stream().map(ItemOptionCombination::upperPotential).distinct().toList(),
                combinations.stream().map(ItemOptionCombination::lowerPotentialGrade).distinct().toList(),
                combinations.stream().map(ItemOptionCombination::statType).distinct().toList(),
                true
        );

        return new ItemOptionsGetResponse(combinations, availableOptions);
    }
}
