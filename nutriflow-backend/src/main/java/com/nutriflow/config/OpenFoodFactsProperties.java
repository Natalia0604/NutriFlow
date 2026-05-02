package com.nutriflow.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "open-food-facts")
public class OpenFoodFactsProperties {
    private String baseUrl;
    private long cacheTtl;
}
