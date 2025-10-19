package com.example.customer.application;

import com.example.customer.domain.Customer;
import com.example.customer.infra.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BalanceUpdater {

    private final CustomerRepository customerRepository;

    public Mono<Customer> deduct(Customer customer, Integer price) {
        customer.decreaseBalance(price);
        return customerRepository.save(customer);
    }

    public Mono<Customer> add(Customer customer, Integer amount) {
        // TODO: Customer 도메인에 increaseBalance 메서드 필요
        return customerRepository.save(customer);
    }
}
