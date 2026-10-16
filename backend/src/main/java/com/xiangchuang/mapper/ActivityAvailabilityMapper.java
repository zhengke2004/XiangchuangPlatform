package com.xiangchuang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiangchuang.entity.ActivityAvailability;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * 活动可用日期Mapper
 */
@Mapper
public interface ActivityAvailabilityMapper extends BaseMapper<ActivityAvailability> {

    @Select("SELECT * FROM activity_availability WHERE activity_id = #{activityId} AND available_date = #{date} AND is_available = 1")
    ActivityAvailability selectByActivityIdAndDate(@Param("activityId") Integer activityId, @Param("date") LocalDate date);

    @Select("SELECT available_date, available_spots FROM activity_availability WHERE activity_id = #{activityId} AND is_available = 1 AND available_spots > 0 ORDER BY available_date")
    List<ActivityAvailability> selectAvailableDates(@Param("activityId") Integer activityId);

}