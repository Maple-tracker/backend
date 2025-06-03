package com.mmt.tracker.icon.controller;

import com.mmt.tracker.icon.controller.dto.request.ItemIconRequest;
import com.mmt.tracker.icon.controller.dto.response.ItemIconResponse;
import com.mmt.tracker.icon.service.ItemIconService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/icon")
@RequiredArgsConstructor
public class ItemIconController {

    private final ItemIconService itemIconService;

    @GetMapping
    public ResponseEntity<ItemIconResponse> getItemIcon(
            @Valid @RequestBody ItemIconRequest request) {
        return ResponseEntity.ok(itemIconService.getIconUrlByItemName(request.itemName()));
    }
}
