package com.xiangchuang.dto.request;

import lombok.Data;

/**
 * 管理端订单状态更新请求DTO
 */
@Data
public class OrderStatusRequest {

    /** 订单状态:待处理/进行中/已完成/已取消 */
    private String orderStatus;

    /** 支付状态:待付款/已付款/已退款/支付失败 */
    private String paymentStatus;
}
