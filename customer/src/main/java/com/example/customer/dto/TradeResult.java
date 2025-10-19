package com.example.customer.dto;

import com.example.customer.domain.Customer;
import com.example.customer.domain.PortfolioItem;

public record TradeResult(
        Customer customer,
        PortfolioItem portfolioItem
) {
}
