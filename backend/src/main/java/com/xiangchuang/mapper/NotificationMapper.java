package com.xiangchuang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiangchuang.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 通知Mapper
 */
@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {

    @Select("SELECT * FROM notifications WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Notification> selectByUserId(@Param("userId") Integer userId);

    @Select("SELECT COUNT(*) FROM notifications WHERE user_id = #{userId} AND is_read = false")
    Long countUnread(@Param("userId") Integer userId);

    /** 系统资讯分页(按标题+内容分组) */
    @Select("SELECT title, content, COUNT(*) AS recipients, MIN(created_at) AS publishedAt " +
            "FROM notifications WHERE type = '系统' " +
            "GROUP BY title, content ORDER BY publishedAt DESC LIMIT #{offset}, #{size}")
    List<Map<String, Object>> selectNewsGrouped(@Param("offset") int offset, @Param("size") int size);

    /** 系统资讯分组总数 */
    @Select("SELECT COUNT(*) FROM (SELECT 1 FROM notifications WHERE type = '系统' GROUP BY title, content) t")
    Long countNewsGroups();

}