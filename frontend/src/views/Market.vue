<template>
  <div class="market">
    <div class="container">
      <div class="market-header">
        <h1>农品市集</h1>
        <p>精选地道乡村特产，品质保证</p>
      </div>

      <div class="market-content">
        <aside class="sidebar">
          <div class="filter-section">
            <h3>商品分类</h3>
            <div class="filter-group">
              <button class="filter-btn" :class="{ active: selectedCategory === 'all' }" @click="handleCategoryChange('all')">全部</button>
              <button v-for="cat in filters.categories" :key="cat" class="filter-btn" :class="{ active: selectedCategory === cat }" @click="handleCategoryChange(cat)">{{ cat }}</button>
            </div>
          </div>

          <div class="filter-section">
            <h3>产地</h3>
            <div class="filter-group">
              <button class="filter-btn" :class="{ active: selectedArea === 'all' }" @click="handleAreaChange('all')">全部</button>
              <button v-for="area in filters.areas" :key="area" class="filter-btn" :class="{ active: selectedArea === area }" @click="handleAreaChange(area)">{{ area }}</button>
            </div>
          </div>

          <div class="filter-section">
            <h3>时令</h3>
            <div class="filter-group">
              <button class="filter-btn" :class="{ active: selectedSeason === 'all' }" @click="handleSeasonChange('all')">全部</button>
              <button v-for="season in filters.seasons" :key="season" class="filter-btn" :class="{ active: selectedSeason === season }" @click="handleSeasonChange(season)">{{ season }}</button>
            </div>
          </div>

          <div class="filter-section">
            <h3>价格区间</h3>
            <div class="price-input">
              <input v-model="minPrice" type="number" placeholder="最低价格" />
              <span>-</span>
              <input v-model="maxPrice" type="number" placeholder="最高价格" />
            </div>
            <button class="apply-btn" @click="loadProducts">应用筛选</button>
          </div>
        </aside>

        <main class="main-content">
          <div class="toolbar">
            <div class="search-box">
              <input v-model="keyword" type="text" placeholder="搜索商品..." />
              <button @click="loadProducts">搜索</button>
            </div>
            <div class="sort-options">
              <select v-model="sortBy" @change="loadProducts">
                <option value="created_at">最新上架</option>
                <option value="goods_price">价格排序</option>
                <option value="goods_stars">评分排序</option>
                <option value="sales_count">销量排序</option>
              </select>
              <select v-model="sortOrder" @change="loadProducts">
                <option value="desc">降序</option>
                <option value="asc">升序</option>
              </select>
            </div>
            <div class="page-size">
              <span>每页</span>
              <select v-model="pageSize" @change="loadProducts">
                <option :value="12">12条</option>
                <option :value="24">24条</option>
                <option :value="36">36条</option>
                <option :value="48">48条</option>
              </select>
            </div>
          </div>

          <div v-if="products.length > 0" class="product-grid">
            <router-link v-for="product in products" :key="product.goods_id" :to="`/product/${product.goods_id}`" class="product-card">
              <img :src="product.goods_picture" :alt="product.goods_name" class="product-image" />
              <div class="product-info">
                <div class="product-header">
                  <span class="product-category">{{ product.goods_category }}</span>
                  <span class="product-rating">⭐ {{ product.goods_stars }}</span>
                </div>
                <h3>{{ product.goods_name }}</h3>
                <p class="product-intro">{{ product.goods_introduce }}</p>
                <div class="product-footer">
                  <span class="product-price">¥{{ product.goods_price }}</span>
                  <span class="product-producer">{{ product.goods_producer }}</span>
                </div>
              </div>
            </router-link>
          </div>

          <div v-else class="empty-state">
            <p>暂无商品</p>
          </div>

          <div v-if="pagination.total > pageSize" class="pagination">
            <button :disabled="pagination.currentPage === 1" @click="prevPage">上一页</button>
            <span>{{ pagination.currentPage }} / {{ pagination.totalPages }}</span>
            <button :disabled="pagination.currentPage === pagination.totalPages" @click="nextPage">下一页</button>
          </div>
        </main>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import api from '@/api'

const products = ref([])
const filters = reactive({
  categories: [],
  areas: [],
  seasons: []
})
const selectedCategory = ref('all')
const selectedArea = ref('all')
const selectedSeason = ref('all')
const keyword = ref('')
const minPrice = ref('')
const maxPrice = ref('')
const sortBy = ref('created_at')
const sortOrder = ref('desc')
const pageSize = ref(12)
const currentPage = ref(1)
const pagination = reactive({
  currentPage: 1,
  pageSize: 12,
  total: 0,
  totalPages: 0
})

