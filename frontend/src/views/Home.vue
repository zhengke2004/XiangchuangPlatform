<template>
  <div class="home">
    <section class="hero">
      <div class="hero-content">
        <h1>农游农家乐平台</h1>
        <p>连接城乡，体验田园，助力乡村振兴</p>
        <p>一站式乡村体验和农创服务平台，为您提供地道的农产品、深度的农旅体验和丰富的乡创资讯</p>
        <div class="hero-buttons">
          <router-link to="/market" class="btn-primary">开始选购</router-link>
          <router-link to="/travel" class="btn-secondary">规划行程</router-link>
        </div>
      </div>
    </section>

    <section class="features">
      <div class="container">
        <h2>快速访问</h2>
        <div class="feature-grid">
          <router-link to="/market" class="feature-card">
            <div class="feature-icon">🛒</div>
            <h3>农品市集</h3>
            <p>购买地道乡村特产</p>
          </router-link>
          <router-link to="/travel" class="feature-card">
            <div class="feature-icon">🌾</div>
            <h3>旅游定制</h3>
            <p>规划专属乡村游</p>
          </router-link>
          <router-link to="/guide" class="feature-card">
            <div class="feature-icon">📰</div>
            <h3>资讯指南</h3>
            <p>获取最新乡村资讯</p>
          </router-link>
          <router-link to="/account" class="feature-card">
            <div class="feature-icon">👤</div>
            <h3>个人账户</h3>
            <p>管理您的订单收藏</p>
          </router-link>
        </div>
      </div>
    </section>

    <section class="recommendations">
      <div class="container">
        <h2>热门推荐</h2>
        <div v-if="products.length > 0" class="product-grid">
          <router-link v-for="product in products" :key="product.goods_id" :to="`/product/${product.goods_id}`" class="product-card">
            <img :src="product.goods_picture" :alt="product.goods_name" class="product-image" />
            <div class="product-info">
              <h3>{{ product.goods_name }}</h3>
              <p class="product-price">¥{{ product.goods_price }}</p>
              <p class="product-area">{{ product.goods_area }}</p>
            </div>
          </router-link>
        </div>
        <div v-else class="loading">
          <p>加载中...</p>
        </div>
      </div>
    </section>

    <section class="stats-section">
      <div class="container">
        <div class="row">
          <div class="col-md-3">
            <div class="stat-item">
              <div class="stat-number">500+</div>
              <div class="stat-label">合作农户</div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="stat-item">
              <div class="stat-number">2000+</div>
              <div class="stat-label">特色农产品</div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="stat-item">
              <div class="stat-number">150+</div>
              <div class="stat-label">旅游路线</div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="stat-item">
              <div class="stat-number">10000+</div>
              <div class="stat-label">满意用户</div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="about">
      <div class="container">
        <h2>开始您的乡村之旅</h2>
        <p>无论是购买地道农产品，还是规划深度乡村游，我们都能为您提供最贴心的服务</p>
        <div class="about-grid">
          <div class="about-item">
            <div class="about-icon">🤖</div>
            <h3>智能推荐</h3>
            <p>基于AI算法为您推荐个性化农产品和旅游路线</p>
          </div>
          <div class="about-item">
            <div class="about-icon">🌱</div>
            <h3>溯源系统</h3>
            <p>每一件农产品都有完整的生产溯源信息</p>
          </div>
          <div class="about-item">
            <div class="about-icon">🤝</div>
            <h3>乡创支持</h3>
            <p>为乡村创业项目提供展示和对接平台</p>
          </div>
        </div>
      </div>
    </section>

    <section class="news-section">
      <div class="container">
        <div class="news-card">
          <h2>最新资讯</h2>
          <div class="news-list">
            <div class="news-item">
              <h3>2024年乡村民宿改造补贴政策发布</h3>
              <div class="news-meta">
                <span>📅 2024-03-15</span>
                <span>👁️ 1245浏览</span>
              </div>
            </div>
            <div class="news-item">
              <h3>春季农耕体验活动开始报名</h3>
              <div class="news-meta">
                <span>📅 2024-03-12</span>
                <span>👁️ 892浏览</span>
              </div>
            </div>
            <div class="news-item">
              <h3>有机农产品认证新标准解读</h3>
              <div class="news-meta">
                <span>📅 2024-03-10</span>
                <span>👁️ 1567浏览</span>
              </div>
            </div>
          </div>
          <div class="news-more">
            <router-link to="/guide">查看更多资讯</router-link>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api'

