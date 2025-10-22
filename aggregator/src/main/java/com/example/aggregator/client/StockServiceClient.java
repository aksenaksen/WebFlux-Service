package com.example.aggregator.client;

import com.example.aggregator.application.out.StockPriceResponse;
import com.example.aggregator.domain.Ticker;
import com.example.aggregator.dto.PriceUpdate;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockServiceClient implements IStockServiceClient {

    private final WebClient stockClient;
    private Flux<PriceUpdate> fluxPrice;

    @PostConstruct
    public void init() {
        this.fluxPrice = getPriceUpdates();

        this.fluxPrice.subscribe();

//        this.fluxPrice.sample(Duration.ofSeconds(2))
//                .subscribe(update -> log.info("received : {}" , update));
    }

    public Mono<StockPriceResponse> getStockPrice(Ticker ticker) {
        return stockClient.get()
                .uri("/stock/{ticker}", ticker)
                .retrieve()
                .bodyToMono(StockPriceResponse.class);
    }

    public Flux<PriceUpdate> priceUpdateStream(){
        return this.fluxPrice;
    }

    private Flux<PriceUpdate> getPriceUpdates() {
        return stockClient.get()
                .uri("/stock/price-stream")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(PriceUpdate.class)
                .retryWhen(retry())
                .cache(1);
//                .share();
    }

    private Retry retry(){
        return Retry.fixedDelay(100, Duration.ofSeconds(1))
                .doBeforeRetry(rs -> log.error("stock service price stream call failed. retrying: {}" , rs.failure().getMessage()));
    }
}
