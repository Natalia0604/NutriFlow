package com.nutriflow.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddFoodPreferenceRequest {
    @NotBlank
    private String foodName;

    @NotBlank
    private String category;

    private Boolean isCustom = false;
    private Boolean isAllergy = false;
}
