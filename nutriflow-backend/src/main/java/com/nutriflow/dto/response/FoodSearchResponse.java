package com.nutriflow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodSearchResponse {
    private Long id;
    private String storeName;
    private String itemName;
    private String sizeLabel;
    private BigDecimal kcal;
    private BigDecimal protein;
    private BigDecimal carbs;
    private BigDecimal fat;
    private BigDecimal sodium;
    private BigDecimal sugar;
    private String dataUrl;
    private String source;
}
