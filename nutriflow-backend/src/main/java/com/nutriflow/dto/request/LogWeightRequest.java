package com.nutriflow.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LogWeightRequest {
    @NotNull
    @DecimalMin("20.0") @DecimalMax("300.0")
    private BigDecimal weight;

    @NotNull
    private LocalDate recordDate;

    private String note;
}
