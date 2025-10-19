package com.example.customer.application;

import com.example.customer.domain.Customer;
import com.example.customer.domain.PortfolioItem;
import com.example.customer.dto.TradeResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TradeExecutor {

    private final BalanceUpdater balanceUpdater;
    private final PortfolioUpdater portfolioUpdater;

    public Mono<TradeResult> executeBuy(Customer customer, PortfolioItem portfolioItem, Integer price, Integer quantity) {
        Mono<Customer> updatedCustomer = balanceUpdater.deduct(customer, price * quantity);
        Mono<PortfolioItem> updatedPortfolio = portfolioUpdater.increaseStock(portfolioItem, quantity);

        return Mono.zip(updatedCustomer, updatedPortfolio)
                .map(tuple -> new TradeResult(tuple.getT1(), tuple.getT2()));
    }

    public Mono<TradeResult> executeSell(Customer customer, PortfolioItem portfolioItem, Integer price, Integer quantity) {
        Mono<Customer> updatedCustomer = balanceUpdater.add(customer, price * quantity);
        Mono<PortfolioItem> updatedPortfolio = portfolioUpdater.decreaseStock(portfolioItem, quantity);

        return Mono.zip(updatedCustomer, updatedPortfolio)
                .map(tuple -> new TradeResult(tuple.getT1(), tuple.getT2()));
    }
}
