package com.xiangchuang.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 商品筛选选项响应DTO
 */
@Data
public class ProductFilterResponse {

    private List<String> categories;

    private List<String> areas;

    private List<String> seasons;

}