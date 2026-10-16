package com.xiangchuang.controller;

import com.xiangchuang.dto.request.ProductQueryRequest;
import com.xiangchuang.dto.request.ReviewRequest;
import com.xiangchuang.dto.response.ApiResponse;
import com.xiangchuang.dto.response.ProductFilterResponse;
import com.xiangchuang.dto.response.ProductListResponse;
import com.xiangchuang.dto.response.ReviewListResponse;
import com.xiangchuang.service.ProductService;
import com.xiangchuang.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品控制器
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final JwtUtil jwtUtil;

    public ProductController(ProductService productService, JwtUtil jwtUtil) {
        this.productService = productService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ApiResponse<ProductListResponse> getProducts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "12") Integer pageSize,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String season,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "created_at") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {

        ProductQueryRequest request = new ProductQueryRequest();
        request.setPage(page);
        request.setPageSize(pageSize);
        request.setCategory(category);
        request.setArea(area);
        request.setSeason(season);
        request.setKeyword(keyword);
        request.setMinPrice(minPrice);
        request.setMaxPrice(maxPrice);
        request.setSortBy(sortBy);
        request.setSortOrder(sortOrder);

        try {
            ProductListResponse response = productService.getProducts(request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取商品列表失败");
        }
    }

    @GetMapping("/recommendations")
    public ApiResponse<List<Object>> getRecommendations(
            @RequestParam(defaultValue = "3") Integer limit,
            @RequestHeader(value = "Authorization", required = false) String token) {

        Integer userId = null;
        if (token != null && token.startsWith("Bearer ")) {
            try {
                userId = jwtUtil.getUserIdFromToken(token.substring(7));
            } catch (Exception e) {
                // ignore
            }
        }

        try {
            List<Object> recommendations = productService.getRecommendations(limit, userId);
            return ApiResponse.success(recommendations);
        } catch (Exception e) {
            return ApiResponse.success("获取推荐失败", List.of());
        }
    }

    @GetMapping("/hot")
    public ApiResponse<List<Object>> getHotProducts(@RequestParam(defaultValue = "5") Integer limit) {
        try {
            List<Object> hotProducts = productService.getHotProducts(limit);
            return ApiResponse.success(hotProducts);
        } catch (Exception e) {
            return ApiResponse.success("获取热门商品失败", List.of());
        }
    }

    @GetMapping("/search")
    public ApiResponse<List<Object>> search(@RequestParam String keyword, @RequestParam(defaultValue = "20") Integer limit) {
        if (keyword == null || keyword.isEmpty()) {
            return ApiResponse.success(List.of());
        }

        try {
            List<Object> results = productService.search(keyword, limit);
            return ApiResponse.success(results);
        } catch (Exception e) {
            return ApiResponse.error(500, "搜索失败");
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<Object> getProductById(@PathVariable Integer id) {
        try {
            Object product = productService.getProductById(id);
            if (product == null) {
                return ApiResponse.error(404, "商品不存在");
            }
            return ApiResponse.success(product);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取商品详情失败");
        }
    }

    @GetMapping("/{id}/reviews")
    public ApiResponse<ReviewListResponse> getReviews(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        try {
            ReviewListResponse response = productService.getReviews(id, page, pageSize);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取评价失败");
        }
    }

    @PostMapping("/{id}/reviews")
    public ApiResponse<Void> addReview(
            @PathVariable Integer id,
            @Valid @RequestBody ReviewRequest request,
            @RequestHeader(value = "Authorization", required = false) String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            return ApiResponse.error(401, "请先登录");
        }

        try {
            Integer userId = jwtUtil.getUserIdFromToken(token.substring(7));
            productService.addReview(id, userId, request);
            return ApiResponse.success("评价成功", null);
        } catch (Exception e) {
            return ApiResponse.error(500, "添加评价失败");
        }
    }

    @GetMapping("/filters/all")
    public ApiResponse<ProductFilterResponse> getFilterOptions() {
        try {
            ProductFilterResponse filters = productService.getFilterOptions();
            return ApiResponse.success(filters);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取筛选选项失败");
        }
    }

}