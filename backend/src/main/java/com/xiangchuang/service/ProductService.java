package com.xiangchuang.service;

import com.xiangchuang.dto.request.ProductQueryRequest;
import com.xiangchuang.dto.request.ReviewRequest;
import com.xiangchuang.dto.response.ProductFilterResponse;
import com.xiangchuang.dto.response.ProductListResponse;
import com.xiangchuang.dto.response.ReviewListResponse;

import java.util.List;

/**
 * 商品服务接口
 */
public interface ProductService {

    ProductListResponse getProducts(ProductQueryRequest request);

    List<Object> getRecommendations(Integer limit, Integer userId);

    List<Object> getHotProducts(Integer limit);

    List<Object> search(String keyword, Integer limit);

    Object getProductById(Integer id);

    ReviewListResponse getReviews(Integer productId, Integer page, Integer pageSize);

    void addReview(Integer productId, Integer userId, ReviewRequest request);

    ProductFilterResponse getFilterOptions();

}