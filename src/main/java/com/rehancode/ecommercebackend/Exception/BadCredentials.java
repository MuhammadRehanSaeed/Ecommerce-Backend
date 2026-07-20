package com.rehancode.ecommercebackend.Exception;

public class BadCredentials extends RuntimeException {
    public BadCredentials(String message) {
        super(message);
    }
}
