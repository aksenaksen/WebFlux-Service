package com.example.aggregator.application.in;


import com.example.aggregator.domain.Ticker;
import com.example.aggregator.domain.TradeAction;

public record StockTradeRequest(
        Ticker ticker,
        Integer price,
        Integer quantity,
        TradeAction action
) {
    public Integer getTotalPrice(){
        return price * quantity;
    }
}
