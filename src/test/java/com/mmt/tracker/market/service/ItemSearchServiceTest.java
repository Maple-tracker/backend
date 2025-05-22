package com.mmt.tracker.market.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.mmt.tracker.advice.NotFoundException;
import com.mmt.tracker.market.controller.dto.request.CompleteItemNameGetRequest;
import com.mmt.tracker.market.controller.dto.response.CompleteItemNameResponse;
import com.mmt.tracker.market.controller.dto.response.ItemOptionCombination;
import com.mmt.tracker.market.controller.dto.response.ItemOptionsGetResponse;
import com.mmt.tracker.market.domain.AdditionalPotentialOption;
import com.mmt.tracker.market.domain.ItemName;
import com.mmt.tracker.market.domain.ItemOption;
import com.mmt.tracker.market.domain.ItemSlot;
import com.mmt.tracker.market.domain.PotentialGrade;
import com.mmt.tracker.market.domain.PotentialOption;
import com.mmt.tracker.market.domain.StatType;
import com.mmt.tracker.market.repository.AdditionalPotentialOptionRepository;
import com.mmt.tracker.market.repository.ItemOptionRepository;
import com.mmt.tracker.market.repository.PotentialOptionRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ItemSearchServiceTest {

    @Autowired
    private ItemSearchService itemSearchService;

    @Autowired
    private ItemOptionRepository itemOptionRepository;

    @Autowired
    private PotentialOptionRepository potentialOptionRepository;

    @Autowired
    private AdditionalPotentialOptionRepository additionalPotentialOptionRepository;

    @BeforeEach
    void setUp() {
        itemOptionRepository.deleteAll();
        potentialOptionRepository.deleteAll();
        additionalPotentialOptionRepository.deleteAll();
    }

    @DisplayName("완성된 단어로 이루어진 아이템명 입력")
    @Test
    void getCompleteItemNames_ShouldReturnRightResults_WhenInputCompleteWord() {
        // Given
        String userInput = "샤이니 레드 워리어 마이스터 심볼";
        CompleteItemNameGetRequest request = new CompleteItemNameGetRequest(userInput);

        // When
        CompleteItemNameResponse response = itemSearchService.getCompleteItemNames(request);

        // Then
        assertThat(response.names()).contains(userInput);
    }

    @DisplayName("일부 단어로 이루어진 아이템명 입력")
    @Test
    void getCompleteItemNames_ShouldReturnRightResults_WhenInputSomeCompleteWord() {
        // Given
        String userInput = "샤이니 레드 워리어";
        CompleteItemNameGetRequest request = new CompleteItemNameGetRequest(userInput);

        // When
        CompleteItemNameResponse response = itemSearchService.getCompleteItemNames(request);

        // Then
        assertThat(response.names()).contains("샤이니 레드 워리어 마이스터 심볼");
    }

    @DisplayName("초성으로만 이루어진 아이템명 입력")
    @Test
    void getCompleteItemNames_ShouldReturnRightResults_WhenInputInitialWord() {
        // Given
        String userInput = "ㅅㅇㄴ ㄹㄷ ㅇㄹㅇ ㅁㅇㅅㅌ ㅅㅂ";
        CompleteItemNameGetRequest request = new CompleteItemNameGetRequest(userInput);

        // When
        CompleteItemNameResponse response = itemSearchService.getCompleteItemNames(request);

        // Then
        assertThat(response.names()).contains("샤이니 레드 워리어 마이스터 심볼");
    }

    @DisplayName("일부 초성과 공백으로 이루어진 아이템명 입력")
    @Test
    void getCompleteItemNames_ShouldReturnRightResults_WhenInputSomeInitialWord() {
        // Given
        String userInput = "ㅅㅇㄴ ㄹㄷ";
        CompleteItemNameGetRequest request = new CompleteItemNameGetRequest(userInput);

        // When
        CompleteItemNameResponse response = itemSearchService.getCompleteItemNames(request);

        // Then
        assertThat(response.names()).isEqualTo(List.of(
                "샤이니 레드 워리어 마이스터 심볼",
                "샤이니 레드 매지션 마이스터 심볼",
                "샤이니 레드 아처 마이스터 심볼",
                "샤이니 레드 시프 마이스터 심볼",
                "샤이니 레드 파이렛 마이스터 심볼"
        ));
    }

    @DisplayName("공백없는 일부 초성로 이루어진 아이템명 입력")
    @Test
    void getCompleteItemNames_ShouldReturnRightResults_WhenInputSomeInitialWordWithNoSpace() {
        // Given
        String userInput = "ㅅㅇㄴㄹㄷ";
        CompleteItemNameGetRequest request = new CompleteItemNameGetRequest(userInput);

        // When
        CompleteItemNameResponse response = itemSearchService.getCompleteItemNames(request);

        // Then
        assertThat(response.names()).isEqualTo(List.of(
                "샤이니 레드 워리어 마이스터 심볼",
                "샤이니 레드 매지션 마이스터 심볼",
                "샤이니 레드 아처 마이스터 심볼",
                "샤이니 레드 시프 마이스터 심볼",
                "샤이니 레드 파이렛 마이스터 심볼"
        ));
    }

    @DisplayName("옵션이 존재하는 아이템으로 조회")
    @Test
    void getItemOptions_ShouldReturnOptions_WhenItemExists() {
        // Given
        ItemName itemName = ItemName.EYE_ECC1;
        ItemSlot itemSlot = ItemSlot.EYE_ECC;

        PotentialOption potentialOption = new PotentialOption(PotentialGrade.LEGENDARY, (short) 30, false);
        potentialOption = potentialOptionRepository.save(potentialOption);

        AdditionalPotentialOption additionalPotentialOption = new AdditionalPotentialOption(
                PotentialGrade.LEGENDARY,
                (short) 2,
                (short) 2
        );
        additionalPotentialOption = additionalPotentialOptionRepository.save(additionalPotentialOption);

        ItemOption mockItemOption1 = new ItemOption(
                itemName,
                itemSlot,
                (short) 17,
                StatType.STR,
                potentialOption,
                additionalPotentialOption,
                false,
                true
        );
        ItemOption mockItemOption2 = new ItemOption(
                itemName,
                itemSlot,
                (short) 18,
                StatType.STR,
                potentialOption,
                additionalPotentialOption,
                false,
                true
        );
        ItemOption mockItemOption3 = new ItemOption(
                itemName,
                itemSlot,
                (short) 17,
                StatType.DEX,
                potentialOption,
                additionalPotentialOption,
                false,
                true
        );

        itemOptionRepository.saveAll(List.of(mockItemOption1, mockItemOption2, mockItemOption3));

        // When
        ItemOptionsGetResponse response = itemSearchService.getItemOptions(itemName.getValue());

        // Then
        // 1. combinations 검증
        assertThat(response.combinations()).hasSize(3);

        assertThat(response.combinations()
                .stream()
                .map(ItemOptionCombination::starForce)
                .toList())
                .containsExactlyInAnyOrder((short) 17, (short) 18, (short) 17);

        assertThat(response.combinations()
                .stream()
                .map(ItemOptionCombination::statType)
                .toList())
                .containsExactlyInAnyOrder("STR", "STR", "DEX");

        String expectedUpperPotential = "레전드리 30% 정옵";
        assertThat(response.combinations()
                .stream()
                .map(ItemOptionCombination::potentialOption)
                .toList())
                .containsOnly(expectedUpperPotential);

        String expectedLowerPotential = "레전드리 2 2";
        assertThat(response.combinations()
                .stream()
                .map(ItemOptionCombination::additionalPotentialOption)
                .toList())
                .containsOnly(expectedLowerPotential);

        assertThat(response.combinations()
                .stream()
                .map(ItemOptionCombination::enchantedFlag)
                .toList())
                .containsOnly(true);

        // 2. availableOptions 검증
        assertThat(response.availableOptions().starForce())
                .containsExactlyInAnyOrder("17성", "18성");

        assertThat(response.availableOptions().statType())
                .containsExactlyInAnyOrder("STR", "DEX");

        assertThat(response.availableOptions().potentialOption())
                .containsOnly(expectedUpperPotential);

        assertThat(response.availableOptions().additionalPotentialOption())
                .containsOnly(expectedLowerPotential);

        // 3. categorizedOptions 검증
        assertThat(response.categorizedOptions()).hasSize(2);
        assertThat(response.categorizedOptions().keySet()).containsExactlyInAnyOrder("17성", "18성");

        // 17성 카테고리 검증
        var starForce17 = response.categorizedOptions().get("17성");
        assertThat(starForce17).isNotNull();
        assertThat(starForce17.name()).isEqualTo("17성");
        assertThat(starForce17.subCategories()).hasSize(2);
        assertThat(starForce17.subCategories().keySet()).containsExactlyInAnyOrder("STR", "DEX");

        // 17성 > STR 카테고리 검증
        var starForce17Str = starForce17.subCategories().get("STR");
        assertThat(starForce17Str).isNotNull();
        assertThat(starForce17Str.name()).isEqualTo("STR");
        assertThat(starForce17Str.subCategories()).hasSize(1);
        assertThat(starForce17Str.subCategories().keySet()).containsExactly(expectedUpperPotential);

        // 17성 > STR > 레전드리 30% 정옵 카테고리 검증
        var starForce17StrPotential = starForce17Str.subCategories().get(expectedUpperPotential);
        assertThat(starForce17StrPotential).isNotNull();
        assertThat(starForce17StrPotential.name()).isEqualTo(expectedUpperPotential);
        assertThat(starForce17StrPotential.subCategories()).hasSize(1);
        assertThat(starForce17StrPotential.subCategories().keySet()).containsExactly(expectedLowerPotential);

        // 17성 > STR > 레전드리 30% 정옵 > 레전드리 2 2 카테고리 검증
        var starForce17StrPotentialAdditional = starForce17StrPotential.subCategories().get(expectedLowerPotential);
        assertThat(starForce17StrPotentialAdditional).isNotNull();
        assertThat(starForce17StrPotentialAdditional.name()).isEqualTo(expectedLowerPotential);
        assertThat(starForce17StrPotentialAdditional.subCategories()).isEmpty();
        assertThat(starForce17StrPotentialAdditional.optionIds()).isNotEmpty();
    }

    @DisplayName("옵션이 존재하지 않는 아이템으로 조회")
    @Test
    void getItemOptions_ShouldReturnEmptyOptions_WhenOptionNotExists() {
        // Given
        ItemName itemName = ItemName.BELT_ECC1;

        // When & Then
        assertThatThrownBy(() -> itemSearchService.getItemOptions(itemName.getValue()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("선택 가능한 옵션이 존재하지 않는 아이템");
    }
}
