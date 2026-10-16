package com.xiangchuang.service.impl;

import com.xiangchuang.dto.request.OrderCreateRequest;
import com.xiangchuang.entity.Goods;
import com.xiangchuang.entity.Order;
import com.xiangchuang.entity.OrderItem;
import com.xiangchuang.mapper.GoodsMapper;
import com.xiangchuang.mapper.OrderItemMapper;
import com.xiangchuang.mapper.OrderMapper;
import com.xiangchuang.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 用户订单服务实现
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final GoodsMapper goodsMapper;

    public OrderServiceImpl(OrderMapper orderMapper, OrderItemMapper orderItemMapper, GoodsMapper goodsMapper) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.goodsMapper = goodsMapper;
    }

    @Override
    @Transactional
    public Map<String, Object> createOrder(Integer userId, OrderCreateRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("订单明细不能为空");
        }
        String orderType = request.getOrderType();
        if (!List.of("商品", "旅游").contains(orderType)) {
            throw new RuntimeException("无效的订单类型");
        }

        // 计算总金额
        BigDecimal totalAmount = request.getItems().stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 生成订单号
        String orderNo = "ORD" + System.currentTimeMillis()
                + String.format("%04d", ThreadLocalRandom.current().nextInt(10000));

        // 写入订单主表
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setOrderType(orderType);
        order.setTotalAmount(totalAmount);
        order.setPaymentStatus("待付款");
        order.setOrderStatus("待处理");
        order.setContactName(request.getContactName());
        order.setContactPhone(request.getContactPhone());
        order.setContactAddress(request.getContactAddress());
        order.setTravelDate(request.getTravelDate());
        order.setParticipants(request.getParticipants() == null ? 1 : request.getParticipants());
        order.setSpecialRequests(request.getSpecialRequests());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.insert(order);

        // 写入订单明细
        String itemType = "商品".equals(orderType) ? "商品" : "活动";
        for (OrderCreateRequest.OrderItemRequest item : request.getItems()) {
            OrderItem oi = new OrderItem();
            oi.setOrderId(order.getOrderId());
            oi.setItemType(itemType);
            oi.setItemIdRef(item.getItemIdRef());
            oi.setItemName(item.getItemName());
            oi.setQuantity(item.getQuantity() == null ? 1 : item.getQuantity());
            oi.setPrice(item.getPrice());
            oi.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(oi.getQuantity())));
            orderItemMapper.insert(oi);
        }

        // 商品订单：扣减库存、累加销量
        if ("商品".equals(orderType)) {
            for (OrderCreateRequest.OrderItemRequest item : request.getItems()) {
                Goods goods = goodsMapper.selectById(item.getItemIdRef());
                if (goods != null) {
                    Goods update = new Goods();
                    update.setGoodsId(goods.getGoodsId());
                    update.setStock(Math.max(0, goods.getStock() - item.getQuantity()));
                    update.setSalesCount(goods.getSalesCount() + item.getQuantity());
                    update.setUpdatedAt(LocalDateTime.now());
                    goodsMapper.updateById(update);
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("orderId", order.getOrderId());
        result.put("orderNo", order.getOrderNo());
        result.put("totalAmount", totalAmount);
        return result;
    }

    @Override
    public List<Map<String, Object>> listMyOrders(Integer userId) {
        List<Order> orders = orderMapper.selectByUserId(userId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Order order : orders) {
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", order.getOrderId());
            map.put("orderNo", order.getOrderNo());
            map.put("orderType", order.getOrderType());
            map.put("totalAmount", order.getTotalAmount());
            map.put("paymentStatus", order.getPaymentStatus());
            map.put("orderStatus", order.getOrderStatus());
            map.put("contactName", order.getContactName());
            map.put("contactPhone", order.getContactPhone());
            map.put("contactAddress", order.getContactAddress());
            map.put("travelDate", order.getTravelDate());
            map.put("participants", order.getParticipants());
            map.put("specialRequests", order.getSpecialRequests());
            map.put("createdAt", order.getCreatedAt());
            map.put("items", convertItems(order.getOrderId()));
            result.add(map);
        }
        return result;
    }

    private List<Map<String, Object>> convertItems(Integer orderId) {
        List<Map<String, Object>> items = new ArrayList<>();
        for (Map<String, Object> raw : orderMapper.selectItemsByOrderId(orderId)) {
            Map<String, Object> item = new HashMap<>();
            item.put("itemId", raw.get("item_id"));
            item.put("itemType", raw.get("item_type"));
            item.put("itemIdRef", raw.get("item_id_ref"));
            item.put("itemName", raw.get("item_name"));
            item.put("quantity", raw.get("quantity"));
            item.put("price", raw.get("price"));
            item.put("subtotal", raw.get("subtotal"));
            items.add(item);
        }
        return items;
    }
}
