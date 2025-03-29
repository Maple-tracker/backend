package com.mmt.tracker.icon.service;

import com.mmt.tracker.advice.BadRequestException;
import com.mmt.tracker.icon.controller.dto.response.ItemIconResponse;
import com.mmt.tracker.icon.domain.ItemIcon;
import com.mmt.tracker.icon.repository.ItemIconRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemIconService {

    private final ItemIconRepository itemIconRepository;

    @Transactional(readOnly = true)
    public ItemIconResponse getIconUrlByItemName(String itemName) {
        if (itemName == null || itemName.isBlank()) {
            log.error("아이템 이름이 비어있습니다");
            throw new BadRequestException("아이템 이름은 필수 입력값입니다");
        }

        log.info("아이템 아이콘 조회 요청: {}", itemName);

        ItemIcon itemIcon =
                itemIconRepository
                        .findByItemName(itemName)
                        .orElseThrow(
                                () -> {
                                    log.error("아이템 아이콘을 찾을 수 없음: {}", itemName);
                                    return new BadRequestException(
                                            "요청한 아이템 아이콘이 존재하지 않습니다: " + itemName);
                                });

        return new ItemIconResponse(itemIcon.getItemName(), itemIcon.getIconUrl());
    }
}
