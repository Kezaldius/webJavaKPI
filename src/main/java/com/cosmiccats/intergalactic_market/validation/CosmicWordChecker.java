package com.cosmiccats.intergalactic_market.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CosmicWordValidator.class) 
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CosmicWordChecker {
    String message() default "Product name must contain a cosmic word like star, galaxy, comet)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}