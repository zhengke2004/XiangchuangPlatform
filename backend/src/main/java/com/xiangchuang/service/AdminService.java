package com.xiangchuang.service;

import com.xiangchuang.dto.request.*;
import com.xiangchuang.entity.ActivityAvailability;

import java.util.List;
import java.util.Map;

/**
 * 管理端服务接口
 */
public interface AdminService {

    // ===== 用户管理 =====
    Map<String, Object> listUsers(Integer page, Integer pageSize, String keyword, String userType);
    void updateUserStatus(Integer userId, Boolean isActive);
    void updateUserType(Integer userId, String userType);

    // ===== 商品管理 =====
    Map<String, Object> listGoods(Integer page, Integer pageSize, String keyword, String category, Boolean isActive);
    void createGoods(AdminGoodsRequest request);
    void updateGoods(Integer goodsId, AdminGoodsRequest request);
    void updateGoodsStatus(Integer goodsId, Boolean isActive);
    void deleteGoods(Integer goodsId);

    // ===== 活动管理 =====
    Map<String, Object> listActivities(Integer page, Integer pageSize, String keyword, String type, Boolean isActive);
    void createActivity(AdminActivityRequest request);
    void updateActivity(Integer activityId, AdminActivityRequest request);
    void updateActivityStatus(Integer activityId, Boolean isActive);
    void deleteActivity(Integer activityId);
    List<ActivityAvailability> listAvailability(Integer activityId);
    void addAvailability(Integer activityId, AvailabilityRequest request);
    void deleteAvailability(Integer activityId, Integer availabilityId);

    // ===== 订单管理 =====
    Map<String, Object> listOrders(Integer page, Integer pageSize, String orderStatus, String paymentStatus,
                                   String orderType, String keyword);
    Map<String, Object> getOrderDetail(Integer orderId);
    void updateOrderStatus(Integer orderId, OrderStatusRequest request);

    // ===== 评价管理 =====
    Map<String, Object> listReviews(Integer page, Integer pageSize, String itemType, Integer rating);
    void deleteReview(Integer reviewId);

    // ===== 资讯管理 =====
    Map<String, Object> listNews(Integer page, Integer pageSize);
    int createNews(NotificationRequest request, Integer adminUserId);
    void deleteNews(String title, String content);

    // ===== 数据统计 =====
    Map<String, Object> getStats();
}
