package com.mmt.tracker.maple.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BaseOption(
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
        String jump,
        String boss_damage,
        String ignore_monster_armor,
        String all_stat,
        String damage,
        String max_hp_rate,
        String max_mp_rate
) {
}
