package com.cosmiccats.intergalactic_market.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PersistenceException extends RuntimeException {
    public PersistenceException(Throwable cause) {
        super("A database error occurred while processing the request", cause);
    }
}