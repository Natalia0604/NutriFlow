package com.nutriflow.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaveCustomFoodRequest {

    @NotBlank
    private String foodName;

    private String sizeLabel;

    @NotNull
    @DecimalMin("0")
    private BigDecimal kcal;

    private BigDecimal protein;
    private BigDecimal carbs;
    private BigDecimal fat;
    private BigDecimal sodium;
    private String note;
}
