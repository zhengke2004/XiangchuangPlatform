package com.xiangchuang.controller.admin;

import com.xiangchuang.dto.request.AdminActivityRequest;
import com.xiangchuang.dto.request.AvailabilityRequest;
import com.xiangchuang.dto.response.ApiResponse;
import com.xiangchuang.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理端-活动管理接口
 */
@RestController
@RequestMapping("/api/admin/activities")
public class AdminActivityController {

    private final AdminService adminService;

    public AdminActivityController(AdminService adminService) {
        this.adminService = adminService;
    }

    /** 活动列表(分页) */
    @GetMapping
    public ApiResponse<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Boolean isActive) {
        return ApiResponse.success(adminService.listActivities(page, pageSize, keyword, type, isActive));
    }

    /** 新增活动 */
    @PostMapping
    public ApiResponse<Void> create(@Valid @RequestBody AdminActivityRequest request) {
        adminService.createActivity(request);
        return ApiResponse.success("创建成功", null);
    }

    /** 编辑活动 */
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Integer id, @Valid @RequestBody AdminActivityRequest request) {
        adminService.updateActivity(id, request);
        return ApiResponse.success("更新成功", null);
    }

    /** 上架/下架 */
    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Integer id, @RequestBody AdminActivityRequest request) {
        if (request.getIsActive() == null) {
            return ApiResponse.error("isActive不能为空");
        }
        adminService.updateActivityStatus(id, request.getIsActive());
        return ApiResponse.success("更新成功", null);
    }

    /** 删除活动 */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Integer id) {
        adminService.deleteActivity(id);
        return ApiResponse.success("删除成功", null);
    }

    /** 查询活动可用日期配置 */
    @GetMapping("/{id}/availability")
    public ApiResponse<List<Map<String, Object>>> availability(@PathVariable Integer id) {
        return ApiResponse.success("查询成功", adminService.listAvailability(id).stream()
                .map(a -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("availabilityId", a.getAvailabilityId());
                    map.put("availableDate", a.getAvailableDate());
                    map.put("availableSpots", a.getAvailableSpots());
                    map.put("priceAdjustment", a.getPriceAdjustment());
                    map.put("isAvailable", a.getIsAvailable());
                    return map;
                }).toList());
    }

    /** 新增可用日期配置 */
    @PostMapping("/{id}/availability")
    public ApiResponse<Void> addAvailability(@PathVariable Integer id, @Valid @RequestBody AvailabilityRequest request) {
        adminService.addAvailability(id, request);
        return ApiResponse.success("添加成功", null);
    }

    /** 删除可用日期配置 */
    @DeleteMapping("/{id}/availability/{availabilityId}")
    public ApiResponse<Void> deleteAvailability(@PathVariable Integer id, @PathVariable Integer availabilityId) {
        adminService.deleteAvailability(id, availabilityId);
        return ApiResponse.success("删除成功", null);
    }
}
