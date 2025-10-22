package com.example.aggregator.application.out;

import com.example.aggregator.domain.Ticker;

public record StockPriceResponse(
        Ticker ticker,
        Integer price
) {
}
