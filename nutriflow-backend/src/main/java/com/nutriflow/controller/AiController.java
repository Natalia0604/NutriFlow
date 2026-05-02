package com.nutriflow.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutriflow.dto.request.AnalyzePhotoRequest;
import com.nutriflow.dto.response.ApiResponse;
import com.nutriflow.exception.PhotoAnalysisException;
import com.nutriflow.service.AiProxyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "AI")
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AiController {

    private final AiProxyService aiProxyService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "Analyze food photo")
    @PostMapping("/analyze-photo")
    public ResponseEntity<ApiResponse<JsonNode>> analyzePhoto(@Valid @RequestBody AnalyzePhotoRequest req) {
        String prompt = """
                請分析這張食物照片，辨識所有可見食物，並以 JSON 格式回傳營養資訊：
                {"items":[{"foodName":"食物名","kcal":0,"protein":0,"carbs":0,"fat":0,"sodium":0,"confidence":0.9,"referenceType":"photo"}]}
                %s只回傳 JSON，不要其他文字。
                """.formatted(req.getStoreName() != null ? "店家：" + req.getStoreName() + "\n" : "");

        String aiResponse = aiProxyService.analyzePhoto(req.getImageBase64(), req.getMediaType(), prompt);
        try {
            JsonNode result = objectMapper.readTree(aiResponse);
            return ResponseEntity.ok(ApiResponse.ok(result));
        } catch (Exception e) {
            log.error("Failed to parse photo analysis response: {}", e.getMessage());
            throw new PhotoAnalysisException("照片分析結果格式錯誤");
        }
    }
}
