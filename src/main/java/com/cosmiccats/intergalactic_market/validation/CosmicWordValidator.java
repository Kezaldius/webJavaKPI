package com.cosmiccats.intergalactic_market.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class CosmicWordValidator implements ConstraintValidator<CosmicWordChecker, String> {
    private static final List<String> COSMIC_WORDS = Arrays.asList("star", "galaxy", "comet");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        return COSMIC_WORDS.stream().anyMatch(word -> value.toLowerCase().contains(word));
    }
}
