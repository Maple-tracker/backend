package com.mmt.tracker.maple.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EquippedItem(
    @JsonProperty("item_equipment_part") String part,
    @JsonProperty("item_equipment_slot") String slot,
    @JsonProperty("item_name") String name,
    @JsonProperty("item_icon") String icon,
    @JsonProperty("potential_option_grade") String potentialOptionGrade,
    @JsonProperty("additional_potential_option_grade") String additionalPotentialOptionGrade,
    @JsonProperty("potential_option_1") String potentialOption1,
    @JsonProperty("potential_option_2") String potentialOption2,
    @JsonProperty("potential_option_3") String potentialOption3,
    @JsonProperty("additional_potential_option_1") String additionalPotentialOption1,
    @JsonProperty("additional_potential_option_2") String additionalPotentialOption2,
    @JsonProperty("additional_potential_option_3") String additionalPotentialOption3,
    @JsonProperty("starforce") Short starforce,
    @JsonProperty("starforce_scroll_flag") String starforceScrollFlag,
    @JsonProperty("cuttable_count") Short cuttableCount,
    @JsonProperty("item_total_option") TotalOption totalOption,
    @JsonProperty("item_base_option") BaseOption baseOption,
    @JsonProperty("item_add_option") AddOption addOption,
    @JsonProperty("item_etc_option") EtcOption etcOption,
    @JsonProperty("item_starforce_option") StarforceOption starforceOption,
    @JsonProperty("item_exceptional_option") ExceptionalOption exceptionalOption,
    @JsonProperty("soul_name") String soulName,
    @JsonProperty("soul_option") String soulOption
) {
    /**
     * TotalOption (BaseOption + AddOption + EtcOption + StarforceOption)
     * SoulOption은 무기 전용, 에디셔널 잠재능력 밑에 별도로 표시
     * ExceptionalOption은 장신구 전용, 에디셔널 잠재능력 밑에 별도로 표시
     */
}
