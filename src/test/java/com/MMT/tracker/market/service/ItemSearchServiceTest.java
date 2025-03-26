package com.MMT.tracker.market.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.mmt.tracker.market.controller.dto.request.CompleteItemNameGetRequest;
import com.mmt.tracker.market.controller.dto.response.CompleteItemNameResponse;
import com.mmt.tracker.market.service.ItemSearchService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ItemSearchServiceTest {

    @Autowired
    private ItemSearchService itemSearchService;

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
}
