package com.example.aggregator.client;

import com.example.aggregator.application.out.StockPriceResponse;
import com.example.aggregator.domain.Ticker;
import com.example.aggregator.dto.PriceUpdate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IStockServiceClient {
    Mono<StockPriceResponse> getStockPrice(Ticker ticker);
    Flux<PriceUpdate> priceUpdateStream();
}
