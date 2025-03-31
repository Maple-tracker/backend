package com.mmt.tracker.market.controller;

import com.mmt.tracker.market.controller.dto.request.ItemTradeGetRequest;
import com.mmt.tracker.market.controller.dto.request.ItemTradePostRequest;
import com.mmt.tracker.market.controller.dto.response.ItemTradePostResponse;
import com.mmt.tracker.market.controller.dto.response.ItemTradeResponse;
import com.mmt.tracker.market.service.ItemTradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/market/trades")
@Tag(name = "Item Trade", description = "아이템 거래 내역 API")
public class ItemTradeController {

    private final ItemTradeService itemTradeService;

    public ItemTradeController(ItemTradeService itemTradeService) {
        this.itemTradeService = itemTradeService;
    }

    @Operation(summary = "아이템 거래 내역 조회", description = "아이템 정보로 거래 내역을 조회합니다")
    @PostMapping("/search")
    public ResponseEntity<ItemTradeResponse> getItemTradeHistories(@RequestBody ItemTradeGetRequest request) {
        ItemTradeResponse response = itemTradeService.getItemTradeHistories(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "아이템 거래 내역 등록", description = "아이템의 거래 내역을 등록합니다")
    @PostMapping
    public ResponseEntity<ItemTradePostResponse> postItemTradeHistory(@RequestBody ItemTradePostRequest request) {
        ItemTradePostResponse response = itemTradeService.postItemTradeHistory(request);
        return ResponseEntity.ok(response);
    }
} 