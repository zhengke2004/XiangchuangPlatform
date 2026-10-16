package com.xiangchuang.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 用户下单请求
 */
@Data
public class OrderCreateRequest {

    /** 订单类型：商品 / 旅游 */
    private String orderType;

    /** 订单明细列表 */
    private List<OrderItemRequest> items;

    private String contactName;

    private String contactPhone;

    private String contactAddress;

    /** 旅游出行日期 */
    private LocalDate travelDate;

    private Integer participants;

    private String specialRequests;

    private String paymentMethod;

    @Data
    public static class OrderItemRequest {
        /** 商品ID或活动ID */
        private Integer itemIdRef;

        private String itemName;

        private Integer quantity;

        private BigDecimal price;
    }
}
