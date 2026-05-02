package com.nutriflow.service;

import com.nutriflow.dto.request.LoginRequest;
import com.nutriflow.dto.request.RegisterRequest;
import com.nutriflow.dto.response.AuthResponse;
import com.nutriflow.entity.User;
import com.nutriflow.exception.DuplicateEmailException;
import com.nutriflow.exception.InvalidCredentialsException;
import com.nutriflow.exception.TokenExpiredException;
import com.nutriflow.repository.UserRepository;
import com.nutriflow.security.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public AuthResponse register(RegisterRequest req, HttpServletResponse response) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateEmailException(req.getEmail());
        }
        User user = User.builder()
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .gender(req.getGender())
                .age(req.getAge())
                .weight(java.math.BigDecimal.valueOf(req.getWeight()))
                .height(java.math.BigDecimal.valueOf(req.getHeight()))
                .activityLevel(req.getActivityLevel())
                .hasWeightGoal(false)
                .isSetupComplete(false)
                .build();
        user = userRepository.save(user);

        String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        setRefreshTokenCookie(response, refreshToken);
        return buildAuthResponse(user, accessToken);
    }

    @Transactional
    public AuthResponse login(LoginRequest req, HttpServletResponse response) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        setRefreshTokenCookie(response, refreshToken);
        return buildAuthResponse(user, accessToken);
    }

    @Transactional
    public AuthResponse refreshToken(String refreshToken, HttpServletResponse response) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new TokenExpiredException();
        }
        Long userId = jwtProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(TokenExpiredException::new);
        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new TokenExpiredException();
        }
        String newAccessToken = jwtProvider.generateAccessToken(user.getId(), user.getEmail());
        String newRefreshToken = jwtProvider.generateRefreshToken(user.getId());
        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        setRefreshTokenCookie(response, newRefreshToken);
        return buildAuthResponse(user, newAccessToken);
    }

    @Transactional
    public void logout(Long userId, HttpServletResponse response) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setRefreshToken(null);
            userRepository.save(user);
        });
        Cookie cookie = new Cookie("refreshToken", "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth");
        response.addCookie(cookie);
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth");
        cookie.setMaxAge(7 * 24 * 3600);
        response.addCookie(cookie);
    }

    private AuthResponse buildAuthResponse(User user, String accessToken) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .isSetupComplete(Boolean.TRUE.equals(user.getIsSetupComplete()))
                .build();
    }
}
