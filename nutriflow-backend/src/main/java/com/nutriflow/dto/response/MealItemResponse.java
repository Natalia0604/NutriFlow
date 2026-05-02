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
public class MealItemResponse {
    private Long id;
    private String foodName;
    private BigDecimal kcal;
    private BigDecimal protein;
    private BigDecimal carbs;
    private BigDecimal fat;
    private BigDecimal sodium;
    private String dataSource;
    private BigDecimal confidence;
    private Integer sortOrder;
}
