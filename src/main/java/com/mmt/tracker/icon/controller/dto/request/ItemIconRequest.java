package com.mmt.tracker.icon.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record ItemIconRequest(@NotNull(message = "아이템 이름은 필수 입력값입니다") String itemName) {}
