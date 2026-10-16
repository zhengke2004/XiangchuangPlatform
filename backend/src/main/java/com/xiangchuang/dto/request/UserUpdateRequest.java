package com.xiangchuang.dto.request;

import lombok.Data;

/**
 * 管理端用户更新请求DTO(状态/角色)
 */
@Data
public class UserUpdateRequest {

    /** 是否启用 */
    private Boolean isActive;

    /** 用户角色:普通用户/农户/商家/管理员 */
    private String userType;
}
