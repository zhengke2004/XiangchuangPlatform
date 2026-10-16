<template>
  <div class="favorites">
    <div class="container">
      <div class="favorites-header">
        <h1>我的收藏</h1>
      </div>

      <div v-if="favorites.length > 0" class="favorites-list">
        <div v-for="item in favorites" :key="item.id" class="favorite-card">
          <router-link :to="`/product/${item.id}`" class="favorite-image">
            <img :src="item.image" :alt="item.name" />
          </router-link>
          <div class="favorite-info">
            <router-link :to="`/product/${item.id}`">
              <h3>{{ item.name }}</h3>
            </router-link>
            <p>{{ item.intro }}</p>
            <div class="favorite-footer">
              <span class="price">¥{{ item.price }}</span>
              <button class="add-cart-btn">加入购物车</button>
              <button class="remove-btn" @click="removeFavorite(item.id)">取消收藏</button>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="empty-state">
        <p>暂无收藏商品</p>
        <router-link to="/market" class="go-market-btn">去逛逛</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const favorites = ref([])

onMounted(() => {
  favorites.value = [
    {
      id: 1,
      name: '有机大米',
      intro: '来自优质稻田的有机大米，口感软糯香甜',
      price: 68.00,
      image: 'https://images.unsplash.com/photo-1558591710-4b4a1ae0f04d?w=200'
    },
    {
      id: 2,
      name: '农家土鸡蛋',
      intro: '散养土鸡产蛋，营养丰富，口感鲜美',
      price: 60.00,
      image: 'https://images.unsplash.com/photo-1516483638261-f4dbaf036963?w=200'
    }
  ]
})

function removeFavorite(id) {
  favorites.value = favorites.value.filter(item => item.id !== id)
  ElMessage.success('已取消收藏')
}
</script>

<style scoped>
.favorites {
  padding: 30px 0;
}

.favorites-header {
  margin-bottom: 20px;
}

.favorites-header h1 {
  font-size: 28px;
}

.favorites-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.favorite-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  display: flex;
  gap: 20px;
}

.favorite-image {
  width: 150px;
  flex-shrink: 0;
}

.favorite-image img {
  width: 100%;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
}

.favorite-info {
  flex: 1;
}

.favorite-info h3 {
  font-size: 18px;
  margin-bottom: 8px;
}

.favorite-info p {
  font-size: 14px;
  color: #666;
  margin-bottom: 15px;
}

.favorite-footer {
  display: flex;
  align-items: center;
  gap: 15px;
}

.price {
  font-size: 20px;
  font-weight: bold;
  color: #e74c3c;
}

.add-cart-btn {
  padding: 8px 20px;
  border: 1px solid #4a8c3a;
  color: #4a8c3a;
  background: white;
  border-radius: 4px;
  font-size: 14px;
}

.remove-btn {
  padding: 8px 20px;
  border: none;
  color: #999;
  background: #f8f9fa;
  border-radius: 4px;
  font-size: 14px;
}

.empty-state {
  text-align: center;
  padding: 50px;
  background: white;
  border-radius: 8px;
}

.empty-state p {
  color: #999;
  margin-bottom: 20px;
}

.go-market-btn {
  background: #4a8c3a;
  color: white;
  padding: 10px 30px;
  border-radius: 4px;
}
</style>