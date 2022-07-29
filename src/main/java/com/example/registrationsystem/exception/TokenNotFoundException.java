package com.example.registrationsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class TokenNotFoundException extends IllegalStateException{
    public TokenNotFoundException(String message) {
        super(message);
    }
}
