package com.mmt.tracker.market.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.mmt.tracker.market.controller.dto.request.CompleteItemNameGetRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@DisplayName("ItemSearchController RestAssured 통합 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemSearchControllerTest {

    @LocalServerPort
    int port;

    @DisplayName("GET /api/item_name/completion 자동완성 테스트 - 완성된 단어 입력")
    @Test
    void testCompletion_WithCompleteWord() {
        CompleteItemNameGetRequest requestBody = new CompleteItemNameGetRequest("샤이니");

        Response response = RestAssured.given().port(port).contentType(ContentType.JSON).body(requestBody)
                .when().get("/api/item_name/completion")
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
                .when().get("/api/item_name/completion")
                .then().statusCode(200).extract().response();

        List<String> names = response.jsonPath().getList("names");
        assertThat(names).containsExactly("샤이니 레드 워리어 마이스터 심볼");
    }

    @DisplayName("GET /api/item_name/completion 자동완성 테스트 - 초성 입력")
    @Test
    void testCompletion_WithInitials() {
        CompleteItemNameGetRequest requestBody = new CompleteItemNameGetRequest("ㅅㅇㄴ ㄹㄷ ㅇㄹㅇ ㅁㅇㅅㅌ ㅅㅂ");

        Response response = RestAssured.given().port(port).contentType(ContentType.JSON).body(requestBody)
                .when().get("/api/item_name/completion")
                .then().statusCode(200).extract().response();

        List<String> names = response.jsonPath().getList("names");
        assertThat(names).containsExactly("샤이니 레드 워리어 마이스터 심볼");
    }
}
