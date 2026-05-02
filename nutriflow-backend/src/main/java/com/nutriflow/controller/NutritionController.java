package com.nutriflow.controller;

import com.nutriflow.dto.response.ApiResponse;
import com.nutriflow.dto.response.DailyNutritionResponse;
import com.nutriflow.dto.response.WeeklyNutritionResponse;
import com.nutriflow.service.NutritionService;
import com.nutriflow.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "Nutrition")
@RestController
@RequestMapping("/api/nutrition")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class NutritionController {

    private final NutritionService nutritionService;
    private final SecurityUtils securityUtils;

    @Operation(summary = "Get daily nutrition summary")
    @GetMapping("/daily")
    public ResponseEntity<ApiResponse<DailyNutritionResponse>> getDaily(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate target = date != null ? date : LocalDate.now();
        return ResponseEntity.ok(ApiResponse.ok(nutritionService.getDailyNutrition(securityUtils.getCurrentUserId(), target)));
    }

    @Operation(summary = "Get weekly nutrition summary")
    @GetMapping("/weekly")
    public ResponseEntity<ApiResponse<WeeklyNutritionResponse>> getWeekly(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        return ResponseEntity.ok(ApiResponse.ok(nutritionService.getWeeklyNutrition(securityUtils.getCurrentUserId(), weekStart)));
    }
}
