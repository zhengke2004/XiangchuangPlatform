package com.xiangchuang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知实体类
 */
@Data
@TableName("notifications")
public class Notification {

    @TableId(type = IdType.AUTO)
    private Integer notificationId;

    private Integer userId;

    private String title;

    private String content;

    private String type;

    private Boolean isRead;

    private LocalDateTime createdAt;

}