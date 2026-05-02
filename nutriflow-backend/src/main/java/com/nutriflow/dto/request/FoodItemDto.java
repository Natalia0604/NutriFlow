package com.nutriflow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FoodItemDto {
    @NotBlank
    private String foodName;

    @NotNull
    private BigDecimal kcal;

    private BigDecimal protein;
    private BigDecimal carbs;
    private BigDecimal fat;
    private BigDecimal sodium;
    private String dataSource;
    private Integer sortOrder;
}
