package com.example.aggregator.application;

import com.example.aggregator.application.in.StockTradeRequest;
import com.example.aggregator.application.in.TradeRequest;
import com.example.aggregator.application.out.StockPriceResponse;
import com.example.aggregator.application.out.StockTradeResponse;
import com.example.aggregator.client.CustomerServiceClient;
import com.example.aggregator.client.ICustomerServiceClient;
import com.example.aggregator.client.IStockServiceClient;
import com.example.aggregator.dto.CustomerInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerPortfolioService {

    private final IStockServiceClient stockClient;
    private final ICustomerServiceClient customerClient;

    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return customerClient.getCustomerInformation(customerId);
    }

    public Mono<StockTradeResponse> buy(Integer customerId, TradeRequest request) {
        return stockClient.getStockPrice(request.ticker())
                .map(StockPriceResponse::price)
                .map(price -> new StockTradeRequest(
                        request.ticker(),
                        price,
                        request.quantity(),
                        request.action()
                ))
                .flatMap(req -> customerClient.buy(customerId,req));
    }

    public Mono<StockTradeResponse> sell(Integer customerId, TradeRequest request) {
        return stockClient.getStockPrice(request.ticker())
                .map(StockPriceResponse::price)
                .map(price -> new StockTradeRequest(
                        request.ticker(),
                        price,
                        request.quantity(),
                        request.action()
                ))
                .flatMap(req -> customerClient.sell(customerId,req));
    }
}
