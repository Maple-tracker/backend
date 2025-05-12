package com.mmt.tracker.maple.controller;

import com.mmt.tracker.maple.controller.dto.request.CharacterInfoRequest;
import com.mmt.tracker.maple.controller.dto.response.CharacterInfoResponse;
import com.mmt.tracker.maple.service.MapleService;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/maple")
@RequiredArgsConstructor
public class MapleController {

    private final MapleService mapleService;

    @GetMapping("/character")
    public ResponseEntity<CharacterInfoResponse> getCharacterInfo(
            @RequestParam String characterName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) throws Exception {
        CharacterInfoRequest request = new CharacterInfoRequest(characterName, date);
        return ResponseEntity.ok(mapleService.getCharacterInfo(request));
    }
}
