package com.example.aggregator.exception;

public class InvalidTradeRequestException extends RuntimeException{

    public InvalidTradeRequestException(String message){
        super(message);
    }
}
