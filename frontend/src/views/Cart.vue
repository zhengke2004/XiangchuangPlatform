<template>
  <div class="cart">
    <div class="container">
      <div class="cart-header">
        <h1>购物车</h1>
        <button class="clear-btn" @click="clearCart">清空购物车</button>
      </div>

      <div v-if="cartItems.length > 0" class="cart-content">
        <div class="cart-list">
          <div v-for="item in cartItems" :key="item.id" class="cart-item">
            <input type="checkbox" v-model="item.selected" class="checkbox" />
            <router-link :to="`/product/${item.id}`" class="item-image">
              <img :src="item.image" :alt="item.name" />
            </router-link>
            <div class="item-info">
              <router-link :to="`/product/${item.id}`">
                <h3>{{ item.name }}</h3>
              </router-link>
              <p>{{ item.spec }}</p>
              <div class="item-footer">
                <span class="price">¥{{ item.price }}</span>
                <div class="quantity-control">
                  <button @click="decreaseQuantity(item)">-</button>
                  <span>{{ item.quantity }}</span>
                  <button @click="increaseQuantity(item)">+</button>
                </div>
                <span class="subtotal">¥{{ item.price * item.quantity }}</span>
                <button class="delete-btn" @click="removeItem(item.id)">删除</button>
              </div>
            </div>
          </div>
        </div>

        <div class="cart-summary">
          <div class="summary-header">
            <label class="select-all">
              <input type="checkbox" v-model="selectAll" @change="toggleSelectAll" />
              全选
            </label>
            <span>已选 {{ selectedCount }} 件商品</span>
          </div>
          <div class="summary-total">
            <span>合计：</span>
            <span class="total-price">¥{{ totalPrice }}</span>
          </div>
          <button class="checkout-btn">去结算</button>
        </div>
      </div>

      <div v-else class="empty-state">
        <p>购物车是空的</p>
        <router-link to="/market" class="go-market-btn">去购物</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const cartItems = ref([])
const selectAll = ref(false)

onMounted(() => {
  cartItems.value = [
    {
      id: 1,
      name: '有机大米',
      spec: '5kg/袋',
      price: 68.00,
      quantity: 2,
      image: 'https://images.unsplash.com/photo-1558591710-4b4a1ae0f04d?w=100',
      selected: true
    },
    {
      id: 2,
      name: '农家鸡蛋',
      spec: '30枚/盒',
      price: 60.00,
      quantity: 1,
      image: 'https://images.unsplash.com/photo-1516483638261-f4dbaf036963?w=100',
      selected: false
    }
  ]
})

const selectedCount = computed(() => {
  return cartItems.value.filter(item => item.selected).length
})

const totalPrice = computed(() => {
  return cartItems.value
    .filter(item => item.selected)
    .reduce((sum, item) => sum + item.price * item.quantity, 0)
    .toFixed(2)
})

function toggleSelectAll() {
  cartItems.value.forEach(item => {
    item.selected = selectAll.value
  })
}

function increaseQuantity(item) {
  item.quantity++
}

function decreaseQuantity(item) {
  if (item.quantity > 1) {
    item.quantity--
  }
}

function removeItem(id) {
  cartItems.value = cartItems.value.filter(item => item.id !== id)
  ElMessage.success('已删除')
}

function clearCart() {
  cartItems.value = []
  ElMessage.success('购物车已清空')
}
</script>

<style scoped>
.cart {
  padding: 30px 0;
}

.cart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.cart-header h1 {
  font-size: 28px;
}

.clear-btn {
  padding: 8px 20px;
  border: none;
  color: #999;
  background: #f8f9fa;
  border-radius: 4px;
}

.cart-content {
  display: flex;
  gap: 20px;
}

.cart-list {
  flex: 3;
}

.cart-item {
  background: white;
  padding: 20px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 15px;
}

.checkbox {
  width: 20px;
  height: 20px;
}

.item-image {
  width: 100px;
  flex-shrink: 0;
}

.item-image img {
  width: 100%;
  height: 80px;
  object-fit: cover;
  border-radius: 8px;
}

.item-info {
  flex: 1;
}

.item-info h3 {
  font-size: 16px;
  margin-bottom: 5px;
}

.item-info p {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
}

.item-footer {
  display: flex;
  align-items: center;
  gap: 20px;
}

.price {
  font-size: 16px;
  font-weight: bold;
  color: #e74c3c;
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: 10px;
  border: 1px solid #ddd;
  padding: 5px;
}

.quantity-control button {
  width: 30px;
  height: 30px;
  font-size: 16px;
  border: none;
  background: none;
}

.quantity-control span {
  font-size: 16px;
}

.subtotal {
  font-size: 16px;
  font-weight: bold;
  color: #e74c3c;
}

.delete-btn {
  padding: 8px 16px;
  border: none;
  color: #999;
  background: #f8f9fa;
  border-radius: 4px;
}

.cart-summary {
  flex: 1;
  background: white;
  padding: 20px;
  border-radius: 8px;
  height: fit-content;
}

.summary-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.select-all {
  display: flex;
  align-items: center;
  gap: 10px;
}

.summary-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.total-price {
  font-size: 28px;
  font-weight: bold;
  color: #e74c3c;
}

.checkout-btn {
  width: 100%;
  background: #4a8c3a;
  color: white;
  border: none;
  padding: 15px;
  border-radius: 4px;
  font-size: 16px;
  font-weight: bold;
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