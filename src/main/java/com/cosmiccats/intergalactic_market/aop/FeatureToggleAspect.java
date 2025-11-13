package com.cosmiccats.intergalactic_market.aop;

import com.cosmiccats.intergalactic_market.exceptions.FeatureNotAvailableException;
import com.cosmiccats.intergalactic_market.service.FeatureToggleService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class FeatureToggleAspect {

    private final FeatureToggleService featureToggleService;

    public FeatureToggleAspect(FeatureToggleService featureToggleService) {
        this.featureToggleService = featureToggleService;
    }

    @Around("@annotation(requiresFeatureToggle)")
    public Object checkFeatureToggle(ProceedingJoinPoint joinPoint, RequiresFeatureToggle requiresFeatureToggle) throws Throwable {
        String featureName = requiresFeatureToggle.value();

        log.info("Checking feature toggle for: '{}' on method: {}",
                featureName, joinPoint.getSignature().toShortString());

        if (featureToggleService.isEnabled(featureName)) {
            log.info("Feature '{}' is ENABLED. Proceeding with method execution.", featureName);
            return joinPoint.proceed();
        } else {
            log.warn("Feature '{}' is DISABLED. Blocking method execution.", featureName);
            throw new FeatureNotAvailableException(featureName);
        }
    }
}