package com.xiangchuang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 */
@Data
@TableName("goods")
public class Goods {

    @TableId(type = IdType.AUTO)
    private Integer goodsId;

    private String goodsName;

    private String goodsArea;

    private String goodsCategory;

    private String goodsTime;

    private String goodsProducer;

    private String goodsPAddress;

    private String goodsIntroduce;

    private BigDecimal goodsPrice;

    private String goodsStandard;

    private BigDecimal goodsStars;

    private String goodsPicture;

    private String goodsPPicture;

    private Integer salesCount;

    private Integer stock;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}