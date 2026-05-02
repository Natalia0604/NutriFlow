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
public class WeekMenuResponse {
    private Long id;
    private LocalDate weekStart;
    private String aiComment;
    private String generatedBy;
    private List<MealCellResponse> cells;
}
