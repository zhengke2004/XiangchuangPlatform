package com.xiangchuang.controller.admin;

import com.xiangchuang.dto.response.ApiResponse;
import com.xiangchuang.service.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 管理端-数据统计接口
 */
@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatsController {

    private final AdminService adminService;

    public AdminStatsController(AdminService adminService) {
        this.adminService = adminService;
    }

    /** 平台数据总览 */
    @GetMapping
    public ApiResponse<Map<String, Object>> stats() {
        return ApiResponse.success(adminService.getStats());
    }
}
