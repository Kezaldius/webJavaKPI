package com.cosmiccats.intergalactic_market.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CosmicWordValidatorTest {

    private final CosmicWordValidator validator = new CosmicWordValidator();

    @ParameterizedTest
    @ValueSource(strings = {"A new star is born", "Across the galaxy", "The fastest comet"})
    void isValid_WhenContainsCosmicWord_ShouldReturnTrue(String validValue) {
        assertTrue(validator.isValid(validValue, null));
    }

    @Test
    void isValid_WhenDoesNotContainCosmicWord_ShouldReturnFalse() {
        assertFalse(validator.isValid("A normal everyday object", null));
    }

    @Test
    void isValid_WhenValueIsNull_ShouldReturnTrue() {
        assertTrue(validator.isValid(null, null));
    }

    @Test
    void isValid_WhenValueIsEmpty_ShouldReturnTrue() {
        assertTrue(validator.isValid("", null));
    }
}
