package com.xiangchuang.controller;

import com.xiangchuang.dto.request.ActivityQueryRequest;
import com.xiangchuang.dto.request.CalculateRequest;
import com.xiangchuang.dto.request.ReviewRequest;
import com.xiangchuang.dto.response.ApiResponse;
import com.xiangchuang.dto.response.ActivityFilterResponse;
import com.xiangchuang.dto.response.ActivityListResponse;
import com.xiangchuang.dto.response.CostCalculationResponse;
import com.xiangchuang.dto.response.ReviewListResponse;
import com.xiangchuang.service.ActivityService;
import com.xiangchuang.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 活动控制器
 */
@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;
    private final JwtUtil jwtUtil;

    public ActivityController(ActivityService activityService, JwtUtil jwtUtil) {
        this.activityService = activityService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ApiResponse<ActivityListResponse> getActivities(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String theme,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        ActivityQueryRequest request = new ActivityQueryRequest();
        request.setPage(page != null ? page : 1);
        request.setPageSize(pageSize != null ? pageSize : 20);
        request.setType(type);
        request.setTheme(theme);
        request.setKeyword(keyword);
        request.setMinPrice(minPrice);
        request.setMaxPrice(maxPrice);

        try {
            ActivityListResponse response = activityService.getActivities(request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取活动列表失败");
        }
    }

    @GetMapping("/theme/{theme}")
    public ApiResponse<List<Object>> getByTheme(@PathVariable String theme) {
        try {
            List<Object> activities = activityService.getByTheme(theme);
            return ApiResponse.success(activities);
        } catch (Exception e) {
            return ApiResponse.error(500, "筛选失败");
        }
    }

    @PostMapping("/calculate")
    public ApiResponse<CostCalculationResponse> calculateCost(@RequestBody CalculateRequest request) {
        try {
            CostCalculationResponse response = activityService.calculateCost(request);
            return ApiResponse.success(response);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(500, "费用计算失败");
        }
    }

    @GetMapping("/recommendations")
    public ApiResponse<List<Object>> getRecommendations(@RequestParam(defaultValue = "3") Integer limit) {
        try {
            List<Object> recommendations = activityService.getRandomRecommendations(limit);
            return ApiResponse.success(recommendations);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取推荐失败");
        }
    }

    @GetMapping("/{id}/availability")
    public ApiResponse<Map<String, Object>> checkAvailability(
            @PathVariable Integer id,
            @RequestParam String date) {

        try {
            LocalDate localDate = LocalDate.parse(date);
            Map<String, Object> result = activityService.checkAvailability(id, localDate);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(500, "检查可用性失败");
        }
    }

    @GetMapping("/{id}/available-dates")
    public ApiResponse<List<Object>> getAvailableDates(@PathVariable Integer id) {
        try {
            List<Object> dates = activityService.getAvailableDates(id);
            return ApiResponse.success(dates);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取可用日期失败");
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<Object> getActivityById(@PathVariable Integer id) {
        try {
            Object activity = activityService.getActivityById(id);
            if (activity == null) {
                return ApiResponse.error(404, "活动不存在");
            }
            return ApiResponse.success(activity);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取活动详情失败");
        }
    }

    @GetMapping("/{id}/reviews")
    public ApiResponse<ReviewListResponse> getReviews(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        try {
            ReviewListResponse response = activityService.getReviews(id, page, pageSize);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取评价失败");
        }
    }

    @PostMapping("/{id}/reviews")
    public ApiResponse<Void> addReview(
            @PathVariable Integer id,
            @Valid @RequestBody ReviewRequest request,
            @RequestHeader(value = "Authorization", required = false) String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            return ApiResponse.error(401, "请先登录");
        }

        try {
            Integer userId = jwtUtil.getUserIdFromToken(token.substring(7));
            activityService.addReview(id, userId, request);
            return ApiResponse.success("评价成功", null);
        } catch (Exception e) {
            return ApiResponse.error(500, "添加评价失败");
        }
    }

}