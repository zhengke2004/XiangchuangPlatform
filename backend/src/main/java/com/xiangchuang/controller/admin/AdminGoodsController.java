package com.xiangchuang.controller.admin;

import com.xiangchuang.dto.request.AdminGoodsRequest;
import com.xiangchuang.dto.response.ApiResponse;
import com.xiangchuang.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理端-商品管理接口
 */
@RestController
@RequestMapping("/api/admin/goods")
public class AdminGoodsController {

    private final AdminService adminService;

    public AdminGoodsController(AdminService adminService) {
        this.adminService = adminService;
    }

    /** 商品列表(分页,含未上架商品) */
    @GetMapping
    public ApiResponse<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean isActive) {
        return ApiResponse.success(adminService.listGoods(page, pageSize, keyword, category, isActive));
    }

    /** 新增商品 */
    @PostMapping
    public ApiResponse<Void> create(@Valid @RequestBody AdminGoodsRequest request) {
        adminService.createGoods(request);
        return ApiResponse.success("创建成功", null);
    }

    /** 编辑商品 */
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Integer id, @Valid @RequestBody AdminGoodsRequest request) {
        adminService.updateGoods(id, request);
        return ApiResponse.success("更新成功", null);
    }

    /** 上架/下架 */
    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Integer id, @RequestBody AdminGoodsRequest request) {
        if (request.getIsActive() == null) {
            return ApiResponse.error("isActive不能为空");
        }
        adminService.updateGoodsStatus(id, request.getIsActive());
        return ApiResponse.success("更新成功", null);
    }

    /** 删除商品 */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Integer id) {
        adminService.deleteGoods(id);
        return ApiResponse.success("删除成功", null);
    }
}
