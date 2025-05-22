package com.mmt.tracker.market.service;

import com.mmt.tracker.advice.NotFoundException;
import com.mmt.tracker.market.controller.dto.request.CompleteItemNameGetRequest;
import com.mmt.tracker.market.controller.dto.response.AvailableOptions;
import com.mmt.tracker.market.controller.dto.response.CategoryOption;
import com.mmt.tracker.market.controller.dto.response.CompleteItemNameResponse;
import com.mmt.tracker.market.controller.dto.response.ItemOptionCombination;
import com.mmt.tracker.market.controller.dto.response.ItemOptionsGetResponse;
import com.mmt.tracker.market.domain.ItemName;
import com.mmt.tracker.market.domain.ItemOption;
import com.mmt.tracker.market.repository.ItemOptionRepository;
import com.mmt.tracker.market.util.HangulUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
                .filter(value -> HangulUtil.getInitial(HangulUtil.removeSpaces(value)).startsWith(initials))
                .toList();
    }

    @Transactional(readOnly = true)
    public ItemOptionsGetResponse getItemOptions(String itemName) {
        List<ItemOption> itemOptions = itemOptionRepository.findByItemName(ItemName.fromString(itemName));
        if (itemOptions.isEmpty()) {
            throw new NotFoundException("선택 가능한 옵션이 존재하지 않는 아이템");
        }

        Long notEnchantedItemOptionId = itemOptions.stream()
                .filter(itemOption -> !itemOption.getEnchantedFlag())
                .findFirst()
                .map(ItemOption::getId)
                .orElse(-1L);

        List<ItemOptionCombination> combinations = itemOptions.stream()
                .map(itemOption -> new ItemOptionCombination(
                        itemOption.getId(),
                        itemOption.getStarForce(),
                        itemOption.getPotentialOption().getGrade().getValue() + " " + itemOption.getPotentialOption()
                                .getStatPercent() + "% " + (
                                Boolean.TRUE.equals(itemOption.getPotentialOption().getPotentialItal()) ? "이탈" : "정옵"),
                        itemOption.getAdditionalPotentialOption().getGrade().getValue() + " "
                                + itemOption.getAdditionalPotentialOption().getLines() + " "
                                + itemOption.getAdditionalPotentialOption().getPercentLines(),
                        itemOption.getStatType().getValue(),
                        itemOption.getEnchantedFlag()
                ))
                .toList();
        AvailableOptions availableOptions = new AvailableOptions(
                combinations.stream()
                        .map(c -> c.starForce() + "성").distinct()
                        .toList(),
                combinations.stream()
                        .map(ItemOptionCombination::potentialOption).distinct()
                        .toList(),
                combinations.stream()
                        .map(ItemOptionCombination::additionalPotentialOption).distinct()
                        .toList(),
                combinations.stream()
                        .map(ItemOptionCombination::statType).distinct()
                        .toList()
        );

        // Create categorized options
        Map<String, CategoryOption> categorizedOptions = createCategorizedOptions(combinations);

        return new ItemOptionsGetResponse(combinations, availableOptions, categorizedOptions, notEnchantedItemOptionId);
    }

    private Map<String, CategoryOption> createCategorizedOptions(List<ItemOptionCombination> combinations) {
        Map<String, CategoryOption> starForceCategories = new HashMap<>();

        Map<Short, List<ItemOptionCombination>> byStarForce = combinations.stream()
                .collect(Collectors.groupingBy(ItemOptionCombination::starForce));

        byStarForce.forEach((starForce, starForceOptions) -> {
            Map<String, CategoryOption> statTypeCategories = new HashMap<>();

            Map<String, List<ItemOptionCombination>> byStatType = starForceOptions.stream()
                    .collect(Collectors.groupingBy(ItemOptionCombination::statType));

            byStatType.forEach((statType, statTypeOptions) -> {
                Map<String, CategoryOption> potentialCategories = new HashMap<>();

                Map<String, List<ItemOptionCombination>> byPotential = statTypeOptions.stream()
                        .collect(Collectors.groupingBy(ItemOptionCombination::potentialOption));

                byPotential.forEach((potential, potentialOptions) -> {
                    Map<String, CategoryOption> additionalCategories = new HashMap<>();

                    Map<String, List<ItemOptionCombination>> byAdditional = potentialOptions.stream()
                            .collect(Collectors.groupingBy(ItemOptionCombination::additionalPotentialOption));

                    byAdditional.forEach((additional, additionalOptions) -> {
                        Long optionId = additionalOptions.stream()
                                .findFirst().get().id();
                        CategoryOption additionalCategory = new CategoryOption(
                                additional,
                                Collections.emptyMap(),
                                optionId
                        );

                        additionalCategories.put(additional, additionalCategory);
                    });

                    CategoryOption potentialCategory = new CategoryOption(
                            potential,
                            additionalCategories,
                            -1L
                    );

                    potentialCategories.put(potential, potentialCategory);
                });

                CategoryOption statTypeCategory = new CategoryOption(
                        statType,
                        potentialCategories,
                        -1L
                );

                statTypeCategories.put(statType, statTypeCategory);
            });

            CategoryOption starForceCategory = new CategoryOption(
                    starForce + "성",
                    statTypeCategories,
                    -1L
            );

            starForceCategories.put(starForce + "성", starForceCategory);
        });

        return starForceCategories;
    }
}
