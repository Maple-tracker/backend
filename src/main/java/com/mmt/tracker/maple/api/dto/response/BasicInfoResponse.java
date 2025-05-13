package com.mmt.tracker.maple.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BasicInfoResponse(
        @JsonProperty("character_name") String characterName,
        @JsonProperty("world_name") String worldName,
        @JsonProperty("character_class") String _class,
        @JsonProperty("character_level") Short level,
        @JsonProperty("character_guild_name") String guildName,
        @JsonProperty("character_image") String characterImageUrl
        ) {
}
