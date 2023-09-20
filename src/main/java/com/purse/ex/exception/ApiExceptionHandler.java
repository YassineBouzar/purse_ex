package com.purse.ex.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Date;

@Slf4j
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ApiExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiExceptionMessage> handleRuntimeException(BadRequestException e){
        ApiExceptionMessage errorMessages = new ApiExceptionMessage(
               e.getMessage(), HttpStatus.BAD_REQUEST, Date.from(Instant.now()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMessages);
    }

}