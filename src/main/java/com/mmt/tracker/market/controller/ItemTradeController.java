package com.mmt.tracker.market.controller;

import com.mmt.tracker.market.controller.dto.request.ItemTradePostRequest;
import com.mmt.tracker.market.controller.dto.response.ItemPriceHistoryResponse;
import com.mmt.tracker.market.controller.dto.response.ItemTradePostResponse;
import com.mmt.tracker.market.service.ItemTradeService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemTradeController {

    private final ItemTradeService itemTradeService;

    @GetMapping("/price/{itemName}")
    public ResponseEntity<ItemPriceHistoryResponse> getItemPriceHistory(
            @PathVariable String itemName,
            @RequestParam Long optionId
    ) {
        ItemPriceHistoryResponse response = itemTradeService.readItemPriceHistory(itemName, optionId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ItemTradePostResponse> postItemTradeHistory(@RequestBody ItemTradePostRequest request) {
        itemTradeService.postItemTradeHistory(request);
        return ResponseEntity.created(URI.create("/api/market/trades")).build();
    }
} 
