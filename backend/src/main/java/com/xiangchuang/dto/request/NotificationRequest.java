package com.xiangchuang.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 资讯发布请求DTO
 */
@Data
public class NotificationRequest {

    @NotBlank(message = "资讯标题不能为空")
    private String title;

    @NotBlank(message = "资讯内容不能为空")
    private String content;

    /** 目标人群:全部/普通用户/农户/商家/管理员,默认全部 */
    private String targetType;
}
