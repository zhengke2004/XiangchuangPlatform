package com.xiangchuang.dto.response;

import lombok.Data;

/**
 * 登录响应DTO
 */
@Data
public class LoginResponse {

    private String token;

    private UserInfoResponse user;

}