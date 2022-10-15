package com.example.cartservice.common;

import com.example.cartservice.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CartServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = InvalidCartRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleInvalidCartRequestException(InvalidCartRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse(400, ex.getMessage());
        return ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler(value = CartServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleInvalidCartServiceException(CartServiceException ex) {
        ErrorResponse errorResponse = new ErrorResponse(500, ex.getMessage());
        return ResponseEntity.status(500).body(errorResponse);
    }
}
