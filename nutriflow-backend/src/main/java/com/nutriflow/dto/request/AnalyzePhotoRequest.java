package com.nutriflow.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnalyzePhotoRequest {
    @NotBlank
    private String imageBase64;

    private String mediaType = "image/jpeg";
    private String storeName;
    private String note;
}
