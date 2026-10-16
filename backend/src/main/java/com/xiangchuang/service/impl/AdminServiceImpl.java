package com.xiangchuang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangchuang.dto.request.*;
import com.xiangchuang.entity.*;
import com.xiangchuang.mapper.*;
import com.xiangchuang.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理端服务实现
 */
@Service
public class AdminServiceImpl implements AdminService {

    private final UserMapper userMapper;
    private final GoodsMapper goodsMapper;
    private final ActivityMapper activityMapper;
    private final ActivityAvailabilityMapper availabilityMapper;
    private final OrderMapper orderMapper;
    private final ReviewMapper reviewMapper;
    private final NotificationMapper notificationMapper;

    public AdminServiceImpl(UserMapper userMapper, GoodsMapper goodsMapper,
                            ActivityMapper activityMapper, ActivityAvailabilityMapper availabilityMapper,
                            OrderMapper orderMapper, ReviewMapper reviewMapper,
                            NotificationMapper notificationMapper) {
        this.userMapper = userMapper;
        this.goodsMapper = goodsMapper;
        this.activityMapper = activityMapper;
        this.availabilityMapper = availabilityMapper;
        this.orderMapper = orderMapper;
        this.reviewMapper = reviewMapper;
        this.notificationMapper = notificationMapper;
    }

    // ==================== 用户管理 ====================

    @Override
    public Map<String, Object> listUsers(Integer page, Integer pageSize, String keyword, String userType) {
        Page<User> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        if (hasText(userType)) {
            qw.eq(User::getUserType, userType);
        }
        if (hasText(keyword)) {
            qw.and(w -> w.like(User::getUsername, keyword).or().like(User::getPhone, keyword));
        }
        qw.orderByDesc(User::getCreatedAt);
        Page<User> result = userMapper.selectPage(p, qw);
        result.getRecords().forEach(u -> u.setPassword(null));
        return pageResult(result);
    }

