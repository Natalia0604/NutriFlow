package com.nutriflow.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private ErrorDto error;

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder().success(true).data(data).build();
    }

    public static <T> ApiResponse<T> ok() {
        return ApiResponse.<T>builder().success(true).build();
    }

    public static <T> ApiResponse<T> fail(String code, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(new ErrorDto(code, message))
                .build();
    }
}
