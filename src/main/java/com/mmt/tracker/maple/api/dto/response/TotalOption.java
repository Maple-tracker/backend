package com.mmt.tracker.maple.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TotalOption(
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
        String jump,
        @JsonProperty("boss_damage") String bossDamage,
        @JsonProperty("ignore_monster_armor") String ignoreMonsterArmor,
        @JsonProperty("all_stat") String allStat,
        String damage,
        @JsonProperty("equipment_level_decrease") Integer equipmentLevelDecrease,
        @JsonProperty("max_hp_rate") String maxHpRate,
        @JsonProperty("max_mp_rate") String maxMpRate
) {
}
