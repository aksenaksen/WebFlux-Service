package com.example.aggregator.client;

import com.example.aggregator.application.in.StockTradeRequest;
import com.example.aggregator.application.out.StockTradeResponse;
import com.example.aggregator.dto.CustomerInformation;
import reactor.core.publisher.Mono;

public interface ICustomerServiceClient {
    Mono<CustomerInformation> getCustomerInformation(Integer customerId);
    Mono<StockTradeResponse> buy(Integer customerId, StockTradeRequest request);
    Mono<StockTradeResponse> sell(Integer customerId, StockTradeRequest request);
}
