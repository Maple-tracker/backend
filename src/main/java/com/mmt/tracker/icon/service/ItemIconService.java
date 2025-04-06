package com.mmt.tracker.icon.service;

import com.mmt.tracker.advice.BadRequestException;
import com.mmt.tracker.icon.controller.dto.response.ItemIconResponse;
import com.mmt.tracker.icon.domain.ItemIcon;
import com.mmt.tracker.icon.repository.ItemIconRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemIconService {

    private final ItemIconRepository itemIconRepository;

    @Transactional(readOnly = true)
    public ItemIconResponse getIconUrlByItemName(String itemName) {
        ItemIcon itemIcon =
                itemIconRepository
                        .findByItemName(itemName)
                        .orElseThrow(
                                () ->
                                        new BadRequestException(
                                                "요청한 아이템 아이콘이 존재하지 않습니다: " + itemName));

        return new ItemIconResponse(itemIcon.getItemName(), itemIcon.getIconUrl());
    }
}
