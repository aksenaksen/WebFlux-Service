package com.example.customer.presentation;

import com.example.customer.application.CustomerService;
import com.example.customer.application.TradeService;
import com.example.customer.application.in.StockTradeRequest;
import com.example.customer.application.out.StockTradeResponse;
import com.example.customer.dto.CustomerInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final TradeService tradeService;

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomerInformation(@PathVariable Integer customerId) {
        return customerService.getCustomerInformation(customerId);
    }

    @PostMapping("/{customerId}/buy")
    public Mono<StockTradeResponse> buy(@PathVariable Integer customerId, @RequestBody Mono<StockTradeRequest> requestMono) {
        return requestMono.flatMap( request  -> tradeService.buy(customerId, request));
    }

    @PostMapping("/{customerId}/sell")
    public Mono<StockTradeResponse> sell(@PathVariable Integer customerId, @RequestBody Mono<StockTradeRequest> requestMono) {
        return requestMono.flatMap( request  -> tradeService.sell(customerId, request));
    }

}
