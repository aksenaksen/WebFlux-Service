package com.example.customer.application;

import com.example.customer.domain.PortfolioItem;
import com.example.customer.domain.Ticker;
import com.example.customer.infra.PortfolioItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PortfolioUpdater {

    private final PortfolioItemRepository portfolioItemRepository;

    public Mono<PortfolioItem> increaseStock(PortfolioItem portfolioItem, Integer quantity) {
        portfolioItem.increaseQuantity(quantity);
        return portfolioItemRepository.save(portfolioItem);
    }

    public Mono<PortfolioItem> decreaseStock(PortfolioItem portfolioItem, Integer quantity) {
        portfolioItem.decreaseQuantity(quantity);
        return portfolioItemRepository.save(portfolioItem);
    }

}
