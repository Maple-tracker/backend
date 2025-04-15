package com.mmt.tracker.market.controller;

import com.mmt.tracker.market.controller.dto.request.CompleteItemNameGetRequest;
import com.mmt.tracker.market.controller.dto.response.CompleteItemNameResponse;
import com.mmt.tracker.market.controller.dto.response.ItemOptionsGetResponse;
import com.mmt.tracker.market.service.ItemSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/item_name")
@RequiredArgsConstructor
public class ItemSearchController {

    private final ItemSearchService itemSearchService;

    @PostMapping("/completion")
    public ResponseEntity<CompleteItemNameResponse> completion(@RequestBody CompleteItemNameGetRequest request) {
        return ResponseEntity.ok().body(itemSearchService.getCompleteItemNames(request));
    }

    @GetMapping("/options")
    public ResponseEntity<ItemOptionsGetResponse> getItemOptions(@RequestParam String itemName) {
        ItemOptionsGetResponse response = itemSearchService.getItemOptions(itemName);
        return ResponseEntity.ok(response);
    }
}
