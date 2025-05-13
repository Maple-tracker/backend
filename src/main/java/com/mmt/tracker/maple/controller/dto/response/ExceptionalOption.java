package com.mmt.tracker.maple.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExceptionalOption(
        String str,
        String dex,
        @JsonProperty("int") String _int,
        String luk,
        @JsonProperty("max_hp") String maxHp,
        @JsonProperty("max_mp") String maxMp,
        @JsonProperty("attack_power") String attackPower
) {
}
