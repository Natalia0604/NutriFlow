package com.nutriflow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealCellResponse {
    private Long id;
    private Integer dayIndex;
    private Integer mealIndex;
    private String mealType;
    private Boolean logged;
    private LocalTime logTime;
    private String note;
    private String source;
    private List<MealItemResponse> items;
    private Double totalKcal;
}
