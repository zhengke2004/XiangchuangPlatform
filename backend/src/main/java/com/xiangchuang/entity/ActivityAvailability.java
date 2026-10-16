package com.xiangchuang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 活动可用日期实体类
 */
@Data
@TableName("activity_availability")
public class ActivityAvailability {

    @TableId(type = IdType.AUTO)
    private Integer availabilityId;

    private Integer activityId;

    private LocalDate availableDate;

    private Integer availableSpots;

    private BigDecimal priceAdjustment;

    private Boolean isAvailable;

}