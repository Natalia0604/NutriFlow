package com.nutriflow.controller;

import com.nutriflow.dto.request.LogMealRequest;
import com.nutriflow.dto.response.ApiResponse;
import com.nutriflow.dto.response.MealLogResponse;
import com.nutriflow.dto.response.PageResponse;
import com.nutriflow.service.MealService;
import com.nutriflow.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Meal")
@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MealController {

    private final MealService mealService;
    private final SecurityUtils securityUtils;

    @Operation(summary = "Get daily meal logs")
    @GetMapping("/daily")
    public ResponseEntity<ApiResponse<List<MealLogResponse>>> getDaily(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.ok(mealService.getDailyLogs(securityUtils.getCurrentUserId(), date)));
    }

    @Operation(summary = "Get meal log history")
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<PageResponse<MealLogResponse>>> getHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(mealService.getLogHistory(securityUtils.getCurrentUserId(), page, size)));
    }

    @Operation(summary = "Log a meal")
    @PostMapping
    public ResponseEntity<ApiResponse<MealLogResponse>> logMeal(@Valid @RequestBody LogMealRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(mealService.logMeal(securityUtils.getCurrentUserId(), req)));
    }

    @Operation(summary = "Delete meal log")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLog(@PathVariable Long id) {
        mealService.deleteLog(securityUtils.getCurrentUserId(), id);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
