package com.xiangchuang.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 费用计算响应DTO
 */
@Data
public class CostCalculationResponse {

    private List<ActivityDetail> activities;

    private BigDecimal activitiesCost;

    private BigDecimal servicesCost;

    private BigDecimal transportCost;

    private BigDecimal total;

    private Integer participants;

    @Data
    public static class ActivityDetail {
        private Integer id;
        private String name;
        private BigDecimal price;
    }

}