package com.xiangchuang.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 评价请求DTO
 */
@Data
public class ReviewRequest {

    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    private Integer rating;

    @NotBlank(message = "评价内容不能为空")
    @Size(min = 5, message = "评价内容不能少于5个字符")
    private String content;

    private String images;

}