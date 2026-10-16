package com.xiangchuang.dto.response;

import lombok.Data;

/**
 * 用户信息响应DTO
 */
@Data
public class UserInfoResponse {

    private Integer userId;

    private String username;

    private String phone;

    private String email;

    private String avatar;

    private String userType;

}