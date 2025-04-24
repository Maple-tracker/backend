//package com.mmt.tracker.market.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import com.mmt.tracker.advice.BadRequestException;
//import com.mmt.tracker.market.controller.dto.request.PricePostRequest;
//import com.mmt.tracker.market.controller.dto.response.DatePriceResponse;
//import com.mmt.tracker.market.controller.dto.response.DatePriceResponses;
//import com.mmt.tracker.market.controller.dto.response.PricePostResponse;
//import com.mmt.tracker.market.domain.Price;
//import com.mmt.tracker.market.repository.PriceRepository;
//import com.mmt.tracker.market.service.PriceService;
//import jakarta.transaction.Transactional;
//import java.time.LocalDate;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//@Transactional
//@DisplayName("PriceService 통합 테스트")
//class PriceServiceTest {
//
//    @Autowired
//    private PriceService priceService;
//
//    @Autowired
//    private PriceRepository priceRepository;
//
//    @BeforeEach
//    void setUp() {
//        priceRepository.deleteAll();
//        Price price1 = new Price("아이템A", "STR", (short) 10, (short) 20, 1000L, LocalDate.of(2025, 4, 1));
//        Price price2 = new Price("아이템A", "STR", (short) 10, (short) 20, 2000L, LocalDate.of(2025, 4, 2));
//        Price price3 = new Price("아이템B", "STR", (short) 10, (short) 20, 2000L, LocalDate.of(2025, 4, 2));
//        priceRepository.save(price1);
//        priceRepository.save(price2);
//        priceRepository.save(price3);
//    }
//
//    @DisplayName("시세 조회 테스트 - 데이터 존재")
//    @Test
//    void testGetPrices_Success() {
//        // Given & When
//        DatePriceResponses responses = priceService.getPrices("아이템A", "STR", (short) 10, (short) 20);
//
//        // Then
//        assertThat(responses.datePrices()).hasSize(2);
//        List<Long> amounts = responses.datePrices()
//                .stream()
//                .map(DatePriceResponse::amount)
//                .toList();
//
//        assertThat(amounts).containsExactlyInAnyOrder(1000L, 2000L);
//    }
//
//    @DisplayName("시세 조회 테스트 - 데이터 없음")
//    @Test
//    void testGetPrices_NoData() {
//        // When
//        DatePriceResponses responses = priceService.getPrices("아이템B", "DEX", (short) 5, (short) 10);
//
//        // Then
//        assertThat(responses.datePrices()).isEmpty();
//    }
//
//    @DisplayName("시세 등록 테스트 - 날짜 제공")
//    @Test
//    void testPostPrice_Success_WithDate() {
//        // Given
//        LocalDate customDate = LocalDate.of(2025, 4, 3);
//        PricePostRequest request = new PricePostRequest("아이템A", "STR", (short) 10, (short) 20, 3000L, customDate);
//
//        // When
//        PricePostResponse response = priceService.postPrice(request);
//
//        // Then
//        assertThat(response.getMessage()).isEqualTo("시세 등록 완료");
//        List<Price> prices = priceRepository.findPricesByItemNameAndStatTypeAndStarForceAndStatPercent(
//                "아이템A",
//                "STR",
//                (short) 10,
//                (short) 20
//        );
//
//        // 기존 2건에 새로 추가된 1건 -> 총 3건이어야 함
//        assertThat(prices).hasSize(3);
//        Price addedPrice = prices.stream()
//                .filter(price -> price.getAmount() == 3000L)
//                .findFirst()
//                .orElse(null);
//        assertThat(addedPrice).isNotNull();
//        assertThat(addedPrice.getDate()).isEqualTo(customDate);
//    }
//
//    @DisplayName("시세 등록 테스트 - 날짜 미제공 (현재 날짜 사용)")
//    @Test
//    void testPostPrice_Success_NoDate() {
//        // Given
//        PricePostRequest request = new PricePostRequest("아이템A", "STR", (short) 10, (short) 20, 4000L, null);
//
//        // When
//        PricePostResponse response = priceService.postPrice(request);
//
//        // Then
//        assertThat(response.getMessage()).isEqualTo("시세 등록 완료");
//        List<Price> prices = priceRepository.findPricesByItemNameAndStatTypeAndStarForceAndStatPercent(
//                "아이템A",
//                "STR",
//                (short) 10,
//                (short) 20
//        );
//        assertThat(prices).hasSize(3);
//        Price addedPrice = prices.stream()
//                .filter(price -> price.getAmount() == 4000L)
//                .findFirst()
//                .orElse(null);
//        assertThat(addedPrice).isNotNull();
//        assertThat(addedPrice.getDate()).isEqualTo(LocalDate.now());
//    }
//
//    @DisplayName("시세 등록 실패 테스트 - 음수 가격")
//    @Test
//    void testPostPrice_Failure_NegativePrice() {
//        // Given
//        PricePostRequest request = new PricePostRequest(
//                "아이템A",
//                "STR",
//                (short) 10,
//                (short) 20,
//                -100L,
//                LocalDate.of(2025, 4, 3)
//        );
//
//        // When & Then
//        assertThatThrownBy(() -> priceService.postPrice(request))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("price는 0 이상이어야 합니다.");
//    }
//
//    @DisplayName("시세 등록 실패 테스트 - starForce 범위 초과")
//    @Test
//    void testPostPrice_Failure_InvalidStarForce() {
//        // Given
//        PricePostRequest request = new PricePostRequest(
//                "아이템A",
//                "STR",
//                (short) 31,
//                (short) 20,
//                3000L,
//                LocalDate.of(2025, 4, 3)
//        );
//
//        // When & Then
//        assertThatThrownBy(() -> priceService.postPrice(request))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("starForce는 0 이상 30 이하여야 합니다.");
//    }
//
//    @Test
//    @DisplayName("시세 등록 실패 테스트 - statPercent 범위 초과")
//    void testPostPrice_Failure_InvalidStatPercent() {
//        // Given
//        PricePostRequest request = new PricePostRequest(
//                "아이템A",
//                "STR",
//                (short) 10,
//                (short) 40,
//                3000L,
//                LocalDate.of(2025, 4, 3)
//        );
//
//        // When & Then
//        assertThatThrownBy(() -> priceService.postPrice(request))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("statPercent는 0 이상 39 이하여야 합니다.");
//    }
//}
