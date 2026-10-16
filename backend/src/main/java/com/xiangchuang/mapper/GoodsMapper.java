package com.xiangchuang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiangchuang.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    @Select("SELECT * FROM goods WHERE is_active = 1 ORDER BY sales_count DESC, goods_stars DESC LIMIT #{limit}")
    List<Goods> selectHotProducts(@Param("limit") Integer limit);

    @Select("SELECT * FROM goods WHERE is_active = 1 AND (goods_name LIKE CONCAT('%', #{keyword}, '%') OR goods_producer LIKE CONCAT('%', #{keyword}, '%') OR goods_introduce LIKE CONCAT('%', #{keyword}, '%')) LIMIT #{limit}")
    List<Goods> search(@Param("keyword") String keyword, @Param("limit") Integer limit);

    @Select("SELECT * FROM goods WHERE is_active = 1 AND goods_id != #{productId} AND goods_category = #{category} ORDER BY RAND() LIMIT #{limit}")
    List<Goods> selectRelatedProducts(@Param("productId") Integer productId, @Param("category") String category, @Param("limit") Integer limit);

    @Select("SELECT DISTINCT goods_category FROM goods WHERE is_active = 1 AND goods_category IS NOT NULL")
    List<String> selectCategories();

    @Select("SELECT DISTINCT goods_area FROM goods WHERE is_active = 1 AND goods_area IS NOT NULL")
    List<String> selectAreas();

    @Select("SELECT DISTINCT goods_time FROM goods WHERE is_active = 1 AND goods_time IS NOT NULL")
    List<String> selectSeasons();

    @Select("SELECT * FROM goods WHERE is_active = 1 ORDER BY RAND() LIMIT #{limit}")
    List<Goods> selectRandomRecommendations(@Param("limit") Integer limit);

}