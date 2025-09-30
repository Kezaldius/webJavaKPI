package com.cosmiccats.intergalactic_market.exceptions;

public class ResourceMissing extends RuntimeException {
    public ResourceMissing(String message) {
        super(message);
    }
}