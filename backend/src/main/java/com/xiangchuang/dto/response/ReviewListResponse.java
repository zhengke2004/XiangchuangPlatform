package com.xiangchuang.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评价列表响应DTO
 */
@Data
public class ReviewListResponse {

    private List<ReviewItem> reviews;

    private PaginationResponse pagination;

    @Data
    public static class ReviewItem {
        private Integer reviewId;
        private Integer userId;
        private String username;
        private String avatar;
        private Integer rating;
        private String content;
        private String images;
        private LocalDateTime createdAt;
    }

}