const products = ref([])

onMounted(async () => {
  try {
    const response = await api.products.recommendations(6)
    products.value = response.data || []
  } catch (e) {
    console.error('获取推荐商品失败', e)
  }
})
</script>

<style scoped>
.home {
  min-height: calc(100vh - 60px);
  padding: 30px 0;
}

.hero {
  background: url('https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=1600') center/cover no-repeat;
  color: white;
  min-height: 400px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
  text-align: center;
  position: relative;
}

.hero::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(74, 140, 58, 0.7);
}

.hero-content {
  max-width: 800px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

.hero h1 {
  font-size: 48px;
  margin-bottom: 20px;
}

.hero p {
  font-size: 18px;
  margin-bottom: 15px;
  opacity: 0.9;
}

.hero-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 30px;
}

.btn-primary,
.btn-secondary {
  padding: 12px 30px;
  border-radius: 6px;
  font-size: 16px;
  font-weight: bold;
}

.btn-primary {
  background: white;
  color: #4a8c3a;
}

.btn-secondary {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 2px solid white;
}

.features {
  padding: 60px 0;
}

.features h2 {
  text-align: center;
  font-size: 32px;
  margin-bottom: 40px;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.feature-card {
  background: white;
  padding: 30px;
  border-radius: 8px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  font-size: 40px;
  margin-bottom: 15px;
}

.feature-card h3 {
  font-size: 18px;
  margin-bottom: 10px;
}

.feature-card p {
  color: #666;
  font-size: 14px;
}

.recommendations {
  padding: 60px 0;
  background: #f8f9fa;
}

.recommendations h2 {
  text-align: center;
  font-size: 32px;
  margin-bottom: 40px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 20px;
}

.product-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.product-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
}

.product-info {
  padding: 15px;
}

.product-info h3 {
  font-size: 14px;
  margin-bottom: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-price {
  font-size: 18px;
  font-weight: bold;
  color: #e74c3c;
  margin-bottom: 5px;
}

.product-area {
  font-size: 12px;
  color: #999;
}

.about {
  padding: 60px 0;
  text-align: center;
}

.about h2 {
  font-size: 32px;
  margin-bottom: 20px;
}

.about p {
  font-size: 16px;
  color: #666;
  margin-bottom: 40px;
}

.about-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
}

.about-item {
  background: white;
  padding: 40px 30px;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.about-icon {
  font-size: 48px;
  margin-bottom: 20px;
}

.about-item h3 {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
}

.about-item p {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
}

.stats-section {
  padding: 40px 0;
}

.stats-section .container {
  background: rgb(56, 110, 46);
  padding: 50px 20px;
  border-radius: 16px;
}

.stats-section .row {
  display: flex;
  flex-wrap: wrap;
  margin: 0 -15px;
}

.stats-section .col-md-3 {
  flex: 0 0 25%;
  max-width: 25%;
  padding: 0 15px;
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 48px;
  font-weight: bold;
  color: white;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.8);
}

@media (max-width: 768px) {
  .stat-number {
    font-size: 36px;
  }
}

.news-section {
  padding: 40px 0;
}

.news-card {
  background: white;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.news-card h2 {
  font-size: 30px;
  font-weight: 800;
  color: #333;
  margin-bottom: 20px;
}

.news-list {
  margin-bottom: 20px;
}

.news-item {
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

.news-item:last-child {
  border-bottom: none;
}

.news-item h3 {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin-bottom: 8px;
  cursor: pointer;
  transition: color 0.3s;
}

.news-item h3:hover {
  color: #4a8c3a;
}

.news-meta {
  display: flex;
  gap: 20px;
  font-size: 13px;
  color: #999;
}

.news-more {
  text-align: center;
}

.news-more a {
  display: inline-block;
  padding: 10px 30px;
  border: 1px solid #4a8c3a;
  color: #4a8c3a;
  border-radius: 20px;
  font-size: 14px;
  transition: all 0.3s;
}

.news-more a:hover {
  background: #4a8c3a;
  color: white;
}
</style>