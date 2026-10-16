package com.xiangchuang.controller.admin;

import com.xiangchuang.dto.response.ApiResponse;
import com.xiangchuang.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理端-内容管理接口(评价 + 资讯)
 */
@RestController
@RequestMapping("/api/admin")
public class AdminContentController {

    private final AdminService adminService;

    public AdminContentController(AdminService adminService) {
        this.adminService = adminService;
    }

    /** 评价列表(分页) */
    @GetMapping("/reviews")
    public ApiResponse<Map<String, Object>> reviews(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String itemType,
            @RequestParam(required = false) Integer rating) {
        return ApiResponse.success(adminService.listReviews(page, pageSize, itemType, rating));
    }

    /** 删除违规评价 */
    @DeleteMapping("/reviews/{id}")
    public ApiResponse<Void> deleteReview(@PathVariable Integer id) {
        adminService.deleteReview(id);
        return ApiResponse.success("删除成功", null);
    }

    /** 资讯列表(分页,按标题+内容分组) */
    @GetMapping("/news")
    public ApiResponse<Map<String, Object>> news(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ApiResponse.success(adminService.listNews(page, pageSize));
    }

    /** 发布资讯(推送系统通知) */
    @PostMapping("/news")
    public ApiResponse<Integer> publish(@RequestBody Map<String, String> body,
                                        @RequestAttribute("adminUserId") Integer adminUserId) {
        com.xiangchuang.dto.request.NotificationRequest request = new com.xiangchuang.dto.request.NotificationRequest();
        request.setTitle(body.get("title"));
        request.setContent(body.get("content"));
        request.setTargetType(body.get("targetType"));
        int count = adminService.createNews(request, adminUserId);
        return ApiResponse.success("发布成功,已通知" + count + "位用户", count);
    }

    /** 删除资讯(按标题+内容) */
    @DeleteMapping("/news")
    public ApiResponse<Void> deleteNews(@RequestParam String title, @RequestParam String content) {
        adminService.deleteNews(title, content);
        return ApiResponse.success("删除成功", null);
    }
}
