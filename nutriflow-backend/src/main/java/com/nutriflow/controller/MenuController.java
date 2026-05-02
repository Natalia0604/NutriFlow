package com.nutriflow.controller;

import com.nutriflow.dto.request.CreateBlankMenuRequest;
import com.nutriflow.dto.request.MenuGenerateRequest;
import com.nutriflow.dto.request.UpdateMealCellRequest;
import com.nutriflow.dto.response.ApiResponse;
import com.nutriflow.dto.response.MealCellResponse;
import com.nutriflow.dto.response.WeekMenuResponse;
import com.nutriflow.service.MenuService;
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

@Tag(name = "Menu")
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MenuController {

    private final MenuService menuService;
    private final SecurityUtils securityUtils;

    @Operation(summary = "Get week menu")
    @GetMapping
    public ResponseEntity<ApiResponse<WeekMenuResponse>> getWeekMenu(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        return ResponseEntity.ok(ApiResponse.ok(menuService.getWeekMenu(securityUtils.getCurrentUserId(), weekStart)));
    }

    @Operation(summary = "Generate week menu via AI")
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<WeekMenuResponse>> generate(@Valid @RequestBody MenuGenerateRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(menuService.generateMenu(securityUtils.getCurrentUserId(), req)));
    }

    @Operation(summary = "Create a blank menu (no AI)")
    @PostMapping("/blank")
    public ResponseEntity<ApiResponse<WeekMenuResponse>> createBlank(
            @Valid @RequestBody CreateBlankMenuRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(
                menuService.createBlankMenu(securityUtils.getCurrentUserId(), req)));
    }

    @Operation(summary = "Update meal cell")
    @PutMapping("/{menuId}/cells")
    public ResponseEntity<ApiResponse<MealCellResponse>> updateCell(
            @PathVariable Long menuId, @Valid @RequestBody UpdateMealCellRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(
                menuService.updateMealCell(securityUtils.getCurrentUserId(), menuId, req)));
    }
}
