package com.example.customer.application.out;

import com.example.customer.application.in.StockTradeRequest;
import com.example.customer.domain.Customer;
import com.example.customer.domain.PortfolioItem;
import com.example.customer.domain.Ticker;
import com.example.customer.domain.TradeAction;

public record StockTradeResponse(
        Integer customerId,
        Ticker ticker,
        Integer price,
        Integer quantity,
        TradeAction action,
        Integer totalPrice,
        Integer balance
) {

    public static StockTradeResponse of(Customer customer, PortfolioItem portfolioItem, StockTradeRequest request){
        return new StockTradeResponse(
                customer.getId(),
                portfolioItem.getTicker(),
                request.price(),
                request.quantity(),
                request.action(),
                request.getTotalPrice(),
                customer.getBalance());
    }
}