    @Override
    public void updateUserStatus(Integer userId, Boolean isActive) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        User update = new User();
        update.setUserId(userId);
        update.setIsActive(isActive);
        update.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(update);
    }

    @Override
    public void updateUserType(Integer userId, String userType) {
        if (!List.of("普通用户", "农户", "商家", "管理员").contains(userType)) {
            throw new RuntimeException("无效的用户角色");
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        User update = new User();
        update.setUserId(userId);
        update.setUserType(userType);
        update.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(update);
    }

    // ==================== 商品管理 ====================

    @Override
    public Map<String, Object> listGoods(Integer page, Integer pageSize, String keyword, String category, Boolean isActive) {
        Page<Goods> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Goods> qw = new LambdaQueryWrapper<>();
        qw.like(hasText(keyword), Goods::getGoodsName, keyword);
        qw.eq(hasText(category), Goods::getGoodsCategory, category);
        qw.eq(isActive != null, Goods::getIsActive, isActive);
        qw.orderByDesc(Goods::getCreatedAt);
        Page<Goods> result = goodsMapper.selectPage(p, qw);
        return pageResult(result);
    }

    @Override
    public void createGoods(AdminGoodsRequest request) {
        Goods goods = new Goods();
        applyGoods(goods, request);
        goods.setGoodsStars(new BigDecimal("4.0"));
        goods.setSalesCount(0);
        if (goods.getStock() == null) {
            goods.setStock(100);
        }
        if (goods.getIsActive() == null) {
            goods.setIsActive(true);
        }
        goods.setCreatedAt(LocalDateTime.now());
        goods.setUpdatedAt(LocalDateTime.now());
        goodsMapper.insert(goods);
    }

    @Override
    public void updateGoods(Integer goodsId, AdminGoodsRequest request) {
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) {
            throw new RuntimeException("商品不存在");
        }
        applyGoods(goods, request);
        goods.setUpdatedAt(LocalDateTime.now());
        goodsMapper.updateById(goods);
    }

    @Override
    public void updateGoodsStatus(Integer goodsId, Boolean isActive) {
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) {
            throw new RuntimeException("商品不存在");
        }
        Goods update = new Goods();
        update.setGoodsId(goodsId);
        update.setIsActive(isActive);
        update.setUpdatedAt(LocalDateTime.now());
        goodsMapper.updateById(update);
    }

    @Override
    public void deleteGoods(Integer goodsId) {
        if (goodsMapper.selectById(goodsId) == null) {
            throw new RuntimeException("商品不存在");
        }
        goodsMapper.deleteById(goodsId);
    }

    private void applyGoods(Goods goods, AdminGoodsRequest request) {
        goods.setGoodsName(request.getGoodsName());
        goods.setGoodsArea(request.getGoodsArea());
        goods.setGoodsCategory(request.getGoodsCategory());
        goods.setGoodsTime(request.getGoodsTime());
        goods.setGoodsProducer(request.getGoodsProducer());
        goods.setGoodsPAddress(request.getGoodsPAddress());
        goods.setGoodsIntroduce(request.getGoodsIntroduce());
        goods.setGoodsPrice(request.getGoodsPrice());
        goods.setGoodsStandard(request.getGoodsStandard());
        goods.setGoodsPicture(request.getGoodsPicture());
        goods.setGoodsPPicture(request.getGoodsPPicture());
        goods.setStock(request.getStock());
        if (request.getIsActive() != null) {
            goods.setIsActive(request.getIsActive());
        }
    }

    // ==================== 活动管理 ====================

    @Override
    public Map<String, Object> listActivities(Integer page, Integer pageSize, String keyword, String type, Boolean isActive) {
        Page<Activity> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Activity> qw = new LambdaQueryWrapper<>();
        qw.like(hasText(keyword), Activity::getActivityName, keyword);
        qw.eq(hasText(type), Activity::getActivityType, type);
        qw.eq(isActive != null, Activity::getIsActive, isActive);
        qw.orderByDesc(Activity::getCreatedAt);
        Page<Activity> result = activityMapper.selectPage(p, qw);
        return pageResult(result);
    }

    @Override
    public void createActivity(AdminActivityRequest request) {
        Activity activity = new Activity();
        applyActivity(activity, request);
        activity.setCurrentBooked(0);
        activity.setActivityRating(new BigDecimal("4.5"));
        activity.setReviewCount(0);
        if (activity.getIsActive() == null) {
            activity.setIsActive(true);
        }
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());
        activityMapper.insert(activity);
    }

    @Override
    public void updateActivity(Integer activityId, AdminActivityRequest request) {
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new RuntimeException("活动不存在");
        }
        applyActivity(activity, request);
        activity.setUpdatedAt(LocalDateTime.now());
        activityMapper.updateById(activity);
    }

    @Override
    public void updateActivityStatus(Integer activityId, Boolean isActive) {
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new RuntimeException("活动不存在");
        }
        Activity update = new Activity();
        update.setActivityId(activityId);
        update.setIsActive(isActive);
        update.setUpdatedAt(LocalDateTime.now());
        activityMapper.updateById(update);
    }

    @Override
    public void deleteActivity(Integer activityId) {
        if (activityMapper.selectById(activityId) == null) {
            throw new RuntimeException("活动不存在");
        }
        // 同步删除可用日期配置
        availabilityMapper.delete(new LambdaQueryWrapper<ActivityAvailability>()
                .eq(ActivityAvailability::getActivityId, activityId));
        activityMapper.deleteById(activityId);
    }

    @Override
    public List<ActivityAvailability> listAvailability(Integer activityId) {
        return availabilityMapper.selectList(new LambdaQueryWrapper<ActivityAvailability>()
                .eq(ActivityAvailability::getActivityId, activityId)
                .orderByAsc(ActivityAvailability::getAvailableDate));
    }

    @Override
    public void addAvailability(Integer activityId, AvailabilityRequest request) {
        if (activityMapper.selectById(activityId) == null) {
            throw new RuntimeException("活动不存在");
        }
        Long exists = availabilityMapper.selectCount(new LambdaQueryWrapper<ActivityAvailability>()
                .eq(ActivityAvailability::getActivityId, activityId)
                .eq(ActivityAvailability::getAvailableDate, request.getAvailableDate()));
        if (exists > 0) {
            throw new RuntimeException("该日期的可用名额配置已存在");
        }
        ActivityAvailability availability = new ActivityAvailability();
        availability.setActivityId(activityId);
        availability.setAvailableDate(request.getAvailableDate());
        availability.setAvailableSpots(request.getAvailableSpots());
        availability.setPriceAdjustment(request.getPriceAdjustment() == null ? BigDecimal.ZERO : request.getPriceAdjustment());
        availability.setIsAvailable(true);
        availabilityMapper.insert(availability);
    }

    @Override
    public void deleteAvailability(Integer activityId, Integer availabilityId) {
        availabilityMapper.delete(new LambdaQueryWrapper<ActivityAvailability>()
                .eq(ActivityAvailability::getActivityId, activityId)
                .eq(ActivityAvailability::getAvailabilityId, availabilityId));
    }

    private void applyActivity(Activity activity, AdminActivityRequest request) {
        activity.setActivityName(request.getActivityName());
        activity.setActivityType(request.getActivityType());
        activity.setActivityTheme(request.getActivityTheme());
        activity.setActivityLocation(request.getActivityLocation());
        activity.setActivityAddress(request.getActivityAddress());
        activity.setActivityDuration(request.getActivityDuration());
        activity.setActivityPrice(request.getActivityPrice());
        activity.setActivityDescription(request.getActivityDescription());
        activity.setActivityImages(request.getActivityImages());
        activity.setMaxParticipants(request.getMaxParticipants());
    }

    // ==================== 订单管理 ====================

    @Override
    public Map<String, Object> listOrders(Integer page, Integer pageSize, String orderStatus, String paymentStatus,
                                          String orderType, String keyword) {
        Page<Order> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Order> qw = new LambdaQueryWrapper<>();
        qw.eq(hasText(orderStatus), Order::getOrderStatus, orderStatus);
        qw.eq(hasText(paymentStatus), Order::getPaymentStatus, paymentStatus);
        qw.eq(hasText(orderType), Order::getOrderType, orderType);
        if (hasText(keyword)) {
            qw.and(w -> w.like(Order::getOrderNo, keyword)
                    .or().like(Order::getContactName, keyword)
                    .or().like(Order::getContactPhone, keyword));
        }
        qw.orderByDesc(Order::getCreatedAt);
        Page<Order> result = orderMapper.selectPage(p, qw);
        return pageResult(result);
    }

    @Override
    public Map<String, Object> getOrderDetail(Integer orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("order", order);
        data.put("items", orderMapper.selectItemsByOrderId(orderId));
        return data;
    }

    @Override
    public void updateOrderStatus(Integer orderId, OrderStatusRequest request) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (hasText(request.getOrderStatus())
                && !List.of("待处理", "进行中", "已完成", "已取消").contains(request.getOrderStatus())) {
            throw new RuntimeException("无效的订单状态");
        }
        if (hasText(request.getPaymentStatus())
                && !List.of("待付款", "已付款", "已退款", "支付失败").contains(request.getPaymentStatus())) {
            throw new RuntimeException("无效的支付状态");
        }
        Order update = new Order();
        update.setOrderId(orderId);
        if (hasText(request.getOrderStatus())) {
            update.setOrderStatus(request.getOrderStatus());
        }
        if (hasText(request.getPaymentStatus())) {
            update.setPaymentStatus(request.getPaymentStatus());
        }
        update.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(update);
    }

    // ==================== 评价管理 ====================

    @Override
    public Map<String, Object> listReviews(Integer page, Integer pageSize, String itemType, Integer rating) {
        Page<Review> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Review> qw = new LambdaQueryWrapper<>();
        qw.eq(hasText(itemType), Review::getItemType, itemType);
        qw.eq(rating != null, Review::getRating, rating);
        qw.orderByDesc(Review::getCreatedAt);
        Page<Review> result = reviewMapper.selectPage(p, qw);
        return pageResult(result);
    }

    @Override
    public void deleteReview(Integer reviewId) {
        if (reviewMapper.selectById(reviewId) == null) {
            throw new RuntimeException("评价不存在");
        }
        reviewMapper.deleteById(reviewId);
    }

    // ==================== 资讯管理 ====================

    @Override
    public Map<String, Object> listNews(Integer page, Integer pageSize) {
        int offset = (page - 1) * pageSize;
        List<Map<String, Object>> list = notificationMapper.selectNewsGrouped(offset, pageSize);
        long total = notificationMapper.countNewsGroups();
        return Map.of("list", list, "total", total, "page", page, "pageSize", pageSize);
    }

    @Override
    @Transactional
    public int createNews(NotificationRequest request, Integer adminUserId) {
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.eq(User::getIsActive, true);
        if (hasText(request.getTargetType()) && !"全部".equals(request.getTargetType())) {
            qw.eq(User::getUserType, request.getTargetType());
        }
        List<User> targets = userMapper.selectList(qw);
        for (User user : targets) {
            Notification notification = new Notification();
            notification.setUserId(user.getUserId());
            notification.setTitle(request.getTitle());
            notification.setContent(request.getContent());
            notification.setType("系统");
            notification.setIsRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            notificationMapper.insert(notification);
        }
        return targets.size();
    }

    @Override
    public void deleteNews(String title, String content) {
        notificationMapper.delete(new LambdaQueryWrapper<Notification>()
                .eq(Notification::getType, "系统")
                .eq(Notification::getTitle, title)
                .eq(Notification::getContent, content));
    }

    // ==================== 数据统计 ====================

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("userCount", userMapper.selectCount(null));
        stats.put("goodsCount", goodsMapper.selectCount(null));
        stats.put("activityCount", activityMapper.selectCount(null));
        stats.put("orderCount", orderMapper.selectCount(null));
        stats.put("reviewCount", reviewMapper.selectCount(null));
        stats.put("pendingOrders", orderMapper.selectCount(
                new LambdaQueryWrapper<Order>().eq(Order::getOrderStatus, "待处理")));
        stats.put("totalRevenue", orderMapper.sumPaidAmount());

        Page<Order> recent = new Page<>(1, 5);
        Page<Order> recentResult = orderMapper.selectPage(recent,
                new LambdaQueryWrapper<Order>().orderByDesc(Order::getCreatedAt));
        stats.put("recentOrders", recentResult.getRecords());
        return stats;
    }

    // ==================== 工具方法 ====================

    private boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private Map<String, Object> pageResult(Page<?> p) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", p.getRecords());
        map.put("total", p.getTotal());
        map.put("page", p.getCurrent());
        map.put("pageSize", p.getSize());
        return map;
    }
}
