package com.xiangchuang.config;

import com.xiangchuang.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 管理员鉴权拦截器
 * 校验 /api/admin/** 请求的JWT令牌与管理员角色
 */
@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public AdminInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            writeJson(response, 401, "未登录或令牌缺失");
            return false;
        }

        String token = auth.substring(7);
        Claims claims;
        try {
            claims = jwtUtil.parseToken(token);
        } catch (Exception e) {
            writeJson(response, 401, "令牌无效或已过期");
            return false;
        }

        String userType = claims.get("userType", String.class);
        if (!"管理员".equals(userType)) {
            writeJson(response, 403, "无管理员权限");
            return false;
        }

        request.setAttribute("adminUserId", claims.get("userId", Integer.class));
        return true;
    }

    private void writeJson(HttpServletResponse response, int code, String message) throws Exception {
        response.setStatus(code);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":" + code + ",\"message\":\"" + message + "\"}");
    }
}
