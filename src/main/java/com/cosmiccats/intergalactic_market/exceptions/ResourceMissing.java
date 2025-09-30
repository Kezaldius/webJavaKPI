package com.cosmiccats.intergalactic_market.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class ResourceMissing extends RuntimeException {
    public ResourceMissing(String message) {
        super(message);
    }
}