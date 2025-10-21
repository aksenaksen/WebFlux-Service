package com.example.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.function.Consumer;

@RestControllerAdvice
public class CustomerExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleException(CustomerNotFoundException e) {
        return build(HttpStatus.NOT_FOUND, e, problem -> {
            problem.setTitle("Customer Not Found");
        });
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ProblemDetail handleException(InsufficientStockException e) {
        return build(HttpStatus.BAD_REQUEST, e, problem -> {
            problem.setTitle("Insufficient Stock");
        });
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ProblemDetail handleException(InsufficientBalanceException e) {
        return build(HttpStatus.BAD_REQUEST, e, problem -> {
            problem.setTitle("Insufficient Balance");
        });
    }

    private ProblemDetail build(HttpStatus status, Exception e, Consumer<ProblemDetail> consumer) {
        var problem = ProblemDetail.forStatusAndDetail(status, e.getMessage());
        consumer.accept(problem);
        return problem;
    }
}
