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
public class UserProfileResponse {
    private Long id;
    private String email;
    private String gender;
    private Integer age;
    private BigDecimal weight;
    private BigDecimal height;
    private String activityLevel;
    private Boolean hasWeightGoal;
    private BigDecimal targetWeight;
    private Integer goalWeeks;
    private Boolean isSetupComplete;
    private Double tdee;
    private Double targetCalories;
}
