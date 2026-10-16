<template>
  <div class="product-detail">
    <div class="container">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-state">
        <p>加载中...</p>
      </div>

      <!-- 商品详情 -->
      <div v-else-if="product" class="detail-content">
        <div class="product-image-section">
          <div class="main-image">
            <img :src="product.goods_picture" :alt="product.goods_name" />
          </div>
          <div v-if="product.goods_p_picture" class="producer-image">
            <img :src="product.goods_p_picture" :alt="product.goods_producer" />
          </div>
        </div>

        <div class="product-info">
          <h1>{{ product.goods_name }}</h1>
          <div class="product-rating">
            <span class="stars">⭐ {{ product.goods_stars || 5.0 }}</span>
            <span class="sales">已售 {{ product.sales_count || 0 }}件</span>
          </div>
          <p class="product-intro">{{ product.goods_introduce }}</p>

          <div class="product-meta">
            <div class="meta-item">
              <span class="label">分类</span>
              <span class="value">{{ product.goods_category }}</span>
            </div>
            <div class="meta-item">
              <span class="label">产地</span>
              <span class="value">{{ product.goods_area }}</span>
            </div>
            <div class="meta-item">
              <span class="label">生产者</span>
              <span class="value">{{ product.goods_producer }}</span>
            </div>
            <div class="meta-item">
              <span class="label">时令</span>
              <span class="value">{{ product.goods_time }}</span>
            </div>
          </div>

          <div class="price-section">
            <span class="price">¥{{ product.goods_price }}</span>
            <span class="unit">/{{ product.goods_standard || '件' }}</span>
          </div>

          <div class="stock-info">
            <span :class="{ 'low-stock': product.stock < 10 }">
              库存：{{ product.stock }}件
            </span>
            <span v-if="product.stock < 10" class="stock-warning">库存不足</span>
          </div>

          <div class="action-section">
            <div class="quantity-control">
              <button @click="decreaseQuantity" :disabled="quantity <= 1">-</button>
              <span>{{ quantity }}</span>
              <button @click="increaseQuantity" :disabled="quantity >= product.stock">+</button>
            </div>
            <button class="add-cart-btn" @click="addToCart" :disabled="product.stock === 0">
              {{ product.stock === 0 ? '暂无库存' : '加入购物车' }}
            </button>
            <button class="buy-btn" @click="buyNow" :disabled="product.stock === 0">
              {{ product.stock === 0 ? '暂无库存' : '立即购买' }}
            </button>
          </div>

          <div v-if="product.goods_p_address" class="address-info">
            <span class="label">发货地</span>
            <span class="value">{{ product.goods_p_address }}</span>
          </div>
        </div>
      </div>

      <!-- 错误状态 -->
      <div v-else class="error-state">
        <p>商品不存在或已下架</p>
        <router-link to="/market" class="back-btn">返回市集</router-link>
      </div>

      <!-- 标签页 -->
      <div v-if="product" class="tabs-section">
        <div class="tabs">
          <button class="tab-btn" :class="{ active: activeTab === 'detail' }" @click="activeTab = 'detail'">商品详情</button>
          <button class="tab-btn" :class="{ active: activeTab === 'reviews' }" @click="activeTab = 'reviews'">
            用户评价 ({{ reviewsPagination.total }})
          </button>
          <button class="tab-btn" :class="{ active: activeTab === 'origin' }" @click="activeTab = 'origin'">产地溯源</button>
        </div>

        <div class="tab-content">
          <!-- 商品详情 -->
          <div v-if="activeTab === 'detail'" class="detail-tab">
            <div class="detail-section">
              <h3>产品介绍</h3>
              <p>{{ product.goods_introduce || '暂无详细介绍' }}</p>
            </div>
            <div class="detail-section">
              <h3>规格参数</h3>
              <table class="spec-table">
                <tbody>
                  <tr>
                    <td class="label">产品名称</td>
                    <td>{{ product.goods_name }}</td>
                  </tr>
                  <tr>
                    <td class="label">产品分类</td>
                    <td>{{ product.goods_category }}</td>
                  </tr>
                  <tr>
                    <td class="label">产品产地</td>
                    <td>{{ product.goods_area }}</td>
                  </tr>
                  <tr>
                    <td class="label">产品规格</td>
                    <td>{{ product.goods_standard || '默认规格' }}</td>
                  </tr>
                  <tr>
                    <td class="label">生产者</td>
                    <td>{{ product.goods_producer }}</td>
                  </tr>
                  <tr>
                    <td class="label">时令季节</td>
                    <td>{{ product.goods_time }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <!-- 用户评价 -->
          <div v-if="activeTab === 'reviews'" class="reviews-tab">
            <div v-if="reviews.length > 0" class="reviews-list">
              <div v-for="review in reviews" :key="review.reviewId" class="review-item">
                <div class="review-header">
                  <div class="reviewer-info">
                    <span class="reviewer-avatar">{{ (review.username || '用户')[0] }}</span>
                    <span class="reviewer-name">{{ review.username || '匿名用户' }}</span>
                  </div>
                  <div class="review-meta">
                    <span class="review-rating">⭐ {{ review.rating }}</span>
                    <span class="review-date">{{ formatDate(review.createdAt) }}</span>
                  </div>
                </div>
                <p class="review-content">{{ review.content }}</p>
                <div v-if="review.images" class="review-images">
                  <img v-for="(img, idx) in parseImages(review.images)" :key="idx" :src="img" alt="评价图片" />
                </div>
              </div>
            </div>
            <div v-else class="empty">暂无评价</div>

            <!-- 分页 -->
            <div v-if="reviewsPagination.totalPages > 1" class="pagination">
              <button :disabled="reviewsPagination.currentPage === 1" @click="prevReviewPage">上一页</button>
              <span>{{ reviewsPagination.currentPage }} / {{ reviewsPagination.totalPages }}</span>
              <button :disabled="reviewsPagination.currentPage === reviewsPagination.totalPages" @click="nextReviewPage">下一页</button>
            </div>
          </div>

          <!-- 产地溯源 -->
          <div v-if="activeTab === 'origin'" class="origin-tab">
            <div class="origin-info">
              <div class="origin-section">
                <h3>产地信息</h3>
                <p>{{ product.goods_area }}</p>
              </div>
              <div class="origin-section">
                <h3>生产者信息</h3>
                <p>{{ product.goods_producer }}</p>
              </div>
              <div v-if="product.goods_p_address" class="origin-section">
                <h3>生产地址</h3>
                <p>{{ product.goods_p_address }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 推荐商品 -->
      <div v-if="recommendations.length > 0" class="recommendations-section">
        <h2>相关推荐</h2>
        <div class="recommendations-grid">
          <router-link
            v-for="item in recommendations"
            :key="item.goods_id"
            :to="`/product/${item.goods_id}`"
            class="recommendation-card"
          >
            <img :src="item.goods_picture" :alt="item.goods_name" />
            <div class="recommendation-info">
              <h4>{{ item.goods_name }}</h4>
              <span class="recommendation-price">¥{{ item.goods_price }}</span>
            </div>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '@/api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

const product = ref(null)
const recommendations = ref([])
const reviews = ref([])
const loading = ref(true)
const quantity = ref(1)
const activeTab = ref('detail')

const reviewsPagination = reactive({
  currentPage: 1,
  pageSize: 5,
  total: 0,
  totalPages: 0
})

// 加载商品详情
async function loadProduct() {
  loading.value = true
  try {
    const response = await api.products.detail(route.params.id)
    product.value = response.data?.product || null
    recommendations.value = response.data?.recommendations || []
  } catch (e) {
    console.error('获取商品详情失败', e)
    ElMessage.error('获取商品详情失败')
    product.value = null
  } finally {
    loading.value = false
  }
}

// 加载评价
async function loadReviews() {
  try {
    const response = await api.products.reviews(
      route.params.id,
      reviewsPagination.currentPage,
      reviewsPagination.pageSize
    )
    reviews.value = response.data?.reviews || []
    reviewsPagination.total = response.data?.pagination?.total || 0
    reviewsPagination.totalPages = response.data?.pagination?.totalPages || 0
  } catch (e) {
    console.error('获取评价失败', e)
  }
}

// 减少数量
function decreaseQuantity() {
  if (quantity.value > 1) {
    quantity.value--
  }
}

// 增加数量
function increaseQuantity() {
  if (product.value && quantity.value < product.value.stock) {
    quantity.value++
  }
}

// 加入购物车
function addToCart() {
  if (!product.value || product.value.stock === 0) return

  // TODO: 实现购物车功能
  ElMessage.success(`已将 ${quantity.value} 件商品加入购物车`)
}

// 立即购买
async function buyNow() {
  if (!product.value || product.value.stock === 0) return

  const authStore = useAuthStore()
  if (!authStore.isLogin) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  const p = product.value
  try {
    await api.orders.create({
      orderType: '商品',
      items: [
        {
          itemIdRef: p.goods_id,
          itemName: p.goods_name,
          quantity: quantity.value,
          price: p.goods_price
        }
      ],
      contactName: authStore.user?.username,
      contactPhone: authStore.user?.phone,
      contactAddress: authStore.user?.address
    })
    ElMessage.success('下单成功，可在「我的订单」中查看')
    router.push('/orders')
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '下单失败')
  }
}

// 评价分页
function prevReviewPage() {
  if (reviewsPagination.currentPage > 1) {
    reviewsPagination.currentPage--
    loadReviews()
  }
}

function nextReviewPage() {
  if (reviewsPagination.currentPage < reviewsPagination.totalPages) {
    reviewsPagination.currentPage++
    loadReviews()
  }
}

// 格式化日期
function formatDate(dateStr) {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

// 解析评价图片
function parseImages(images) {
  if (!images) return []
  try {
    return JSON.parse(images)
  } catch {
    return images.split(',').filter(img => img.trim())
  }
}

// 监听路由变化
watch(() => route.params.id, (newId) => {
  if (newId) {
    quantity.value = 1
    activeTab.value = 'detail'
    reviewsPagination.currentPage = 1
    loadProduct()
    loadReviews()
  }
})

onMounted(() => {
  loadProduct()
  loadReviews()
})
</script>

<style scoped>
.product-detail {
  padding: 30px 0;
}

.loading-state,
.error-state {
  text-align: center;
  padding: 100px 20px;
  color: #666;
}

.back-btn {
  display: inline-block;
  margin-top: 20px;
  padding: 10px 30px;
  background: #4a8c3a;
  color: white;
  border-radius: 4px;
}

.detail-content {
  display: flex;
  gap: 40px;
  margin-bottom: 40px;
  background: white;
  padding: 30px;
  border-radius: 8px;
}

.product-image-section {
  width: 450px;
  flex-shrink: 0;
}

.main-image img {
  width: 100%;
  height: 400px;
  object-fit: cover;
  border-radius: 8px;
}

.producer-image {
  margin-top: 15px;
}

.producer-image img {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 8px;
}

.product-info {
  flex: 1;
}

.product-info h1 {
  font-size: 28px;
  margin-bottom: 15px;
  color: #333;
}

.product-rating {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 15px;
}

.stars {
  font-size: 18px;
  color: #f5a623;
}

.sales {
  font-size: 14px;
  color: #666;
}

.product-intro {
  font-size: 16px;
  color: #666;
  line-height: 1.8;
  margin-bottom: 20px;
}

.product-meta {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
  margin-bottom: 25px;
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.meta-item .label {
  font-size: 14px;
  color: #999;
}

.meta-item .value {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.price-section {
  margin-bottom: 15px;
}

.price {
  font-size: 36px;
  font-weight: bold;
  color: #e74c3c;
}

.unit {
  font-size: 14px;
  color: #666;
  margin-left: 5px;
}

.stock-info {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 25px;
  font-size: 14px;
  color: #666;
}

.low-stock {
  color: #e74c3c;
}

.stock-warning {
  background: #fff3f3;
  color: #e74c3c;
  padding: 3px 10px;
  border-radius: 4px;
  font-size: 12px;
}

.action-section {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
}

.quantity-control {
  display: flex;
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow: hidden;
}

.quantity-control button {
  width: 40px;
  height: 40px;
  font-size: 18px;
  border: none;
  background: #f8f9fa;
  cursor: pointer;
}

.quantity-control button:hover:not(:disabled) {
  background: #e9ecef;
}

.quantity-control button:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.quantity-control span {
  width: 50px;
  text-align: center;
  font-size: 16px;
  font-weight: bold;
}

.add-cart-btn,
.buy-btn {
  padding: 12px 30px;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s;
}

.add-cart-btn {
  border: 1px solid #4a8c3a;
  color: #4a8c3a;
  background: white;
}

.add-cart-btn:hover:not(:disabled) {
  background: #f0f7ed;
}

.buy-btn {
  background: #4a8c3a;
  color: white;
  border: none;
}

.buy-btn:hover:not(:disabled) {
  background: #3d7a2f;
}

.add-cart-btn:disabled,
.buy-btn:disabled {
  background: #ccc;
  color: #999;
  cursor: not-allowed;
  border-color: #ccc;
}

.address-info {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  color: #666;
}

.address-info .label {
  color: #999;
}

.tabs-section {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 40px;
}

.tabs {
  display: flex;
  border-bottom: 1px solid #eee;
  background: #f8f9fa;
}

.tab-btn {
  padding: 15px 30px;
  border: none;
  background: none;
  font-size: 16px;
  cursor: pointer;
  color: #666;
  transition: all 0.3s;
}

.tab-btn:hover {
  color: #4a8c3a;
}

.tab-btn.active {
  color: #4a8c3a;
  border-bottom: 2px solid #4a8c3a;
  background: white;
}

.tab-content {
  padding: 30px;
}

.detail-section {
  margin-bottom: 30px;
}

.detail-section h3 {
  font-size: 18px;
  margin-bottom: 15px;
  color: #333;
}

.detail-section p {
  font-size: 14px;
  color: #666;
  line-height: 1.8;
}

.spec-table {
  width: 100%;
  border-collapse: collapse;
}

.spec-table td {
  padding: 12px 15px;
  border-bottom: 1px solid #eee;
}

.spec-table td.label {
  width: 120px;
  color: #999;
  background: #f8f9fa;
}

.reviews-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.review-item {
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.reviewer-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.reviewer-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #4a8c3a;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
}

.reviewer-name {
  font-weight: 500;
  color: #333;
}

.review-meta {
  display: flex;
  align-items: center;
  gap: 15px;
}

.review-rating {
  color: #f5a623;
}

.review-date {
  color: #999;
  font-size: 14px;
}

.review-content {
  color: #666;
  font-size: 14px;
  line-height: 1.8;
}

.review-images {
  display: flex;
  gap: 10px;
  margin-top: 15px;
}

.review-images img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-top: 30px;
}

.pagination button {
  padding: 8px 20px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 4px;
  cursor: pointer;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.origin-info {
  display: grid;
  gap: 25px;
}

.origin-section h3 {
  font-size: 16px;
  margin-bottom: 10px;
  color: #333;
}

.origin-section p {
  font-size: 14px;
  color: #666;
  line-height: 1.8;
}

.empty {
  text-align: center;
  padding: 50px;
  color: #999;
}

.recommendations-section {
  margin-top: 40px;
}

.recommendations-section h2 {
  font-size: 24px;
  margin-bottom: 25px;
  color: #333;
}

.recommendations-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.recommendation-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s;
}

.recommendation-card:hover {
  transform: translateY(-5px);
}

.recommendation-card img {
  width: 100%;
  height: 150px;
  object-fit: cover;
}

.recommendation-info {
  padding: 15px;
}

.recommendation-info h4 {
  font-size: 14px;
  margin-bottom: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.recommendation-price {
  font-size: 18px;
  font-weight: bold;
  color: #e74c3c;
}

@media (max-width: 1200px) {
  .detail-content {
    flex-direction: column;
  }

  .product-image-section {
    width: 100%;
  }

  .recommendations-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .product-meta {
    grid-template-columns: 1fr;
  }

  .action-section {
    flex-wrap: wrap;
  }

  .recommendations-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>