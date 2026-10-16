package com.xiangchuang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiangchuang.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 评价Mapper
 */
@Mapper
public interface ReviewMapper extends BaseMapper<Review> {

    @Select("SELECT r.*, u.username, u.avatar FROM reviews r " +
            "LEFT JOIN users u ON r.user_id = u.user_id " +
            "WHERE r.item_type = #{itemType} AND r.item_id = #{itemId} " +
            "ORDER BY r.created_at DESC LIMIT #{pageSize} OFFSET #{offset}")
    List<Review> selectWithUserInfo(@Param("itemType") String itemType,
                                    @Param("itemId") Integer itemId,
                                    @Param("pageSize") Integer pageSize,
                                    @Param("offset") Integer offset);

    @Select("SELECT COUNT(*) FROM reviews WHERE item_type = #{itemType} AND item_id = #{itemId}")
    Long countByItem(@Param("itemType") String itemType, @Param("itemId") Integer itemId);

}