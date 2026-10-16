package com.xiangchuang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 活动实体类
 */
@Data
@TableName("activities")
public class Activity {

    @TableId(type = IdType.AUTO)
    private Integer activityId;

    private String activityName;

    private String activityType;

    private String activityTheme;

    private String activityLocation;

    private String activityAddress;

    private String activityDuration;

    private BigDecimal activityPrice;

    private String activityDescription;

    private String activityImages;

    private Integer maxParticipants;

    private Integer currentBooked;

    private BigDecimal activityRating;

    private Integer reviewCount;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}