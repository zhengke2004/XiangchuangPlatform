package com.xiangchuang.controller.admin;

import com.xiangchuang.dto.request.UserUpdateRequest;
import com.xiangchuang.dto.response.ApiResponse;
import com.xiangchuang.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理端-用户管理接口
 */
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminService adminService;

    public AdminUserController(AdminService adminService) {
        this.adminService = adminService;
    }

    /** 用户列表(分页,支持关键字/角色筛选) */
    @GetMapping
    public ApiResponse<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String userType) {
        return ApiResponse.success(adminService.listUsers(page, pageSize, keyword, userType));
    }

    /** 更新用户状态(启用/停用) */
    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Integer id, @Valid @RequestBody UserUpdateRequest request) {
        if (request.getIsActive() == null) {
            return ApiResponse.error("isActive不能为空");
        }
        adminService.updateUserStatus(id, request.getIsActive());
        return ApiResponse.success("更新成功", null);
    }

    /** 调整用户角色 */
    @PutMapping("/{id}/type")
    public ApiResponse<Void> updateType(@PathVariable Integer id, @Valid @RequestBody UserUpdateRequest request) {
        adminService.updateUserType(id, request.getUserType());
        return ApiResponse.success("更新成功", null);
    }
}
