package com.nutriflow.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Min(10) @Max(120)
    private Integer age;

    @DecimalMin("20.0") @DecimalMax("300.0")
    private Double weight;

    @DecimalMin("100.0") @DecimalMax("250.0")
    private Double height;

    private String activityLevel;
    private Boolean hasWeightGoal;

    @DecimalMin("20.0") @DecimalMax("300.0")
    private Double targetWeight;

    @Min(1) @Max(104)
    private Integer goalWeeks;

    private Boolean isSetupComplete;
}
