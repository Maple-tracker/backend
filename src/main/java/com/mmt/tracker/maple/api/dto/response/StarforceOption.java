package com.mmt.tracker.maple.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StarforceOption(
        String str,
        String dex,
        @JsonProperty("int") String _int,
        String luk,
        @JsonProperty("max_hp") String maxHp,
        @JsonProperty("max_mp") String maxMp,
        @JsonProperty("attack_power") String attackPower,
        @JsonProperty("magic_power") String magicPower,
        String armor,
        String speed,
        String jump
) {
}
