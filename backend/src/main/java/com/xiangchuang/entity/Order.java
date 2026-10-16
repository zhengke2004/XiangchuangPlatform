package com.xiangchuang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@TableName("orders")
public class Order {

    @TableId(type = IdType.AUTO)
    private Integer orderId;

    private String orderNo;

    private Integer userId;

    private String orderType;

    private BigDecimal totalAmount;

    private String paymentMethod;

    private String paymentStatus;

    private String orderStatus;

    private String contactName;

    private String contactPhone;

    private String contactAddress;

    private LocalDate travelDate;

    private Integer participants;

    private String specialRequests;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}