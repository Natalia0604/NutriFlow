package com.nutriflow.controller;

import com.nutriflow.dto.request.AddFoodPreferenceRequest;
import com.nutriflow.dto.request.UpdateCatRequest;
import com.nutriflow.dto.request.UpdateUserRequest;
import com.nutriflow.dto.response.*;
import com.nutriflow.service.FoodService;
import com.nutriflow.service.UserService;
import com.nutriflow.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User")
@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final FoodService foodService;
    private final SecurityUtils securityUtils;

    @Operation(summary = "Get profile")
    @GetMapping
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile() {
        return ResponseEntity.ok(ApiResponse.ok(userService.getProfile(securityUtils.getCurrentUserId())));
    }

    @Operation(summary = "Update profile")
    @PatchMapping
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(@Valid @RequestBody UpdateUserRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(userService.updateProfile(securityUtils.getCurrentUserId(), req)));
    }

    @Operation(summary = "Get cat profile")
    @GetMapping("/cat")
    public ResponseEntity<ApiResponse<CatProfileResponse>> getCat() {
        return ResponseEntity.ok(ApiResponse.ok(userService.getCatProfile(securityUtils.getCurrentUserId())));
    }

    @Operation(summary = "Update cat profile")
    @PutMapping("/cat")
    public ResponseEntity<ApiResponse<CatProfileResponse>> updateCat(@Valid @RequestBody UpdateCatRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(userService.updateCatProfile(securityUtils.getCurrentUserId(), req)));
    }

    @Operation(summary = "Get food preferences")
    @GetMapping("/preferences")
    public ResponseEntity<ApiResponse<List<FoodPreferenceResponse>>> getPreferences() {
        return ResponseEntity.ok(ApiResponse.ok(foodService.getPreferences(securityUtils.getCurrentUserId())));
    }

    @Operation(summary = "Add food preference")
    @PostMapping("/preferences")
    public ResponseEntity<ApiResponse<FoodPreferenceResponse>> addPreference(
            @Valid @RequestBody AddFoodPreferenceRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(foodService.addPreference(securityUtils.getCurrentUserId(), req)));
    }

    @Operation(summary = "Sync food preferences (replace all)")
    @PutMapping("/preferences")
    public ResponseEntity<ApiResponse<List<FoodPreferenceResponse>>> syncPreferences(
            @RequestBody List<AddFoodPreferenceRequest> requests) {
        return ResponseEntity.ok(ApiResponse.ok(
                foodService.syncPreferences(securityUtils.getCurrentUserId(), requests)));
    }

    @Operation(summary = "Delete food preference")
    @DeleteMapping("/preferences/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePreference(@PathVariable Long id) {
        foodService.deletePreference(securityUtils.getCurrentUserId(), id);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
