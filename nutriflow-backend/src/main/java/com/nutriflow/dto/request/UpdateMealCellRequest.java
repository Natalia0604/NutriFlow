package com.nutriflow.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class UpdateMealCellRequest {
    @NotNull
    private Integer dayIndex;

    @NotNull
    private Integer mealIndex;

    private String mealType;
    private LocalTime logTime;
    private String note;
    private List<FoodItemDto> items;
}
