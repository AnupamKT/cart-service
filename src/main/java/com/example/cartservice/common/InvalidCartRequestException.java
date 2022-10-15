package com.example.cartservice.common;

public class InvalidCartRequestException extends Exception {
    public InvalidCartRequestException(String msg) {
        super(msg);
    }
}
