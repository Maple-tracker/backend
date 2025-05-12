package com.mmt.tracker.maple.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExceptionalOption(
        String str,
        String dex,
        @JsonProperty("int") String _int,
        String luk,
        String max_hp,
        String max_mp,
        String attack_power
) {
}
