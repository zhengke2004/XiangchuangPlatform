package com.xiangchuang.service.impl;

import com.xiangchuang.dto.request.LoginRequest;
import com.xiangchuang.dto.request.RegisterRequest;
import com.xiangchuang.dto.response.LoginResponse;
import com.xiangchuang.dto.response.UserInfoResponse;
import com.xiangchuang.entity.User;
import com.xiangchuang.mapper.UserMapper;
import com.xiangchuang.service.AuthService;
import com.xiangchuang.util.JwtUtil;
import com.xiangchuang.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 认证服务实现
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserMapper userMapper, PasswordUtil passwordUtil, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.passwordUtil = passwordUtil;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.selectByPhone(request.getPhone());

        if (user == null) {
            throw new RuntimeException("手机号未注册");
        }

        if (!passwordUtil.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        String token = jwtUtil.generateToken(user.getUserId(), user.getPhone(), user.getUsername(), user.getUserType());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUser(toUserInfoResponse(user));

        return response;
    }

    @Override
    @Transactional
    public UserInfoResponse register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("两次输入的密码不一致");
        }

        User existingUser = userMapper.selectByPhone(request.getPhone());
        if (existingUser != null) {
            throw new RuntimeException("该手机号已注册");
        }

        String hashedPassword = passwordUtil.encode(request.getPassword());
        String finalUsername = request.getUsername() != null && !request.getUsername().isEmpty()
                ? request.getUsername()
                : "用户" + request.getPhone().substring(request.getPhone().length() - 4);

        User user = new User();
        user.setPhone(request.getPhone());
        user.setPassword(hashedPassword);
        user.setUsername(finalUsername);
        user.setEmail(request.getEmail());
        user.setUserType("普通用户");
        user.setAvatar("/images/avatars/default.jpg");
        user.setIsActive(true);

        userMapper.insert(user);

        return toUserInfoResponse(user);
    }

    @Override
    public void logout(String token) {
    }

    @Override
    public UserInfoResponse getUserInfo(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return toUserInfoResponse(user);
    }

    @Override
    public boolean checkLogin(String token) {
        try {
            return !jwtUtil.isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private UserInfoResponse toUserInfoResponse(User user) {
        UserInfoResponse response = new UserInfoResponse();
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setPhone(user.getPhone());
        response.setEmail(user.getEmail());
        response.setAvatar(user.getAvatar());
        response.setUserType(user.getUserType());
        return response;
    }

}