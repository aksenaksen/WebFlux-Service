package com.example.customer;

import com.example.customer.application.in.StockTradeRequest;
import com.example.customer.domain.Ticker;
import com.example.customer.domain.TradeAction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class CustomerServiceApplicationTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceApplicationTest.class);

    @Autowired
    private WebTestClient client;

    @Test
    @DisplayName("GET : /customers")
    void customerInformationTest(){
        //given
        client.get()
                .uri("/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(e -> log.info("{}", new String(e.getResponseBody())));
        //when

        //then
    }

    @Test
    @DisplayName("POST: buy - 정상 구매")
    void buyTest() {
        //given
        var buyRequest = new StockTradeRequest(Ticker.GOOGLE, 100, 5, TradeAction.BUY);

        //when
        client.post()
                .uri("/customers/1/buy")
                .bodyValue(buyRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(e -> log.info("Buy Result: {}", new String(e.getResponseBody())));

        //then
        client.get()
                .uri("/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(e -> log.info("Customer Info After Buy: {}", new String(e.getResponseBody())));
    }

    @Test
    @DisplayName("POST: buy - 잔액 부족 시 에러")
    void buyWithInsufficientBalanceTest() {
        //given
        var buyRequest = new StockTradeRequest(Ticker.GOOGLE, 100000, 100, TradeAction.BUY);

        //when & then
        client.post()
                .uri("/customers/1/buy")
                .bodyValue(buyRequest)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(e -> log.info("Insufficient Balance Error: {}", new String(e.getResponseBody())));
    }

    @Test
    @DisplayName("POST: sell - 정상 판매")
    void sellTest() {
        //given - 먼저 구매
        var buyRequest = new StockTradeRequest(Ticker.GOOGLE, 100, 10, TradeAction.BUY);
        client.post()
                .uri("/customers/1/buy")
                .bodyValue(buyRequest)
                .exchange()
                .expectStatus().is2xxSuccessful();

        //when - 판매
        var sellRequest = new StockTradeRequest(Ticker.GOOGLE, 100, 5, TradeAction.SELL);
        client.post()
                .uri("/customers/1/sell")
                .bodyValue(sellRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(e -> log.info("Sell Result: {}", new String(e.getResponseBody())));

        //then
        client.get()
                .uri("/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(e -> log.info("Customer Info After Sell: {}", new String(e.getResponseBody())));
    }

    @Test
    @DisplayName("POST: sell - 보유 수량 부족 시 에러")
    void sellWithInsufficientStockTest() {
        //given
        var sellRequest = new StockTradeRequest(Ticker.GOOGLE, 100, 1000, TradeAction.SELL);

        //when & then
        client.post()
                .uri("/customers/1/sell")
                .bodyValue(sellRequest)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(e -> log.info("Insufficient Stock Error: {}", new String(e.getResponseBody())));
    }

    @Test
    @DisplayName("POST: buy & sell - 연속 거래")
    void multipleTrades() {
        //given
        var buyRequest1 = new StockTradeRequest(Ticker.GOOGLE, 100, 5, TradeAction.BUY);
        var buyRequest2 = new StockTradeRequest(Ticker.APPLE, 200, 3, TradeAction.BUY);

        //when - 첫 번째 구매
        client.post()
                .uri("/customers/1/buy")
                .bodyValue(buyRequest1)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(e -> log.info("First Buy: {}", new String(e.getResponseBody())));

        //when - 두 번째 구매
        client.post()
                .uri("/customers/1/buy")
                .bodyValue(buyRequest2)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(e -> log.info("Second Buy: {}", new String(e.getResponseBody())));

        //when - 판매
        var sellRequest = new StockTradeRequest(Ticker.GOOGLE, 100, 2, TradeAction.SELL);
        client.post()
                .uri("/customers/1/sell")
                .bodyValue(sellRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(e -> log.info("Sell: {}", new String(e.getResponseBody())));

        //then
        client.get()
                .uri("/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(e -> log.info("Final Customer Info: {}", new String(e.getResponseBody())));
    }

    @Test
    @DisplayName("POST: buy - 같은 종목 여러 번 구매")
    void buySameStockMultipleTimes() {
        //given
        var buyRequest1 = new StockTradeRequest(Ticker.GOOGLE, 100, 5, TradeAction.BUY);
        var buyRequest2 = new StockTradeRequest(Ticker.GOOGLE, 100, 3, TradeAction.BUY);

        //when - 첫 번째 구매
        client.post()
                .uri("/customers/1/buy")
                .bodyValue(buyRequest1)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(e -> log.info("First Buy Same Stock: {}", new String(e.getResponseBody())));

        //when - 두 번째 구매 (같은 종목)
        client.post()
                .uri("/customers/1/buy")
                .bodyValue(buyRequest2)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(e -> log.info("Second Buy Same Stock: {}", new String(e.getResponseBody())));

        //then
        client.get()
                .uri("/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(e -> log.info("Customer Info After Multiple Buys: {}", new String(e.getResponseBody())));
    }

    @Test
    @DisplayName("GET: /customers - 존재하지 않는 고객")
    void getNonExistentCustomer() {
        //given
        Integer nonExistentCustomerId = 99999;

        //when & then
        client.get()
                .uri("/customers/" + nonExistentCustomerId)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(e -> log.info("Customer Not Found Error: {}", new String(e.getResponseBody())));
    }




}
