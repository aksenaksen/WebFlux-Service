package com.example.aggregator.dto;

import com.example.aggregator.domain.Ticker;

import java.time.LocalDateTime;

public record PriceUpdate(
        Ticker ticker,
        Integer price,
        LocalDateTime time
) {
}
