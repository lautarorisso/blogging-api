package com.lautarorisso.blogging_platform_api.exception;

public class JwtVerificationException extends RuntimeException {
    public JwtVerificationException(String message) {
        super(message);
    }
}
