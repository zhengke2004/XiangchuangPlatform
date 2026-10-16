package com.xiangchuang.service;

import com.xiangchuang.dto.request.OrderCreateRequest;

import java.util.List;
import java.util.Map;

/**
 * 用户订单服务接口
 */
public interface OrderService {

    /** 创建订单 */
    Map<String, Object> createOrder(Integer userId, OrderCreateRequest request);

    /** 查询当前用户订单列表(含明细) */
    List<Map<String, Object>> listMyOrders(Integer userId);
}