function handleCategoryChange(category) {
  selectedCategory.value = category
  currentPage.value = 1
  loadProducts()
}

function handleAreaChange(area) {
  selectedArea.value = area
  currentPage.value = 1
  loadProducts()
}

function handleSeasonChange(season) {
  selectedSeason.value = season
  currentPage.value = 1
  loadProducts()
}

async function loadProducts() {
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      category: selectedCategory.value === 'all' ? '' : selectedCategory.value,
      area: selectedArea.value === 'all' ? '' : selectedArea.value,
      season: selectedSeason.value === 'all' ? '' : selectedSeason.value,
      keyword: keyword.value,
      minPrice: minPrice.value ? parseFloat(minPrice.value) : null,
      maxPrice: maxPrice.value ? parseFloat(maxPrice.value) : null,
      sortBy: sortBy.value,
      sortOrder: sortOrder.value
    }

    const response = await api.products.list(params)
    products.value = response.data?.products || []
    pagination.currentPage = response.data?.pagination?.currentPage || 1
    pagination.pageSize = response.data?.pagination?.pageSize || 12
    pagination.total = response.data?.pagination?.total || 0
    pagination.totalPages = response.data?.pagination?.totalPages || 0
  } catch (e) {
    console.error('获取商品列表失败', e)
  }
}

async function loadFilters() {
  try {
    const response = await api.products.filters()
    filters.categories = response.data?.categories || []
    filters.areas = response.data?.areas || []
    filters.seasons = response.data?.seasons || []
  } catch (e) {
    console.error('获取筛选选项失败', e)
  }
}

function prevPage() {
  if (currentPage.value > 1) {
    currentPage.value--
    loadProducts()
  }
}

function nextPage() {
  if (currentPage.value < pagination.totalPages) {
    currentPage.value++
    loadProducts()
  }
}

onMounted(() => {
  loadFilters()
  loadProducts()
})
</script>

<style scoped>
.market {
  padding: 30px 0;
}

.market-header {
  text-align: center;
  margin-bottom: 40px;
  background: url('https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=1600') center/cover no-repeat;
  color: white;
  min-height: 400px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
  position: relative;
}

.market-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(74, 140, 58, 0.7);
}

.market-header h1 {
  font-size: 32px;
  margin-bottom: 10px;
  position: relative;
  z-index: 1;
}

.market-header p {
  color: rgba(255, 255, 255, 0.9);
  position: relative;
  z-index: 1;
}

.market-content {
  display: flex;
  gap: 30px;
}

.sidebar {
  width: 200px;
  flex-shrink: 0;
}

.filter-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.filter-section h3 {
  font-size: 16px;
  margin-bottom: 15px;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.filter-btn {
  background: none;
  border: 1px solid #ddd;
  padding: 8px 12px;
  border-radius: 4px;
  text-align: left;
  font-size: 14px;
  transition: all 0.3s;
}

.filter-btn:hover {
  border-color: #4a8c3a;
  color: #4a8c3a;
}

.filter-btn.active {
  background: #4a8c3a;
  color: white;
  border-color: #4a8c3a;
}

.price-input {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 15px;
  overflow: hidden;
}

.price-input input {
  flex: 1;
  min-width: 0;
  padding: 6px 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 13px;
  max-width: 80px;
}

.price-input span {
  flex-shrink: 0;
}

.apply-btn {
  width: 100%;
  background: #4a8c3a;
  color: white;
  border: none;
  padding: 10px;
  border-radius: 4px;
}

.main-content {
  flex: 1;
}

.toolbar {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
}

.search-box {
  display: flex;
  gap: 10px;
}

.search-box input {
  width: 200px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.search-box button {
  background: #4a8c3a;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
}

.sort-options {
  display: flex;
  gap: 10px;
}

.sort-options select {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.page-size {
  display: flex;
  align-items: center;
  gap: 10px;
}

.page-size select {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.product-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s;
}

.product-card:hover {
  transform: translateY(-5px);
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.product-info {
  padding: 15px;
}

.product-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.product-category {
  font-size: 12px;
  color: #4a8c3a;
  background: #f0f7ed;
  padding: 3px 8px;
  border-radius: 4px;
}

.product-rating {
  font-size: 12px;
}

.product-info h3 {
  font-size: 16px;
  margin-bottom: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-intro {
  font-size: 13px;
  color: #666;
  margin-bottom: 10px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.product-price {
  font-size: 20px;
  font-weight: bold;
  color: #e74c3c;
}

.product-producer {
  font-size: 12px;
  color: #999;
}

.empty-state {
  text-align: center;
  padding: 50px;
  color: #999;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-top: 30px;
}

.pagination button {
  padding: 10px 20px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 4px;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination span {
  font-size: 16px;
}
</style>