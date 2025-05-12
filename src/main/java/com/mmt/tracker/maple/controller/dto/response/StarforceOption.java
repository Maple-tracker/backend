package com.mmt.tracker.maple.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StarforceOption(
        String str,
        String dex,
        @JsonProperty("int") String _int,
        String luk,
        String max_hp,
        String max_mp,
        String attack_power,
        String magic_power,
        String armor,
        String speed,
        String jump
) {
}
