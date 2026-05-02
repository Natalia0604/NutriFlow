package com.nutriflow.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class LogMealRequest {
    @NotNull
    private LocalDate mealDate;

    @NotNull
    private Integer mealIndex;

    private String mealType;
    private Long mealCellId;
    private String storeName;
    private String note;
    private List<MealLogItemDto> items;
}
