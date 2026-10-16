package com.xiangchuang.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 管理端活动请求DTO
 */
@Data
public class AdminActivityRequest {

    @NotBlank(message = "活动名称不能为空")
    private String activityName;

    private String activityType;

    private String activityTheme;

    private String activityLocation;

    private String activityAddress;

    private String activityDuration;

    @NotNull(message = "活动价格不能为空")
    private BigDecimal activityPrice;

    private String activityDescription;

    private String activityImages;

    private Integer maxParticipants;

    /** 上架状态(仅状态更新接口使用) */
    private Boolean isActive;
}
