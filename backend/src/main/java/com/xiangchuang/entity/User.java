package com.xiangchuang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@TableName("users")
public class User {

    @TableId(type = IdType.AUTO)
    private Integer userId;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String avatar;

    private String realName;

    private String gender;

    private LocalDateTime birthDate;

    private String province;

    private String city;

    private String address;

    private String userType;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}