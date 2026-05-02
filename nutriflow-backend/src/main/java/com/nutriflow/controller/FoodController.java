package com.nutriflow.controller;

import com.nutriflow.dto.request.SaveCustomFoodRequest;
import com.nutriflow.dto.response.ApiResponse;
import com.nutriflow.dto.response.CustomFoodResponse;
import com.nutriflow.dto.response.FoodSearchResponse;
import com.nutriflow.service.FoodService;
import com.nutriflow.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Food")
@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class FoodController {

    private final FoodService foodService;
    private final SecurityUtils securityUtils;

    @Operation(summary = "Search food nutrition (official + personal library)")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<FoodSearchResponse>>> search(
            @RequestParam String query,
            @RequestParam(required = false) String store) {
        Long userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.ok(foodService.search(query, store, userId)));
    }

    // ── Custom food library ───────────────────────────────────────────────

    @Operation(summary = "List my custom foods")
    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<List<CustomFoodResponse>>> listCustomFoods() {
        return ResponseEntity.ok(ApiResponse.ok(foodService.getCustomFoods(securityUtils.getCurrentUserId())));
    }

    @Operation(summary = "Create a custom food")
    @PostMapping("/mine")
    public ResponseEntity<ApiResponse<CustomFoodResponse>> createCustomFood(
            @Valid @RequestBody SaveCustomFoodRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(
                foodService.createCustomFood(securityUtils.getCurrentUserId(), req)));
    }

    @Operation(summary = "Update a custom food")
    @PutMapping("/mine/{id}")
    public ResponseEntity<ApiResponse<CustomFoodResponse>> updateCustomFood(
            @PathVariable Long id,
            @Valid @RequestBody SaveCustomFoodRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(
                foodService.updateCustomFood(securityUtils.getCurrentUserId(), id, req)));
    }

    @Operation(summary = "Delete a custom food")
    @DeleteMapping("/mine/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomFood(@PathVariable Long id) {
        foodService.deleteCustomFood(securityUtils.getCurrentUserId(), id);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
