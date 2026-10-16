package com.xiangchuang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单详情实体类
 */
@Data
@TableName("order_items")
public class OrderItem {

    @TableId(type = IdType.AUTO)
    private Integer itemId;

    private Integer orderId;

    private String itemType;

    private Integer itemIdRef;

    private String itemName;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal subtotal;

}