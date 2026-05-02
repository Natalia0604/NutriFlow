package com.nutriflow.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateBlankMenuRequest {
    @NotNull
    private LocalDate weekStart;
}
