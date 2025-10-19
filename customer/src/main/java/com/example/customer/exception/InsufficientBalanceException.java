package com.example.customer.exception;

public class InsufficientBalanceException extends RuntimeException {

    private static final String MESSAGE = "고객 [id = %d]의 잔액이 부족합니다.";

    public InsufficientBalanceException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}
