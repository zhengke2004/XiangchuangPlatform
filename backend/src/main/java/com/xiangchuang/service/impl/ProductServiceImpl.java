package com.xiangchuang.service.impl;

import com.xiangchuang.dto.request.ProductQueryRequest;
import com.xiangchuang.dto.request.ReviewRequest;
import com.xiangchuang.dto.response.ProductFilterResponse;
import com.xiangchuang.dto.response.ProductListResponse;
import com.xiangchuang.dto.response.PaginationResponse;
import com.xiangchuang.dto.response.ReviewListResponse;
import com.xiangchuang.entity.Goods;
import com.xiangchuang.entity.Review;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangchuang.mapper.GoodsMapper;
import com.xiangchuang.mapper.ReviewMapper;
import com.xiangchuang.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品服务实现
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final GoodsMapper goodsMapper;
    private final ReviewMapper reviewMapper;

    public ProductServiceImpl(GoodsMapper goodsMapper, ReviewMapper reviewMapper) {
        this.goodsMapper = goodsMapper;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public ProductListResponse getProducts(ProductQueryRequest request) {
        String season = convertSeason(request.getSeason());

        int pageSize = Math.max(1, Math.min(100, request.getPageSize()));
        int currentPage = Math.max(1, request.getPage());

        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Goods::getIsActive, true);

        if (request.getCategory() != null && !request.getCategory().isEmpty()) {
            queryWrapper.eq(Goods::getGoodsCategory, request.getCategory());
        }
        if (request.getArea() != null && !request.getArea().isEmpty()) {
            queryWrapper.eq(Goods::getGoodsArea, request.getArea());
        }
        if (season != null && !season.isEmpty()) {
            queryWrapper.eq(Goods::getGoodsTime, season);
        }
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            queryWrapper.and(wrapper -> wrapper
                    .like(Goods::getGoodsName, request.getKeyword())
                    .or()
                    .like(Goods::getGoodsProducer, request.getKeyword())
                    .or()
                    .like(Goods::getGoodsIntroduce, request.getKeyword())
            );
        }
        if (request.getMinPrice() != null) {
            queryWrapper.ge(Goods::getGoodsPrice, request.getMinPrice());
        }
        if (request.getMaxPrice() != null) {
            queryWrapper.le(Goods::getGoodsPrice, request.getMaxPrice());
        }

        String sortBy = request.getSortBy() != null ? request.getSortBy() : "goods_id";
        boolean isAsc = "ASC".equalsIgnoreCase(request.getSortOrder());
        
        if ("goods_id".equals(sortBy)) {
            if (isAsc) {
                queryWrapper.orderByAsc(Goods::getGoodsId);
            } else {
                queryWrapper.orderByDesc(Goods::getGoodsId);
            }
        } else if ("goods_price".equals(sortBy)) {
            if (isAsc) {
                queryWrapper.orderByAsc(Goods::getGoodsPrice);
            } else {
                queryWrapper.orderByDesc(Goods::getGoodsPrice);
            }
        } else if ("sales_count".equals(sortBy)) {
            if (isAsc) {
                queryWrapper.orderByAsc(Goods::getSalesCount);
            } else {
                queryWrapper.orderByDesc(Goods::getSalesCount);
            }
        } else if ("goods_stars".equals(sortBy)) {
            if (isAsc) {
                queryWrapper.orderByAsc(Goods::getGoodsStars);
            } else {
                queryWrapper.orderByDesc(Goods::getGoodsStars);
            }
        } else {
            queryWrapper.orderByDesc(Goods::getGoodsId);
        }

        Page<Goods> page = new Page<>(currentPage, pageSize);
        Page<Goods> resultPage = goodsMapper.selectPage(page, queryWrapper);

        ProductFilterResponse filters = getFilterOptions();

        ProductListResponse response = new ProductListResponse();
        response.setProducts(resultPage.getRecords().stream().map(this::convertToMap).collect(Collectors.toList()));
        response.setPagination(PaginationResponse.of(request.getPage(), pageSize, resultPage.getTotal()));
        response.setFilters(filters);

        return response;
    }

    @Override
    public List<Object> getRecommendations(Integer limit, Integer userId) {
        int safeLimit = Math.max(1, Math.min(50, limit));
        List<Goods> goodsList = goodsMapper.selectRandomRecommendations(safeLimit);
        return goodsList.stream().map(this::convertToMap).collect(Collectors.toList());
    }

    @Override
    public List<Object> getHotProducts(Integer limit) {
        int safeLimit = Math.max(1, Math.min(50, limit));
        List<Goods> goodsList = goodsMapper.selectHotProducts(safeLimit);
        return goodsList.stream().map(this::convertToMap).collect(Collectors.toList());
    }

    @Override
    public List<Object> search(String keyword, Integer limit) {
        int safeLimit = Math.max(1, Math.min(100, limit));
        List<Goods> goodsList = goodsMapper.search(keyword, safeLimit);
        return goodsList.stream().map(this::convertToMap).collect(Collectors.toList());
    }

    @Override
    public Object getProductById(Integer id) {
        Goods goods = goodsMapper.selectById(id);
        if (goods == null || !goods.getIsActive()) {
            return null;
        }

        List<Goods> related = goodsMapper.selectRelatedProducts(id, goods.getGoodsCategory(), 4);

        Map<String, Object> result = new HashMap<>();
        result.put("product", convertToMap(goods));
        result.put("recommendations", related.stream().map(this::convertToMap).collect(Collectors.toList()));

        return result;
    }

    @Override
    public ReviewListResponse getReviews(Integer productId, Integer page, Integer pageSize) {
        int safePageSize = Math.max(1, Math.min(100, pageSize));
        int offset = Math.max(0, (page - 1) * safePageSize);

        List<Review> reviews = reviewMapper.selectWithUserInfo("商品", productId, safePageSize, offset);
        Long total = reviewMapper.countByItem("商品", productId);

        ReviewListResponse response = new ReviewListResponse();
        response.setReviews(reviews.stream().map(this::convertToReviewItem).collect(Collectors.toList()));
        response.setPagination(PaginationResponse.of(page, safePageSize, total));

        return response;
    }

    @Override
    @Transactional
    public void addReview(Integer productId, Integer userId, ReviewRequest request) {
        Review review = new Review();
        review.setUserId(userId);
        review.setItemType("商品");
        review.setItemId(productId);
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setImages(request.getImages());

        reviewMapper.insert(review);
    }

    @Override
    public ProductFilterResponse getFilterOptions() {
        ProductFilterResponse response = new ProductFilterResponse();
        response.setCategories(goodsMapper.selectCategories());
        response.setAreas(goodsMapper.selectAreas());
        response.setSeasons(goodsMapper.selectSeasons());
        return response;
    }

    private String convertSeason(String season) {
        if (season == null) return null;
        Map<String, String> seasonMap = new HashMap<>();
        seasonMap.put("spring", "春");
        seasonMap.put("summer", "夏");
        seasonMap.put("autumn", "秋");
        seasonMap.put("winter", "冬");
        return seasonMap.getOrDefault(season, season);
    }

    private Map<String, Object> convertToMap(Goods goods) {
        Map<String, Object> map = new HashMap<>();
        map.put("goods_id", goods.getGoodsId());
        map.put("goods_name", goods.getGoodsName());
        map.put("goods_area", goods.getGoodsArea());
        map.put("goods_category", goods.getGoodsCategory());
        map.put("goods_time", goods.getGoodsTime());
        map.put("goods_producer", goods.getGoodsProducer());
        map.put("goods_p_address", goods.getGoodsPAddress());
        map.put("goods_introduce", goods.getGoodsIntroduce());
        map.put("goods_price", goods.getGoodsPrice());
        map.put("goods_standard", goods.getGoodsStandard());
        map.put("goods_stars", goods.getGoodsStars());
        map.put("goods_picture", goods.getGoodsPicture());
        map.put("goods_p_picture", goods.getGoodsPPicture());
        map.put("sales_count", goods.getSalesCount());
        map.put("stock", goods.getStock());
        map.put("created_at", goods.getCreatedAt());
        return map;
    }

    private ReviewListResponse.ReviewItem convertToReviewItem(Review review) {
        ReviewListResponse.ReviewItem item = new ReviewListResponse.ReviewItem();
        item.setReviewId(review.getReviewId());
        item.setUserId(review.getUserId());
        item.setRating(review.getRating());
        item.setContent(review.getContent());
        item.setImages(review.getImages());
        item.setCreatedAt(review.getCreatedAt());
        return item;
    }

}