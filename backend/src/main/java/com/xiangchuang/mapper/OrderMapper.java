package com.xiangchuang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiangchuang.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单Mapper
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    @Select("SELECT * FROM orders WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Order> selectByUserId(@Param("userId") Integer userId);

    @Select("SELECT * FROM orders WHERE user_id = #{userId} AND order_status = #{status} ORDER BY created_at DESC")
    List<Order> selectByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") String status);

    /** 已付款订单总金额 */
    @Select("SELECT IFNULL(SUM(total_amount), 0) FROM orders WHERE payment_status = '已付款'")
    BigDecimal sumPaidAmount();

    /** 订单明细列表 */
    @Select("SELECT item_id, order_id, item_type, item_id_ref, item_name, quantity, price, subtotal " +
            "FROM order_items WHERE order_id = #{orderId}")
    List<Map<String, Object>> selectItemsByOrderId(@Param("orderId") Integer orderId);

}