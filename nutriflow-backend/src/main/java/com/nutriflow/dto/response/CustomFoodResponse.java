package com.nutriflow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomFoodResponse {
    private Long id;
    private String foodName;
    private String sizeLabel;
    private BigDecimal kcal;
    private BigDecimal protein;
    private BigDecimal carbs;
    private BigDecimal fat;
    private BigDecimal sodium;
    private String note;
    private LocalDateTime createdAt;
}
