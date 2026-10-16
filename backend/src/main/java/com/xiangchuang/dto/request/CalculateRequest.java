package com.xiangchuang.dto.request;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 费用计算请求DTO
 */
@Data
public class CalculateRequest {

    private List<Integer> activityIds;

    private Map<String, Boolean> services;

    private Integer participants = 1;

}