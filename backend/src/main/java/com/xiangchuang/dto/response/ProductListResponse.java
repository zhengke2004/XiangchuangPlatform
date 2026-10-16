package com.xiangchuang.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 商品列表响应DTO
 */
@Data
public class ProductListResponse {

    private List<Object> products;

    private PaginationResponse pagination;

    private ProductFilterResponse filters;

}