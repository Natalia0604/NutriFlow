package com.nutriflow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyNutritionResponse {
    private LocalDate date;
    private Double totalKcal;
    private Double totalProtein;
    private Double totalCarbs;
    private Double totalFat;
    private Double totalSodium;
    private Double targetKcal;
    private Double kcalProgress;
    private String advice;
}
