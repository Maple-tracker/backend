package com.mmt.tracker.market.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.mmt.tracker.market.controller.dto.request.CompleteItemNameGetRequest;
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
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@DisplayName("ItemSearchController RestAssured 통합 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemSearchControllerTest {

    @LocalServerPort
    int port;

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
    }

    @DisplayName("GET /api/item_name/completion 자동완성 테스트 - 완성된 단어 입력")
    @Test
    void testCompletion_WithCompleteWord() {
        CompleteItemNameGetRequest requestBody = new CompleteItemNameGetRequest("샤이니");

        Response response = RestAssured.given().port(port).contentType(ContentType.JSON).body(requestBody)
                .when().post("/api/item_name/completion")
                .then().statusCode(200).extract().response();

        List<String> names = response.jsonPath().getList("names");
        assertThat(names).isEqualTo(List.of(
                "샤이니 레드 워리어 마이스터 심볼",
                "샤이니 레드 매지션 마이스터 심볼",
                "샤이니 레드 아처 마이스터 심볼",
                "샤이니 레드 시프 마이스터 심볼",
                "샤이니 레드 파이렛 마이스터 심볼"
        ));
    }

    @DisplayName("GET /api/item_name/completion 자동완성 테스트 - 일부 단어 입력")
    @Test
    void testCompletion_WithSomeWord() {
        CompleteItemNameGetRequest requestBody = new CompleteItemNameGetRequest("샤이니 레드 워리어");

        Response response = RestAssured.given().port(port).contentType(ContentType.JSON).body(requestBody)
                .when().post("/api/item_name/completion")
                .then().statusCode(200).extract().response();

        List<String> names = response.jsonPath().getList("names");
        assertThat(names).containsExactly("샤이니 레드 워리어 마이스터 심볼");
    }

    @DisplayName("GET /api/item_name/completion 자동완성 테스트 - 초성 입력")
    @Test
    void testCompletion_WithInitials() {
        CompleteItemNameGetRequest requestBody = new CompleteItemNameGetRequest("ㅅㅇㄴ ㄹㄷ ㅇㄹㅇ ㅁㅇㅅㅌ ㅅㅂ");

        Response response = RestAssured.given().port(port).contentType(ContentType.JSON).body(requestBody)
                .when().post("/api/item_name/completion")
                .then().statusCode(200).extract().response();

        List<String> names = response.jsonPath().getList("names");
        assertThat(names).containsExactly("샤이니 레드 워리어 마이스터 심볼");
    }

    @DisplayName("GET /api/item_name/options 아이템 옵션 조회 테스트")
    @Test
    void testGetItemOptions() {
        String itemName = ItemName.EYE_ECC1.getValue();

        Response response = RestAssured.given().port(port)
                .when().get("/api/item_name/options?name=" + itemName)
                .then().statusCode(200).extract().response();

        assertThat(response.statusCode()).isEqualTo(200);

        List<Map<String, Object>> combinations = response.jsonPath().getList("combinations");
        assertThat(combinations).hasSize(3);

        List<Short> starForces = response.jsonPath().getList("combinations.starForce", Short.class);
        assertThat(starForces).containsExactlyInAnyOrder((short) 17, (short) 18, (short) 17);

        List<String> statTypes = response.jsonPath().getList("combinations.statType", String.class);
        assertThat(statTypes).containsExactlyInAnyOrder("STR", "STR", "DEX");

        List<String> potentialOptions = response.jsonPath().getList("combinations.potentialOption", String.class);
        assertThat(potentialOptions).allMatch(option ->
                option.equals("레전드리 30% 정옵"));

        List<String> additionalPotentialOptions = response.jsonPath().getList(
                "combinations.additionalPotentialOption",
                String.class
        );
        assertThat(additionalPotentialOptions).allMatch(option ->
                option.equals("레전드리 2 2"));

        Map<String, List<String>> availableOptions = response.jsonPath().getObject("availableOptions", Map.class);

        assertThat(availableOptions.get("starForce"))
                .containsExactlyInAnyOrder("17성", "18성");

        assertThat(availableOptions.get("potentialOption"))
                .containsExactly("레전드리 30% 정옵");

        assertThat(availableOptions.get("additionalPotentialOption"))
                .containsExactly("레전드리 2 2");

        assertThat(availableOptions.get("statType"))
                .containsExactlyInAnyOrder("STR", "DEX");

        // 카테고리화된 옵션 검증
        Map<String, Object> categorizedOptions = response.jsonPath().getObject("categorizedOptions", Map.class);
        assertThat(categorizedOptions).hasSize(2);
        assertThat(categorizedOptions.keySet()).containsExactlyInAnyOrder("17성", "18성");

        // 17성 카테고리 검증
        Map<String, Object> starForce17 = response.jsonPath().getObject("categorizedOptions.'17성'", Map.class);
        assertThat(starForce17.get("name")).isEqualTo("17성");

        Map<String, Object> starForce17SubCategories = response.jsonPath().getObject(
                "categorizedOptions.'17성'.subCategories",
                Map.class
        );
        assertThat(starForce17SubCategories.keySet()).containsExactlyInAnyOrder("STR", "DEX");

        // 17성 > STR 카테고리 검증
        Map<String, Object> starForce17Str = response.jsonPath().getObject(
                "categorizedOptions.'17성'.subCategories.STR",
                Map.class
        );
        assertThat(starForce17Str.get("name")).isEqualTo("STR");

        Map<String, Object> starForce17StrSubCategories = response.jsonPath().getObject(
                "categorizedOptions.'17성'.subCategories.STR.subCategories",
                Map.class
        );
        assertThat(starForce17StrSubCategories.keySet()).hasSize(1);

        // 최하위 카테고리에 옵션 ID가 있는지 검증
        List<Integer> optionIds = response.jsonPath().getList(
                "categorizedOptions.'17성'.subCategories.STR.subCategories.'레전드리 30% 정옵'.subCategories.'레전드리 2 2'.optionIds");
        assertThat(optionIds).isNotEmpty();
    }
}
