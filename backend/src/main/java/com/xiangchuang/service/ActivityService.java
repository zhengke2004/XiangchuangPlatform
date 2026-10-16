package com.xiangchuang.service;

import com.xiangchuang.dto.request.ActivityQueryRequest;
import com.xiangchuang.dto.request.CalculateRequest;
import com.xiangchuang.dto.request.ReviewRequest;
import com.xiangchuang.dto.response.ActivityFilterResponse;
import com.xiangchuang.dto.response.ActivityListResponse;
import com.xiangchuang.dto.response.CostCalculationResponse;
import com.xiangchuang.dto.response.ReviewListResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 活动服务接口
 */
public interface ActivityService {

    ActivityListResponse getActivities(ActivityQueryRequest request);

    List<Object> getByTheme(String theme);

    CostCalculationResponse calculateCost(CalculateRequest request);

    List<Object> getRandomRecommendations(Integer limit);

    Object getActivityById(Integer id);

    Map<String, Object> checkAvailability(Integer activityId, LocalDate date);

    List<Object> getAvailableDates(Integer activityId);

    ReviewListResponse getReviews(Integer activityId, Integer page, Integer pageSize);

    void addReview(Integer activityId, Integer userId, ReviewRequest request);

    ActivityFilterResponse getFilterOptions();

}