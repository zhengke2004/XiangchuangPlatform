<template>
  <div class="guide">
    <div class="container">
      <div class="guide-header">
        <h1>资讯指南</h1>
        <p>获取最新乡村资讯，了解乡村振兴政策，学习农技知识</p>
      </div>

      <div class="category-tabs">
        <button class="category-btn" :class="{ active: selectedCategory === 'all' }" @click="selectedCategory = 'all'">全部</button>
        <button class="category-btn" :class="{ active: selectedCategory === 'policy' }" @click="selectedCategory = 'policy'">政策解读</button>
        <button class="category-btn" :class="{ active: selectedCategory === 'tech' }" @click="selectedCategory = 'tech'">农技知识</button>
        <button class="category-btn" :class="{ active: selectedCategory === 'travel' }" @click="selectedCategory = 'travel'">旅游攻略</button>
        <button class="category-btn" :class="{ active: selectedCategory === 'festival' }" @click="selectedCategory = 'festival'">节庆资讯</button>
      </div>

      <div class="search-box">
        <input v-model="keyword" type="text" placeholder="搜索资讯..." />
        <button @click="search">搜索</button>
      </div>

      <div class="article-list">
        <div v-for="article in articles" :key="article.id" class="article-card">
          <router-link :to="`/guide/${article.id}`" class="article-image">
            <img :src="article.image" :alt="article.title" />
          </router-link>
          <div class="article-info">
            <router-link :to="`/guide/${article.id}`">
              <h3>{{ article.title }}</h3>
            </router-link>
            <p class="article-summary">{{ article.summary }}</p>
            <div class="article-meta">
              <span class="category-tag">{{ article.category }}</span>
              <span class="author">{{ article.author }}</span>
              <span class="date">{{ article.date }}</span>
              <span class="views">👁️ {{ article.views }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="pagination">
        <button :disabled="currentPage === 1" @click="prevPage">上一页</button>
        <span>{{ currentPage }} / {{ totalPages }}</span>
        <button :disabled="currentPage === totalPages" @click="nextPage">下一页</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const articles = ref([
  {
    id: 1,
    title: '乡村振兴政策解读：如何助力农业现代化发展',
    summary: '本文深入解读国家乡村振兴战略，分析政策如何推动农业现代化，助力农村经济发展',
    category: '政策解读',
    author: '管理员',
    date: '2025-01-15',
    views: 2345,
    image: 'https://images.unsplash.com/photo-1568702846914-96b305d2aaeb?w=300'
  },
  {
    id: 2,
    title: '有机农业种植技术指南',
    summary: '详细介绍有机农业的种植技术和管理方法，帮助农户实现绿色生产',
    category: '农技知识',
    author: '农业专家',
    date: '2025-01-14',
    views: 1890,
    image: 'https://images.unsplash.com/photo-1567306226416-28f0efdc88ce?w=300'
  },
  {
    id: 3,
    title: '春季乡村旅游攻略推荐',
    summary: '春季是乡村旅游的最佳时节，推荐几个热门乡村旅游目的地和游玩攻略',
    category: '旅游攻略',
    author: '旅游达人',
    date: '2025-01-13',
    views: 3456,
    image: 'https://images.unsplash.com/photo-1501555088652-021faa106b9b?w=300'
  },
  {
    id: 4,
    title: '乡村节庆活动大盘点',
    summary: '盘点全国各地的乡村节庆活动，感受传统文化的魅力',
    category: '节庆资讯',
    author: '文化爱好者',
    date: '2025-01-12',
    views: 1234,
    image: 'https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=300'
  },
  {
    id: 5,
    title: '农产品电商销售技巧',
    summary: '分享农产品电商销售的实用技巧，帮助农户拓展销售渠道',
    category: '农技知识',
    author: '电商专家',
    date: '2025-01-11',
    views: 2156,
    image: 'https://images.unsplash.com/photo-1587049352847-4a222e784d38?w=300'
  }
])

const selectedCategory = ref('all')
const keyword = ref('')
const currentPage = ref(1)
const totalPages = ref(1)

function search() {
  console.log('搜索:', keyword.value)
}

function prevPage() {
  if (currentPage.value > 1) {
    currentPage.value--
  }
}

function nextPage() {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
  }
}

onMounted(() => {})
</script>

<style scoped>
.guide {
  padding: 30px 0;
}

.guide-header {
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

.guide-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(74, 140, 58, 0.7);
}

.guide-header h1 {
  font-size: 36px;
  margin-bottom: 15px;
  position: relative;
  z-index: 1;
}

.guide-header p {
  color: rgba(255, 255, 255, 0.9);
  position: relative;
  z-index: 1;
}

.category-tabs {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-bottom: 30px;
  flex-wrap: wrap;
}

.category-btn {
  background: white;
  border: 1px solid #ddd;
  padding: 8px 20px;
  border-radius: 4px;
  font-size: 14px;
  color: #333;
  cursor: pointer;
  transition: all 0.3s;
}

.category-btn:hover,
.category-btn.active {
  background: #4a8c3a;
  color: white;
  border-color: #4a8c3a;
}

.search-box {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.search-box input {
  flex: 1;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.search-box button {
  background: #4a8c3a;
  color: white;
  border: none;
  padding: 12px 20px;
  border-radius: 4px;
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.article-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  display: flex;
  gap: 20px;
}

.article-image {
  width: 200px;
  flex-shrink: 0;
}

.article-image img {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 8px;
}

.article-info {
  flex: 1;
}

.article-info h3 {
  font-size: 18px;
  margin-bottom: 10px;
}

.article-summary {
  font-size: 14px;
  color: #666;
  margin-bottom: 15px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-meta {
  display: flex;
  gap: 15px;
  font-size: 13px;
  color: #999;
}

.category-tag {
  background: #4a8c3a;
  color: white;
  padding: 3px 8px;
  border-radius: 4px;
  font-size: 12px;
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
</style>

