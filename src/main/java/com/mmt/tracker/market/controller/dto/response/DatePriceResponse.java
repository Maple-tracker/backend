package com.mmt.tracker.market.controller.dto.response;

import java.time.LocalDate;

public record DatePriceResponse(LocalDate date, Long amount) {}
