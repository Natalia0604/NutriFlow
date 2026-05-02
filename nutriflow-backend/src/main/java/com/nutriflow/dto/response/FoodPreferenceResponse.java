package com.nutriflow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodPreferenceResponse {
    private Long id;
    private String foodName;
    private String category;
    private Boolean isCustom;
    private Boolean isAllergy;
}
