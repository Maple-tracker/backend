package com.mmt.tracker.market.controller;

import com.mmt.tracker.market.controller.dto.request.ItemOptionIdsPostRequest;
import com.mmt.tracker.market.controller.dto.request.ItemTradePostRequest;
import com.mmt.tracker.market.controller.dto.response.ItemPriceHistoryResponse;
import com.mmt.tracker.market.controller.dto.response.ItemTradePostResponse;
import com.mmt.tracker.market.service.ItemTradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
@Tag(name = "Item Trade", description = "아이템 거래 내역 API")
public class ItemTradeController {

    private final ItemTradeService itemTradeService;

    @PostMapping("/price/{itemName}")
    public ResponseEntity<ItemPriceHistoryResponse> getItemPriceHistory(
            @PathVariable String itemName,
            @RequestBody ItemOptionIdsPostRequest optionIds
    ) {
        ItemPriceHistoryResponse response = itemTradeService.readItemPriceHistory(itemName, optionIds);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "아이템 거래 내역 등록", description = "아이템의 거래 내역을 등록합니다")
    @PostMapping
    public ResponseEntity<ItemTradePostResponse> postItemTradeHistory(@RequestBody ItemTradePostRequest request) {
        itemTradeService.postItemTradeHistory(request);
        return ResponseEntity.created(URI.create("/api/market/trades")).build();
    }
} 
