package com.nutriflow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealLogResponse {
    private Long id;
    private LocalDate mealDate;
    private Integer mealIndex;
    private String mealType;
    private Long mealCellId;
    private String storeName;
    private String note;
    private LocalDateTime loggedAt;
    private List<MealLogItemResponse> items;
    private Double totalKcal;
}
