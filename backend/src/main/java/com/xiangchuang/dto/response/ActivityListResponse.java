package com.xiangchuang.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 活动列表响应DTO
 */
@Data
public class ActivityListResponse {

    private List<Object> activities;

    private PaginationResponse pagination;

    private ActivityFilterResponse filters;

}