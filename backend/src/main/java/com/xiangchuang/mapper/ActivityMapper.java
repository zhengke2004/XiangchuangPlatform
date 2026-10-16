package com.xiangchuang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiangchuang.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {

    @Select("SELECT * FROM activities WHERE is_active = 1 ORDER BY RAND() LIMIT #{limit}")
    List<Activity> selectRandomRecommendations(@Param("limit") Integer limit);

    @Select("SELECT DISTINCT activity_type FROM activities WHERE is_active = 1")
    List<String> selectTypes();

    @Select("SELECT activity_theme FROM activities WHERE is_active = 1")
    List<String> selectThemes();

}