package com.mmt.tracker.icon.controller;

import com.mmt.tracker.advice.BadRequestException;
import com.mmt.tracker.icon.controller.dto.request.ItemIconRequest;
import com.mmt.tracker.icon.controller.dto.response.ItemIconResponse;
import com.mmt.tracker.icon.service.ItemIconService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "아이콘 API", description = "아이콘 조회 API")
@Slf4j
@RestController
@RequestMapping("/api/icon")
@RequiredArgsConstructor
public class ItemIconController {

    private final ItemIconService itemIconService;

    @GetMapping
    public ResponseEntity<ItemIconResponse> getItemIcon(@RequestBody ItemIconRequest request) {
        if (request == null || request.itemName() == null || request.itemName().isBlank()) {
            throw new BadRequestException("아이템 이름이 비어있거나 요청이 잘못되었습니다");
        }
        
        return ResponseEntity.ok(itemIconService.getIconUrlByItemName(request.itemName()));
    }
} 