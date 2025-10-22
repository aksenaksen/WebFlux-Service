package com.example.aggregator.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class AggregateExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleException(CustomerNotFoundException e) {
        return build(HttpStatus.NOT_FOUND, e, problem -> {
            problem.setTitle("Customer Not Found");
        });
    }

    @ExceptionHandler(InvalidTradeRequestException.class)
    public ProblemDetail handleException(InvalidTradeRequestException e){
        return build(HttpStatus.BAD_REQUEST, e, problem -> {
            problem.setTitle("Invalid Trade Request");
        });
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ProblemDetail handleException(WebExchangeBindException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> "field :" + error.getField() + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        if (errorMessage.isEmpty()) {
            errorMessage = "Validation failed";
        }

        String finalErrorMessage = errorMessage;
        return build(HttpStatus.BAD_REQUEST, ex, problem -> {
            problem.setDetail(finalErrorMessage);
            problem.setTitle("Not Matched Request Format");
        });
    }

    private ProblemDetail build(HttpStatus status, Exception e, Consumer<ProblemDetail> consumer) {
        var problem = ProblemDetail.forStatusAndDetail(status, e.getMessage());
        consumer.accept(problem);
        return problem;
    }
}
