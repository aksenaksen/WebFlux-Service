package com.example.customer.application;

import com.example.customer.dto.CustomerInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerFinder customerFinder;
    private final PortfolioFinder portfolioFinder;

    public Mono<CustomerInformation> getCustomerInformation(Integer id) {
        return customerFinder.findById(id)
                .flatMap(customer -> portfolioFinder.findHoldingsByCustomerId(customer.getId())
                        .map(holdings -> CustomerInformation.of(customer, holdings)));
    }

}
