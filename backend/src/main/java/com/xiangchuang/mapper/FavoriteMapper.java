package com.xiangchuang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiangchuang.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 收藏Mapper
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {

    @Select("SELECT * FROM favorites WHERE user_id = #{userId} AND item_type = #{itemType}")
    List<Favorite> selectByUserIdAndType(@Param("userId") Integer userId, @Param("itemType") String itemType);

    @Select("SELECT * FROM favorites WHERE user_id = #{userId} AND item_type = #{itemType} AND item_id = #{itemId}")
    Favorite selectByUserIdAndItem(@Param("userId") Integer userId, @Param("itemType") String itemType, @Param("itemId") Integer itemId);

}