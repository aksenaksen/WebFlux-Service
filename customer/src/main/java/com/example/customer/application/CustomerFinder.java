package com.example.customer.application;

import com.example.customer.domain.Customer;
import com.example.customer.exception.CustomerNotFoundException;
import com.example.customer.infra.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerFinder {

    private final CustomerRepository customerRepository;

    public Mono<Customer> findById(Integer id) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(id)));
    }
}
