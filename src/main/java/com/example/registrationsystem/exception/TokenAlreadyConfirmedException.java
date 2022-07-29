package com.example.registrationsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class TokenAlreadyConfirmedException extends IllegalStateException{
    public TokenAlreadyConfirmedException(String message) {
        super(message);
    }
}
