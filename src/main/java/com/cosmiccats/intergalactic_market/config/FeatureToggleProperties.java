package com.cosmiccats.intergalactic_market.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
@ConfigurationProperties(prefix = "feature")
@Data
public class FeatureToggleProperties {

    private Map<String, Boolean> toggles = new HashMap<>();

}