package com.nutriflow.controller;

import com.nutriflow.dto.request.LogWeightRequest;
import com.nutriflow.dto.response.ApiResponse;
import com.nutriflow.dto.response.WeightRecordResponse;
import com.nutriflow.service.WeightService;
import com.nutriflow.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Weight")
@RestController
@RequestMapping("/api/weights")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class WeightController {

    private final WeightService weightService;
    private final SecurityUtils securityUtils;

    @Operation(summary = "Get weight history")
    @GetMapping
    public ResponseEntity<ApiResponse<List<WeightRecordResponse>>> getHistory(
            @RequestParam(defaultValue = "30") int limit) {
        return ResponseEntity.ok(ApiResponse.ok(weightService.getHistory(securityUtils.getCurrentUserId(), limit)));
    }

    @Operation(summary = "Log weight")
    @PostMapping
    public ResponseEntity<ApiResponse<WeightRecordResponse>> logWeight(@Valid @RequestBody LogWeightRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(weightService.logWeight(securityUtils.getCurrentUserId(), req)));
    }

    @Operation(summary = "Delete weight record")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        weightService.deleteRecord(securityUtils.getCurrentUserId(), id);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
