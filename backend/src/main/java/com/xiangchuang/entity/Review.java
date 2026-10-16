package com.xiangchuang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评价实体类
 */
@Data
@TableName("reviews")
public class Review {

    @TableId(type = IdType.AUTO)
    private Integer reviewId;

    private Integer userId;

    private String itemType;

    private Integer itemId;

    private Integer rating;

    private String content;

    private String images;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}