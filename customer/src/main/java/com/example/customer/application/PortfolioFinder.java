package com.example.customer.application;

import com.example.customer.domain.PortfolioItem;
import com.example.customer.domain.Ticker;
import com.example.customer.dto.Holding;
import com.example.customer.infra.PortfolioItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PortfolioFinder {

    private final PortfolioItemRepository portfolioItemRepository;

    public Mono<List<Holding>> findHoldingsByCustomerId(Integer customerId) {
        return portfolioItemRepository.findByCustomerId(customerId)
                .map(Holding::from)
                .collectList();
    }

    public Mono<PortfolioItem> findByIdAndTicker(Integer customerId, Ticker ticker) {
        return portfolioItemRepository.findByCustomerIdAndTicker(customerId, ticker);
    }
}
