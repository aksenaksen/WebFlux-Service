package com.example.customer.exception;

public class InsufficientStockException extends RuntimeException {

    private static final String MESSAGE = "고객 [id = %d]이 주문한 주식의 재고가 부족합니다.";

    public InsufficientStockException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}
