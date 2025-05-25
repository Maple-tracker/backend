package com.mmt.tracker.advice;

public class BadRequestException extends TrackerGlobalException {

    public BadRequestException(String message) {
        super(message);
    }
}
