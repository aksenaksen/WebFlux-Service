package com.example.customer.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
public class PortfolioItem {

    @Id
    private Integer id;
    private Integer customerId;
    private Ticker ticker;
    private Integer quantity;

    public static PortfolioItem createItem(Integer customerId, Ticker ticker, Integer quantity) {
        PortfolioItem portfolioItem = new PortfolioItem();
        portfolioItem.customerId = customerId;
        portfolioItem.ticker = ticker;
        portfolioItem.quantity = quantity;
        return portfolioItem;
    }

    public void increaseQuantity(Integer quantity) {
        this.quantity += quantity;
    }

    public void decreaseQuantity(Integer quantity) {
        this.quantity -= quantity;
    }
}
