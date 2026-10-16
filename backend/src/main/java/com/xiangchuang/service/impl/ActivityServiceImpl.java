package com.xiangchuang.service.impl;

import com.xiangchuang.dto.request.ActivityQueryRequest;
import com.xiangchuang.dto.request.CalculateRequest;
import com.xiangchuang.dto.request.ReviewRequest;
import com.xiangchuang.dto.response.ActivityFilterResponse;
import com.xiangchuang.dto.response.ActivityListResponse;
import com.xiangchuang.dto.response.CostCalculationResponse;
import com.xiangchuang.dto.response.PaginationResponse;
import com.xiangchuang.dto.response.ReviewListResponse;
import com.xiangchuang.entity.Activity;
import com.xiangchuang.entity.ActivityAvailability;
import com.xiangchuang.entity.Review;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangchuang.mapper.ActivityAvailabilityMapper;
import com.xiangchuang.mapper.ActivityMapper;
import com.xiangchuang.mapper.ReviewMapper;
import com.xiangchuang.service.ActivityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 活动服务实现
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityMapper activityMapper;
    private final ActivityAvailabilityMapper availabilityMapper;
    private final ReviewMapper reviewMapper;

    public ActivityServiceImpl(ActivityMapper activityMapper, ActivityAvailabilityMapper availabilityMapper, ReviewMapper reviewMapper) {
        this.activityMapper = activityMapper;
        this.availabilityMapper = availabilityMapper;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public ActivityListResponse getActivities(ActivityQueryRequest request) {
        int pageSize = Math.max(1, Math.min(100, request.getPageSize()));
        int currentPage = Math.max(1, request.getPage());

        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Activity::getIsActive, true);

        if (request.getType() != null && !request.getType().isEmpty() && !"all".equals(request.getType())) {
            queryWrapper.eq(Activity::getActivityType, request.getType());
        }
        if (request.getTheme() != null && !request.getTheme().isEmpty() && !"all".equals(request.getTheme())) {
            queryWrapper.apply("FIND_IN_SET({0}, activity_theme)", request.getTheme());
        }
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            queryWrapper.and(wrapper -> wrapper
                    .like(Activity::getActivityName, request.getKeyword())
                    .or()
                    .like(Activity::getActivityDescription, request.getKeyword())
            );
        }
        if (request.getMinPrice() != null) {
            queryWrapper.ge(Activity::getActivityPrice, request.getMinPrice());
        }
        if (request.getMaxPrice() != null) {
            queryWrapper.le(Activity::getActivityPrice, request.getMaxPrice());
        }

        queryWrapper.orderByDesc(Activity::getActivityId);

        Page<Activity> page = new Page<>(currentPage, pageSize);
        Page<Activity> resultPage = activityMapper.selectPage(page, queryWrapper);

        ActivityFilterResponse filters = getFilterOptions();

        ActivityListResponse response = new ActivityListResponse();
        response.setActivities(resultPage.getRecords().stream().map(this::convertToMap).collect(Collectors.toList()));
        response.setPagination(PaginationResponse.of(request.getPage(), pageSize, resultPage.getTotal()));
        response.setFilters(filters);

        return response;
    }

    @Override
    public List<Object> getByTheme(String theme) {
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Activity::getIsActive, true);

        if (theme != null && !theme.isEmpty() && !"all".equals(theme)) {
            queryWrapper.apply("FIND_IN_SET({0}, activity_theme)", theme);
        }

        queryWrapper.orderByDesc(Activity::getActivityRating);

        if (theme == null || theme.isEmpty() || "all".equals(theme)) {
            queryWrapper.last("LIMIT 50");
        }

        List<Activity> activities = activityMapper.selectList(queryWrapper);
        return activities.stream().map(this::convertToMap).collect(Collectors.toList());
    }

    @Override
    public CostCalculationResponse calculateCost(CalculateRequest request) {
        if (request.getActivityIds() == null || request.getActivityIds().isEmpty()) {
            throw new RuntimeException("请至少选择一个活动");
        }

        List<Activity> activities = activityMapper.selectBatchIds(request.getActivityIds());

        BigDecimal activitiesCost = activities.stream()
                .map(Activity::getActivityPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> servicePrices = new HashMap<>();
        servicePrices.put("guide", BigDecimal.valueOf(200));
        servicePrices.put("transport", BigDecimal.valueOf(300));
        servicePrices.put("photo", BigDecimal.valueOf(500));
        servicePrices.put("insurance", BigDecimal.valueOf(30));

        BigDecimal servicesCost = BigDecimal.ZERO;
        if (request.getServices() != null) {
            if (Boolean.TRUE.equals(request.getServices().get("guide"))) {
                servicesCost = servicesCost.add(servicePrices.get("guide"));
            }
            if (Boolean.TRUE.equals(request.getServices().get("transport"))) {
                servicesCost = servicesCost.add(servicePrices.get("transport"));
            }
            if (Boolean.TRUE.equals(request.getServices().get("photo"))) {
                servicesCost = servicesCost.add(servicePrices.get("photo"));
            }
            if (Boolean.TRUE.equals(request.getServices().get("insurance"))) {
                servicesCost = servicesCost.add(servicePrices.get("insurance").multiply(BigDecimal.valueOf(request.getParticipants())));
            }
        }

        BigDecimal transportCost = BigDecimal.valueOf(120);

        CostCalculationResponse response = new CostCalculationResponse();
        response.setActivities(activities.stream().map(a -> {
            CostCalculationResponse.ActivityDetail detail = new CostCalculationResponse.ActivityDetail();
            detail.setId(a.getActivityId());
            detail.setName(a.getActivityName());
            detail.setPrice(a.getActivityPrice());
            return detail;
        }).collect(Collectors.toList()));
        response.setActivitiesCost(activitiesCost);
        response.setServicesCost(servicesCost);
        response.setTransportCost(transportCost);
        response.setTotal(activitiesCost.add(servicesCost).add(transportCost));
        response.setParticipants(request.getParticipants());

        return response;
    }

    @Override
    public List<Object> getRandomRecommendations(Integer limit) {
        int safeLimit = Math.max(1, Math.min(50, limit));
        List<Activity> activities = activityMapper.selectRandomRecommendations(safeLimit);
        return activities.stream().map(this::convertToMap).collect(Collectors.toList());
    }

    @Override
    public Object getActivityById(Integer id) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null || !activity.getIsActive()) {
            return null;
        }

        List<Activity> recommendations = activityMapper.selectRandomRecommendations(3);

        Map<String, Object> result = new HashMap<>();
        result.put("activity", convertToMap(activity));
        result.put("recommendations", recommendations.stream().map(this::convertToMap).collect(Collectors.toList()));

        return result;
    }

    @Override
    public Map<String, Object> checkAvailability(Integer activityId, LocalDate date) {
        ActivityAvailability availability = availabilityMapper.selectByActivityIdAndDate(activityId, date);

        Map<String, Object> result = new HashMap<>();
        result.put("activity_id", activityId);
        result.put("date", date);
        result.put("is_available", availability != null);
        result.put("available_spots", availability != null ? availability.getAvailableSpots() : 0);

        return result;
    }

    @Override
    public List<Object> getAvailableDates(Integer activityId) {
        List<ActivityAvailability> dates = availabilityMapper.selectAvailableDates(activityId);
        return dates.stream().map(a -> {
            Map<String, Object> map = new HashMap<>();
            map.put("available_date", a.getAvailableDate());
            map.put("available_spots", a.getAvailableSpots());
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public ReviewListResponse getReviews(Integer activityId, Integer page, Integer pageSize) {
        int safePageSize = Math.max(1, Math.min(100, pageSize));
        int offset = Math.max(0, (page - 1) * safePageSize);

        List<Review> reviews = reviewMapper.selectWithUserInfo("活动", activityId, safePageSize, offset);
        Long total = reviewMapper.countByItem("活动", activityId);

        ReviewListResponse response = new ReviewListResponse();
        response.setReviews(reviews.stream().map(this::convertToReviewItem).collect(Collectors.toList()));
        response.setPagination(PaginationResponse.of(page, safePageSize, total));

        return response;
    }

    @Override
    @Transactional
    public void addReview(Integer activityId, Integer userId, ReviewRequest request) {
        Review review = new Review();
        review.setUserId(userId);
        review.setItemType("活动");
        review.setItemId(activityId);
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setImages(request.getImages());

        reviewMapper.insert(review);
    }

    @Override
    public ActivityFilterResponse getFilterOptions() {
        ActivityFilterResponse response = new ActivityFilterResponse();
        response.setTypes(activityMapper.selectTypes());

        List<String> themeStrings = activityMapper.selectThemes();
        Set<String> themes = new HashSet<>();
        for (String themeStr : themeStrings) {
            if (themeStr != null) {
                Arrays.stream(themeStr.split(","))
                        .map(String::trim)
                        .forEach(themes::add);
            }
        }
        response.setThemes(new ArrayList<>(themes));

        return response;
    }

    private Map<String, Object> convertToMap(Activity activity) {
        Map<String, Object> map = new HashMap<>();
        map.put("activity_id", activity.getActivityId());
        map.put("activity_name", activity.getActivityName());
        map.put("activity_type", activity.getActivityType());
        map.put("activity_theme", activity.getActivityTheme());
        map.put("activity_location", activity.getActivityLocation());
        map.put("activity_address", activity.getActivityAddress());
        map.put("activity_duration", activity.getActivityDuration());
        map.put("activity_price", activity.getActivityPrice());
        map.put("activity_description", activity.getActivityDescription());
        map.put("activity_images", activity.getActivityImages());
        map.put("max_participants", activity.getMaxParticipants());
        map.put("current_booked", activity.getCurrentBooked());
        map.put("activity_rating", activity.getActivityRating());
        map.put("review_count", activity.getReviewCount());
        return map;
    }

    private ReviewListResponse.ReviewItem convertToReviewItem(Review review) {
        ReviewListResponse.ReviewItem item = new ReviewListResponse.ReviewItem();
        item.setReviewId(review.getReviewId());
        item.setUserId(review.getUserId());
        item.setRating(review.getRating());
        item.setContent(review.getContent());
        item.setImages(review.getImages());
        item.setCreatedAt(review.getCreatedAt());
        return item;
    }

}