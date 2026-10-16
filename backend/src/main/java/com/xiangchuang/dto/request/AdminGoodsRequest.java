package com.xiangchuang.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 管理端商品请求DTO
 */
@Data
public class AdminGoodsRequest {

    @NotBlank(message = "商品名称不能为空")
    private String goodsName;

    private String goodsArea;

    private String goodsCategory;

    private String goodsTime;

    private String goodsProducer;

    private String goodsPAddress;

    private String goodsIntroduce;

    @NotNull(message = "商品价格不能为空")
    private BigDecimal goodsPrice;

    private String goodsStandard;

    private String goodsPicture;

    private String goodsPPicture;

    private Integer stock;

    private Boolean isActive;
}
