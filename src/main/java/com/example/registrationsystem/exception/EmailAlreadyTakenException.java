package com.example.registrationsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailAlreadyTakenException extends IllegalArgumentException{
    public EmailAlreadyTakenException(String message) {
        super(message);
    }
}
