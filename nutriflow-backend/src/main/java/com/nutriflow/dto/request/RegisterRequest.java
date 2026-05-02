package com.nutriflow.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 8)
    private String password;

    @NotBlank
    private String gender;

    @NotNull @Min(10) @Max(120)
    private Integer age;

    @NotNull @DecimalMin("20.0") @DecimalMax("300.0")
    private Double weight;

    @NotNull @DecimalMin("100.0") @DecimalMax("250.0")
    private Double height;

    @NotBlank
    private String activityLevel;
}
