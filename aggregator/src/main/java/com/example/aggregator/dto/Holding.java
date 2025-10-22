package com.example.aggregator.dto;


import com.example.aggregator.domain.Ticker;

public record Holding(
        Ticker ticker,
        Integer quantity
) {

}
