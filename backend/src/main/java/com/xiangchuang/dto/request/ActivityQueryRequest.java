package com.xiangchuang.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 活动查询请求DTO
 */
@Data
public class ActivityQueryRequest {

    private Integer page = 1;

    private Integer pageSize = 20;

    private String type;

    private String theme;

    private String keyword;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

}