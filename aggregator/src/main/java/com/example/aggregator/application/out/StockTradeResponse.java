package com.example.aggregator.application.out;


import com.example.aggregator.domain.Ticker;
import com.example.aggregator.domain.TradeAction;

public record StockTradeResponse(
        Integer customerId,
        Ticker ticker,
        Integer price,
        Integer quantity,
        TradeAction action,
        Integer totalPrice,
        Integer balance
) {


}
