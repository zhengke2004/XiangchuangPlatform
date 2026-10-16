package com.xiangchuang.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品查询请求DTO
 */
@Data
public class ProductQueryRequest {

    private Integer page = 1;

    private Integer pageSize = 12;

    private String category;

    private String area;

    private String season;

    private String keyword;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private String sortBy = "created_at";

    private String sortOrder = "desc";

}