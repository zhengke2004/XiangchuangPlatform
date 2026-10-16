package com.xiangchuang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收藏实体类
 */
@Data
@TableName("favorites")
public class Favorite {

    @TableId(type = IdType.AUTO)
    private Integer favoriteId;

    private Integer userId;

    private String itemType;

    private Integer itemId;

    private LocalDateTime createdAt;

}