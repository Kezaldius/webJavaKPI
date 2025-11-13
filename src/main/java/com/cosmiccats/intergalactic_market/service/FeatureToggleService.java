package com.cosmiccats.intergalactic_market.service;

import com.cosmiccats.intergalactic_market.config.FeatureToggleProperties;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FeatureToggleService {

    private final Map<String, Boolean> toggles;
    public FeatureToggleService(FeatureToggleProperties properties) {
        this.toggles = properties.getToggles();
    }

    public boolean isEnabled(String featureName) {
        return toggles.getOrDefault(featureName, false);
    }
}

