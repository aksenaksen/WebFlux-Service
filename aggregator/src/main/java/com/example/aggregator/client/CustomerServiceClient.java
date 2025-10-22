package com.example.aggregator.client;

import com.example.aggregator.application.in.StockTradeRequest;
import com.example.aggregator.application.out.StockTradeResponse;
import com.example.aggregator.dto.CustomerInformation;
import com.example.aggregator.exception.CustomerNotFoundException;
import com.example.aggregator.exception.InvalidTradeRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClientResponseException.NotFound;
import org.springframework.web.reactive.function.client.WebClientResponseException.BadRequest;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerServiceClient implements ICustomerServiceClient {

    private final WebClient customerClient;

    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return customerClient.get()
                .uri("/customers/{customerId}", customerId)
                .retrieve()
                .bodyToMono(CustomerInformation.class)
                .onErrorMap(NotFound.class,e -> new CustomerNotFoundException(customerId));
    }

    public Mono<StockTradeResponse> buy(Integer customerId, StockTradeRequest request){
        return customerClient.post()
                .uri("/customers/{customerId}/buy", customerId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(StockTradeResponse.class)
                .onErrorMap(NotFound.class,e -> new CustomerNotFoundException(customerId))
                .onErrorMap(BadRequest.class, e -> {
                    ProblemDetail problemDetail = e.getResponseBodyAs(ProblemDetail.class);
                    var message = Objects.nonNull(problemDetail) ? problemDetail.getDetail() : e.getMessage();
                    return new InvalidTradeRequestException(message);
                });
    }

    public Mono<StockTradeResponse> sell(Integer customerId, StockTradeRequest request){
        return customerClient.post()
                .uri("/customers/{customerId}/sell", customerId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(StockTradeResponse.class)
                .onErrorMap(NotFound.class,e -> new CustomerNotFoundException(customerId))
                .onErrorMap(BadRequest.class, e -> {
                    ProblemDetail problemDetail = e.getResponseBodyAs(ProblemDetail.class);
                    var message = Objects.nonNull(problemDetail) ? problemDetail.getDetail() : e.getMessage();
                    return new InvalidTradeRequestException(message);
                });
    }
}
