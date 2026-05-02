package com.nutriflow.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MenuGenerateRequest {
    @NotNull
    private LocalDate weekStart;

    private String extraNote;
    private Boolean forceRegenerate = false;
}
