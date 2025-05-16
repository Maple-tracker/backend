package com.mmt.tracker.market.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mmt.tracker.advice.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("ItemName Enum 테스트")
@ActiveProfiles("test")
class ItemNameTest {

    @Test
    @DisplayName("유효한 아이템 이름으로 fromString 테스트")
    void testFromString_ValidExact() {
        // Given
        String validName = "샤이니 레드 워리어 마이스터 심볼";

        // When
        ItemName itemName = ItemName.fromString(validName);

        // Then
        assertThat(itemName).isEqualTo(ItemName.EYE_ECC1);
    }

    @Test
    @DisplayName("Enum 스타일 아이템 이름으로 fromString 테스트")
    void testFromString_EnumStyle() {
        // Given
        String enumStyleName = "EYE_ECC1";

        // When
        ItemName itemName = ItemName.fromString(enumStyleName);

        // Then
        assertThat(itemName).isEqualTo(ItemName.EYE_ECC1);
    }

    @Test
    @DisplayName("잘못된 아이템 이름으로 fromString 실패 테스트")
    void testFromString_Invalid() {
        // Given
        String invalidName = "존재하지 않는 아이템";

        // When & Then
        assertThatThrownBy(() -> ItemName.fromString(invalidName))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Unknown item name");
    }

    @Test
    @DisplayName("null 아이템 이름으로 fromString 실패 테스트")
    void testFromString_Null() {
        // When & Then
        assertThatThrownBy(() -> ItemName.fromString(null))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Item name cannot be null");
    }
}
