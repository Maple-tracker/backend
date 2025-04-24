package com.mmt.tracker.market.exception;

import com.mmt.tracker.advice.NotFoundException;

public class ItemOptionNotFound extends NotFoundException {

    private static final String MESSAGE = "존재하지 않는 아이템입니다.";
    public ItemOptionNotFound() {
        super(MESSAGE);
    }
}
