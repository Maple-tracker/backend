package com.mmt.tracker.maple.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CharacterEquipmentRequest {
    private String characterName;
    private LocalDate date;
}
