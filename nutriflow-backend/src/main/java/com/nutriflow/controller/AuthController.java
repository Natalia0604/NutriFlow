package com.nutriflow.controller;

import com.nutriflow.dto.request.LoginRequest;
import com.nutriflow.dto.request.RegisterRequest;
import com.nutriflow.dto.response.ApiResponse;
import com.nutriflow.dto.response.AuthResponse;
import com.nutriflow.service.AuthService;
import com.nutriflow.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Tag(name = "Auth")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SecurityUtils securityUtils;

    @Operation(summary = "Register")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest req, HttpServletResponse response) {
        return ResponseEntity.ok(ApiResponse.ok(authService.register(req, response)));
    }

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest req, HttpServletResponse response) {
        return ResponseEntity.ok(ApiResponse.ok(authService.login(req, response)));
    }

    @Operation(summary = "Refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshCookie(request);
        return ResponseEntity.ok(ApiResponse.ok(authService.refreshToken(refreshToken, response)));
    }

    @Operation(summary = "Logout")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        authService.logout(securityUtils.getCurrentUserId(), response);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    private String extractRefreshCookie(HttpServletRequest request) {
        if (request.getCookies() == null) throw new com.nutriflow.exception.TokenExpiredException();
        return Arrays.stream(request.getCookies())
                .filter(c -> "refreshToken".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(com.nutriflow.exception.TokenExpiredException::new);
    }
}
