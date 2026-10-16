package com.xiangchuang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiangchuang.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM users WHERE phone = #{phone} AND is_active = 1")
    User selectByPhone(@Param("phone") String phone);

    @Select("SELECT * FROM users WHERE username = #{username}")
    User selectByUsername(@Param("username") String username);

}