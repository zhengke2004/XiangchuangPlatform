package com.xiangchuang.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 活动可用日期请求DTO
 */
@Data
public class AvailabilityRequest {

    @NotNull(message = "可用日期不能为空")
    private LocalDate availableDate;

    @NotNull(message = "可预订名额不能为空")
    private Integer availableSpots;

    /** 价格调整(正数加价、负数减价),可为0 */
    private BigDecimal priceAdjustment;
}
