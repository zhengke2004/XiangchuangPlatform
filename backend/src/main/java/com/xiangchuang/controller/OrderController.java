package com.xiangchuang.controller;

import com.xiangchuang.dto.request.OrderCreateRequest;
import com.xiangchuang.dto.response.ApiResponse;
import com.xiangchuang.service.OrderService;
import com.xiangchuang.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户订单控制器
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    public OrderController(OrderService orderService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.jwtUtil = jwtUtil;
    }

    /** 创建订单(下单) */
    @PostMapping
    public ApiResponse<Map<String, Object>> create(
            @RequestBody OrderCreateRequest request,
            @RequestHeader(value = "Authorization", required = false) String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            return ApiResponse.error(401, "请先登录");
        }

        try {
            Integer userId = jwtUtil.getUserIdFromToken(token.substring(7));
            Map<String, Object> result = orderService.createOrder(userId, request);
            return ApiResponse.success("下单成功", result);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(500, "下单失败");
        }
    }

    /** 我的订单列表 */
    @GetMapping
    public ApiResponse<List<Map<String, Object>>> list(
            @RequestHeader(value = "Authorization", required = false) String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            return ApiResponse.error(401, "请先登录");
        }

        try {
            Integer userId = jwtUtil.getUserIdFromToken(token.substring(7));
            List<Map<String, Object>> orders = orderService.listMyOrders(userId);
            return ApiResponse.success(orders);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取订单失败");
        }
    }
}
