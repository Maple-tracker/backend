package com.mmt.tracker.market.controller;

import com.mmt.tracker.market.controller.dto.request.CompleteItemNameGetRequest;
import com.mmt.tracker.market.controller.dto.response.CompleteItemNameResponse;
import com.mmt.tracker.market.service.ItemSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/item_name")
@RequiredArgsConstructor
public class ItemSearchController {

    private final ItemSearchService itemSearchService;

    @GetMapping("/completion")
    public ResponseEntity<CompleteItemNameResponse> completion(@RequestBody CompleteItemNameGetRequest request) {
        return ResponseEntity.ok().body(itemSearchService.getCompleteItemNames(request));
    }
}
