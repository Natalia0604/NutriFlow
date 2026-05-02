package com.nutriflow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyNutritionResponse {
    private LocalDate weekStart;
    private LocalDate weekEnd;
    private List<DailyNutritionResponse> days;
    private Double avgKcal;
    private Double avgProtein;
    private Double avgCarbs;
    private Double avgFat;
    private Double targetKcal;
}
