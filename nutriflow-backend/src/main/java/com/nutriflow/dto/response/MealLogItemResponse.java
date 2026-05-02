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
public class MealLogItemResponse {
    private Long id;
    private String foodName;
    private BigDecimal kcal;
    private BigDecimal kcalRangeMin;
    private BigDecimal kcalRangeMax;
    private BigDecimal protein;
    private BigDecimal carbs;
    private BigDecimal fat;
    private BigDecimal sodium;
    private String dataSource;
    private BigDecimal confidence;
    private Boolean photoUsed;
    private String referenceType;
    private Integer sortOrder;
}
