package com.nutriflow.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutriflow.config.AiProperties;
import com.nutriflow.exception.AiServiceException;
import com.nutriflow.exception.PhotoAnalysisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiProxyService {

    private final WebClient.Builder webClientBuilder;
    private final AiProperties aiProperties;
    private final ObjectMapper objectMapper;

    @Retryable(maxAttempts = 2, backoff = @org.springframework.retry.annotation.Backoff(delay = 5000))
    public String chat(String systemPrompt, String userMessage) {
        try {
            String url = aiProperties.getBaseUrl()
                    + "/models/" + aiProperties.getModel()
                    + ":generateContent?key=" + aiProperties.getApiKey();

            Map<String, Object> body = Map.of(
                    "systemInstruction", Map.of(
                            "parts", List.of(Map.of("text", systemPrompt))
                    ),
                    "contents", List.of(
                            Map.of("role", "user",
                                    "parts", List.of(Map.of("text", userMessage)))
                    ),
                    "generationConfig", Map.of(
                            "maxOutputTokens", aiProperties.getMaxTokens()
                    )
            );

            JsonNode response = webClientBuilder.build()
                    .post()
                    .uri(url)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            return extractText(response);
        } catch (AiServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Gemini chat failed: {}", e.getMessage(), e);
            throw new AiServiceException("AI 服務呼叫失敗", e);
        }
    }

    @Retryable(maxAttempts = 2)
    public String analyzePhoto(String imageBase64, String mediaType, String prompt) {
        try {
            String url = aiProperties.getBaseUrl()
                    + "/models/" + aiProperties.getModel()
                    + ":generateContent?key=" + aiProperties.getApiKey();

            Map<String, Object> imagePart = Map.of(
                    "inlineData", Map.of(
                            "mimeType", mediaType,
                            "data", imageBase64
                    )
            );
            Map<String, Object> textPart = Map.of("text", prompt);

            Map<String, Object> body = Map.of(
                    "contents", List.of(
                            Map.of("role", "user",
                                    "parts", List.of(imagePart, textPart))
                    ),
                    "generationConfig", Map.of(
                            "maxOutputTokens", aiProperties.getMaxTokens()
                    )
            );

            JsonNode response = webClientBuilder.build()
                    .post()
                    .uri(url)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            return extractText(response);
        } catch (PhotoAnalysisException e) {
            throw e;
        } catch (Exception e) {
            log.error("Gemini photo analysis failed: {}", e.getMessage(), e);
            throw new PhotoAnalysisException("照片分析失敗: " + e.getMessage());
        }
    }

    private String extractText(JsonNode response) {
        if (response == null) throw new AiServiceException("Gemini 回應為空");
        // Gemini response: candidates[0].content.parts[0].text
        JsonNode text = response
                .path("candidates").path(0)
                .path("content").path("parts").path(0)
                .path("text");
        if (!text.isMissingNode()) return text.asText();
        // 錯誤訊息
        JsonNode error = response.path("error").path("message");
        if (!error.isMissingNode()) throw new AiServiceException("Gemini 錯誤: " + error.asText());
        throw new AiServiceException("Gemini 回應格式異常");
    }
}
