package com.xiangchuang.util;

import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码工具类
 */
@Component
public class PasswordUtil {

    private final BCryptPasswordEncoder encoder;

    public PasswordUtil() {
        this.encoder = new BCryptPasswordEncoder();
    }

    public String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

}