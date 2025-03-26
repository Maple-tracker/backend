package com.mmt.tracker.market.service;

import com.mmt.tracker.advice.BadRequestException;
import com.mmt.tracker.market.domain.Price;
import com.mmt.tracker.market.controller.dto.request.PricePostRequest;
import com.mmt.tracker.market.controller.dto.response.DatePriceResponse;
import com.mmt.tracker.market.controller.dto.response.DatePriceResponses;
import com.mmt.tracker.market.controller.dto.response.PricePostResponse;
import com.mmt.tracker.market.repository.PriceRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PriceService {

    private final PriceRepository priceRepository;

    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Transactional(readOnly = true)
    public DatePriceResponses getPrices(
            String itemName, String statType, Short starForce, Short statPercent) {
        List<Price> prices =
                priceRepository.findPricesByItemNameAndStatTypeAndStarForceAndStatPercent(
                        itemName, statType, starForce, statPercent);

        return convertPriceToResponse(prices);
    }

    private DatePriceResponses convertPriceToResponse(List<Price> prices) {
        return new DatePriceResponses(
                prices.stream()
                        .map(price -> new DatePriceResponse(price.getDate(), price.getAmount()))
                        .toList());
    }

    @Transactional
    public PricePostResponse postPrice(PricePostRequest request) {
        validatePriceRequest(request);

        Price price =
                new Price(
                        request.itemName(),
                        request.statType(),
                        request.starForce(),
                        request.statPercent(),
                        request.amount(),
                        request.date() != null ? request.date() : LocalDate.now());
        priceRepository.save(price);
        PricePostResponse response = new PricePostResponse();
        response.setMessage("시세 등록 완료");
        return response;
    }

    private void validatePriceRequest(PricePostRequest request) {
        if (request.amount() != null && request.amount() < 0) {
            throw new BadRequestException("price는 0 이상이어야 합니다.");
        }
        if (request.starForce() < 0 || request.starForce() > 30) {
            throw new BadRequestException("starForce는 0 이상 30 이하여야 합니다.");
        }
        if (request.statPercent() < 0 || request.statPercent() > 39) {
            throw new BadRequestException("statPercent는 0 이상 39 이하여야 합니다.");
        }
    }
}
