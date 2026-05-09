package com.appsdevelloperblog.reactive.ws.users.infrastructure;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    public Mono<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException exception){
        return Mono.just(ErrorResponse.builder(exception, HttpStatus.CONFLICT, exception.getMessage() ).build());
    }

    @ExceptionHandler(Exception.class)
    public Mono<ErrorResponse> handleGeneralException(Exception exception){
        return Mono.just(ErrorResponse.builder(exception, HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage() ).build());
    }
}
