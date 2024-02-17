package com.example.restservice.ErrorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UEntityHandler extends RuntimeException {
    public UEntityHandler (String message) {
        super(message);
    }
}
