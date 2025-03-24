package com.mmt.tracker.market.controller;

import com.mmt.tracker.market.controller.dto.request.PricePostRequest;
import com.mmt.tracker.market.controller.dto.response.DatePriceResponses;
import com.mmt.tracker.market.controller.dto.response.PricePostResponse;
import com.mmt.tracker.market.service.PriceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@Tag(name = "Price", description = "경매장 시세 조회 API")
@RestController
@RequestMapping("/api/market/price")
@RequiredArgsConstructor
public class PriceController {
    private final PriceService priceService;

    @Operation(
            summary = "아이템 시세 조회",
            description = "아이템 이름, 스텟 타입, 스타포스 강화 수치, 스텟 퍼센트를 입력하여 해당 아이템의 시세 조회")
    @GetMapping
    public DatePriceResponses getPrices(
            @RequestParam String itemName,
            @RequestParam String statType,
            @RequestParam Short starForce,
            @RequestParam Short statPercent) {
        return priceService.getPrices(itemName, statType, starForce, statPercent);
    }

    @Operation(
            summary = "아이템 시세 등록",
            description = "아이템 이름, 스텟 타입, 스타포스 강화 수치, 스텟 퍼센트, 시세를 입력하여 해당 아이템의 시세 등록")
    @PostMapping
    public PricePostResponse postPrice(@RequestBody PricePostRequest request) {
        return priceService.postPrice(request);
    }
}
