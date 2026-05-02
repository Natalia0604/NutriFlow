package com.nutriflow.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCatRequest {
    @NotBlank
    private String breed;

    @NotBlank
    private String name;

    @DecimalMin("0.0001") @DecimalMax("9.9999")
    private Double knucklePxRatio;

    @DecimalMin("1.0") @DecimalMax("999.9")
    private Double bowlWidthMm;
}
