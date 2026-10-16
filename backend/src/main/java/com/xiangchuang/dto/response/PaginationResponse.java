package com.xiangchuang.dto.response;

import lombok.Data;

/**
 * 分页响应DTO
 */
@Data
public class PaginationResponse {

    private Integer currentPage;

    private Integer pageSize;

    private Long total;

    private Integer totalPages;

    public static PaginationResponse of(Integer currentPage, Integer pageSize, Long total) {
        PaginationResponse response = new PaginationResponse();
        response.setCurrentPage(currentPage);
        response.setPageSize(pageSize);
        response.setTotal(total);
        response.setTotalPages((int) Math.ceil((double) total / pageSize));
        return response;
    }

}