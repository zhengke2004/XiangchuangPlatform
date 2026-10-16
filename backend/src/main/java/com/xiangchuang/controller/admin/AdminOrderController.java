package com.xiangchuang.controller.admin;

import com.xiangchuang.dto.request.OrderStatusRequest;
import com.xiangchuang.dto.response.ApiResponse;
import com.xiangchuang.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理端-订单管理接口
 */
@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final AdminService adminService;

    public AdminOrderController(AdminService adminService) {
        this.adminService = adminService;
    }

    /** 订单列表(分页,支持订单状态/支付状态/类型/关键字筛选) */
    @GetMapping
    public ApiResponse<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String orderStatus,
            @RequestParam(required = false) String paymentStatus,
            @RequestParam(required = false) String orderType,
            @RequestParam(required = false) String keyword) {
        return ApiResponse.success(adminService.listOrders(page, pageSize, orderStatus, paymentStatus, orderType, keyword));
    }

    /** 订单详情(含明细) */
    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> detail(@PathVariable Integer id) {
        return ApiResponse.success(adminService.getOrderDetail(id));
    }

    /** 更新订单状态 */
    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Integer id, @Valid @RequestBody OrderStatusRequest request) {
        adminService.updateOrderStatus(id, request);
        return ApiResponse.success("更新成功", null);
    }
}
