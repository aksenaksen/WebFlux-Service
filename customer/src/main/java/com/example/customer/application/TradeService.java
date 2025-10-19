package com.example.customer.application;

import com.example.customer.application.in.StockTradeRequest;
import com.example.customer.application.out.StockTradeResponse;
import com.example.customer.domain.PortfolioItem;
import com.example.customer.exception.InsufficientBalanceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final CustomerFinder customerFinder;
    private final PortfolioFinder portfolioFinder;
    private final TradeExecutor tradeExecutor;

    public Mono<StockTradeResponse> buy(Integer customerId, StockTradeRequest request) {
        return customerFinder.findById(customerId)
                .filter(customer -> customer.canBuy(request.getTotalPrice()))
                .switchIfEmpty(Mono.error(new InsufficientBalanceException(customerId)))
                .flatMap(customer ->
                        portfolioFinder.findByIdAndTicker(customerId, request.ticker())
                                .defaultIfEmpty(createNewPortfolioItem(customerId, request))
                                .flatMap(portfolioItem ->
                                        tradeExecutor.executeBuy(customer, portfolioItem, request.price(), request.quantity())
                                )
                )
                .map(tradeResult -> StockTradeResponse.of(tradeResult.customer(), tradeResult.portfolioItem(), request));
    }

    private PortfolioItem createNewPortfolioItem(Integer customerId, StockTradeRequest request) {
        return PortfolioItem.createItem(
                customerId,
                request.ticker(),
                request.getTotalPrice()
        );
    }
}
