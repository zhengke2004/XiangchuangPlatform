package com.xiangchuang.service;

import com.xiangchuang.dto.request.LoginRequest;
import com.xiangchuang.dto.request.RegisterRequest;
import com.xiangchuang.dto.response.LoginResponse;
import com.xiangchuang.dto.response.UserInfoResponse;

/**
 * 认证服务接口
 */
public interface AuthService {

    LoginResponse login(LoginRequest request);

    UserInfoResponse register(RegisterRequest request);

    void logout(String token);

    UserInfoResponse getUserInfo(Integer userId);

    boolean checkLogin(String token);

}