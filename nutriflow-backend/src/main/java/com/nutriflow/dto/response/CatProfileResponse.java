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
public class CatProfileResponse {
    private Long id;
    private String breed;
    private String name;
    private BigDecimal knucklePxRatio;
    private BigDecimal bowlWidthMm;
}
