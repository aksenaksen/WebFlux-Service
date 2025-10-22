package com.example.aggregator.application.in;

import com.example.aggregator.domain.Ticker;
import com.example.aggregator.domain.TradeAction;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TradeRequest(
        @NotNull(message = "Ticker은 필수값입니다.")
        Ticker ticker,
        @NotNull(message = "TradeAction은 필수값입니다.")
        TradeAction action,
        @NotNull(message = "quantity는 필수값입니다.")
        @Min(value = 1, message = "quantity는 최소 1보다 큰 값이어야합니다.")
        Integer quantity
) {
}
