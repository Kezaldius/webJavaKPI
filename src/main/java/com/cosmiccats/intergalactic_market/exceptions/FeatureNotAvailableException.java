package com.cosmiccats.intergalactic_market.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class FeatureNotAvailableException extends RuntimeException {
    public FeatureNotAvailableException(String featureName) {
        super(String.format("Feature '%s' is not enabled.", featureName));
    }
}