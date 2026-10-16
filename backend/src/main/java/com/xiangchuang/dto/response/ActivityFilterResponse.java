package com.xiangchuang.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 活动筛选选项响应DTO
 */
@Data
public class ActivityFilterResponse {

    private List<String> types;

    private List<String> themes;

}