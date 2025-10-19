package com.example.customer.dto;

import com.example.customer.domain.PortfolioItem;
import com.example.customer.domain.Ticker;

public record Holding(
        Ticker ticker,
        Integer quantity
) {
    public static Holding from(PortfolioItem portfolioItem){
        return new Holding(portfolioItem.getTicker(), portfolioItem.getQuantity());
    }
}
