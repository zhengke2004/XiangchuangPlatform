package com.xiangchuang.controller;

import com.xiangchuang.dto.request.LoginRequest;
import com.xiangchuang.dto.request.RegisterRequest;
import com.xiangchuang.dto.response.ApiResponse;
import com.xiangchuang.dto.response.LoginResponse;
import com.xiangchuang.dto.response.UserInfoResponse;
import com.xiangchuang.service.AuthService;
import com.xiangchuang.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ApiResponse.success("登录成功", response);
        } catch (RuntimeException e) {
            return ApiResponse.error(401, e.getMessage());
        }
    }

    @PostMapping("/register")
    public ApiResponse<UserInfoResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            UserInfoResponse response = authService.register(request);
            return ApiResponse.success("注册成功", response);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("已注册")) {
                return ApiResponse.error(409, e.getMessage());
            }
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token != null && token.startsWith("Bearer ")) {
            authService.logout(token.substring(7));
        }
        return ApiResponse.success("登出成功", null);
    }

    @GetMapping("/me")
    public ApiResponse<UserInfoResponse> getMe(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ApiResponse.error(401, "未登录");
        }

        try {
            Integer userId = jwtUtil.getUserIdFromToken(token.substring(7));
            UserInfoResponse response = authService.getUserInfo(userId);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(401, "未登录");
        }
    }

    @GetMapping("/check")
    public ApiResponse<Map<String, Object>> checkLogin(@RequestHeader(value = "Authorization", required = false) String token) {
        Map<String, Object> result = new HashMap<>();

        if (token != null && token.startsWith("Bearer ")) {
            try {
                String jwtToken = token.substring(7);
                if (!jwtUtil.isTokenExpired(jwtToken)) {
                    Integer userId = jwtUtil.getUserIdFromToken(jwtToken);
                    UserInfoResponse user = authService.getUserInfo(userId);
                    result.put("isLogin", true);
                    result.put("user", user);
                    return ApiResponse.success(result);
                }
            } catch (Exception e) {
                // token无效
            }
        }

        result.put("isLogin", false);
        result.put("user", null);
        return ApiResponse.success(result);
    }

}