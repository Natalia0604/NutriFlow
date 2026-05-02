package com.nutriflow.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.nutriflow.config.OpenFoodFactsProperties;
import com.nutriflow.dto.response.FoodSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenFoodFactsClient {

    private final WebClient.Builder webClientBuilder;
    private final OpenFoodFactsProperties properties;

    @Cacheable(value = "food", key = "#query + ':' + #store")
    @Retryable(maxAttempts = 3)
    public List<FoodSearchResponse> search(String query, String store) {
        String url = properties.getBaseUrl() + "/cgi/search.pl?search_terms="
                + query + "&json=1&page_size=20&fields=product_name,brands,nutriments,serving_size,url";
        try {
            JsonNode root = webClientBuilder.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
            return parseProducts(root);
        } catch (Exception e) {
            log.warn("OpenFoodFacts search failed for query={}: {}", query, e.getMessage());
            return List.of();
        }
    }

    private List<FoodSearchResponse> parseProducts(JsonNode root) {
        List<FoodSearchResponse> results = new ArrayList<>();
        if (root == null || !root.has("products")) return results;
        for (JsonNode product : root.get("products")) {
            try {
                JsonNode n = product.get("nutriments");
                results.add(FoodSearchResponse.builder()
                        .itemName(text(product, "product_name"))
                        .storeName(text(product, "brands"))
                        .sizeLabel(text(product, "serving_size"))
                        .kcal(decimal(n, "energy-kcal_100g"))
                        .protein(decimal(n, "proteins_100g"))
                        .carbs(decimal(n, "carbohydrates_100g"))
                        .fat(decimal(n, "fat_100g"))
                        .sodium(decimal(n, "sodium_100g"))
                        .sugar(decimal(n, "sugars_100g"))
                        .dataUrl(text(product, "url"))
                        .source("openfoodfacts")
                        .build());
            } catch (Exception e) {
                log.debug("Skip malformed product: {}", e.getMessage());
            }
        }
        return results;
    }

    private String text(JsonNode node, String field) {
        return node != null && node.has(field) ? node.get(field).asText("") : "";
    }

    private java.math.BigDecimal decimal(JsonNode node, String field) {
        if (node == null || !node.has(field)) return java.math.BigDecimal.ZERO;
        try {
            return new java.math.BigDecimal(node.get(field).asText("0"));
        } catch (Exception e) {
            return java.math.BigDecimal.ZERO;
        }
    }
}
