package com.example.aggregator.presentation;

import com.example.aggregator.application.CustomerPortfolioService;
import com.example.aggregator.application.in.StockTradeRequest;
import com.example.aggregator.application.in.TradeRequest;
import com.example.aggregator.application.out.StockTradeResponse;
import com.example.aggregator.dto.CustomerInformation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerPortfolioController {

    private final CustomerPortfolioService customerPortfolioService;

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomerInformation(@PathVariable Integer customerId) {
        log.info("GET : customerId = {}", customerId);
        return customerPortfolioService.getCustomerInformation(customerId);
    }

    @PostMapping("/{customerId}/buy")
    public Mono<StockTradeResponse> buy(@PathVariable Integer customerId, @Valid @RequestBody Mono<TradeRequest> requestMono) {
        return requestMono
                .flatMap(request -> customerPortfolioService.buy(customerId, request));
    }

    @PostMapping("/{customerId}/sell")
    public Mono<StockTradeResponse> sell(@PathVariable Integer customerId, @Valid @RequestBody Mono<TradeRequest> requestMono) {
        return requestMono
                .flatMap(request -> customerPortfolioService.sell(customerId, request));
    }


}
