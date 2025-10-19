package com.example.customer.application.in;

import com.example.customer.domain.Ticker;
import com.example.customer.domain.TradeAction;

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
