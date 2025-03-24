package com.mmt.tracker.market.dto.response;

import java.time.LocalDate;

public record DatePriceResponse(LocalDate date, Long amount) {}
