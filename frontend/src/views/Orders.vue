<template>
  <div class="orders">
    <div class="container">
      <div class="orders-header">
        <h1>我的订单</h1>
      </div>

      <div class="tabs">
        <button class="tab-btn" :class="{ active: activeTab === 'all' }" @click="activeTab = 'all'">全部</button>
        <button class="tab-btn" :class="{ active: activeTab === '待处理' }" @click="activeTab = '待处理'">待处理</button>
        <button class="tab-btn" :class="{ active: activeTab === '进行中' }" @click="activeTab = '进行中'">进行中</button>
        <button class="tab-btn" :class="{ active: activeTab === '已完成' }" @click="activeTab = '已完成'">已完成</button>
        <button class="tab-btn" :class="{ active: activeTab === '已取消' }" @click="activeTab = '已取消'">已取消</button>
      </div>

      <div v-if="filteredOrders.length > 0" class="orders-list">
        <div v-for="order in filteredOrders" :key="order.orderId" class="order-card">
          <div class="order-header">
            <span class="order-id">订单号：{{ order.orderNo }}</span>
            <span class="order-status">{{ order.orderStatus }}</span>
            <span class="order-date">{{ formatDate(order.createdAt) }}</span>
          </div>
          <div class="order-items">
            <div v-for="item in order.items" :key="item.itemId" class="order-item">
              <div class="item-info">
                <h3>{{ item.itemName }}</h3>
                <div class="item-footer">
                  <span class="price">¥{{ item.price }}</span>
                  <span class="quantity">×{{ item.quantity }}</span>
                  <span class="subtotal">小计 ¥{{ item.subtotal }}</span>
                </div>
              </div>
            </div>
          </div>
          <div class="order-footer">
            <span class="payment">{{ order.paymentStatus }}</span>
            <span class="total">合计：¥{{ order.totalAmount }}</span>
          </div>
        </div>
      </div>

      <div v-else class="empty-state">
        <p>暂无订单</p>
        <router-link to="/market" class="go-market-btn">去逛逛</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '@/api'

const orders = ref([])
const activeTab = ref('all')

const filteredOrders = computed(() => {
  if (activeTab.value === 'all') return orders.value
  return orders.value.filter(o => o.orderStatus === activeTab.value)
})

function formatDate(dateStr) {
  if (!dateStr) return ''
  return String(dateStr).replace('T', ' ').substring(0, 16)
}

async function loadOrders() {
  try {
    const response = await api.orders.list()
    orders.value = response.data || []
  } catch (e) {
    console.error('获取订单失败', e)
    orders.value = []
  }
}

onMounted(loadOrders)
</script>

<style scoped>
.orders {
  padding: 30px 0;
}

.orders-header {
  margin-bottom: 20px;
}

.orders-header h1 {
  font-size: 28px;
}

.tabs {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.tab-btn {
  padding: 10px 20px;
  border: none;
  background: white;
  border-radius: 4px;
  font-size: 14px;
}

.tab-btn.active {
  background: #4a8c3a;
  color: white;
}

.orders-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background: #f8f9fa;
  border-bottom: 1px solid #eee;
}

.order-id {
  font-size: 14px;
}

.order-status {
  font-size: 14px;
  font-weight: bold;
  color: #4a8c3a;
}

.order-date {
  font-size: 14px;
  color: #999;
}

.order-items {
  padding: 15px 20px;
}

.order-item {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
}

.order-item:last-child {
  margin-bottom: 0;
}

.item-info {
  flex: 1;
}

.item-info h3 {
  font-size: 16px;
  margin-bottom: 5px;
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

.quantity {
  font-size: 14px;
  color: #666;
}

.subtotal {
  font-size: 14px;
  color: #666;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background: #f8f9fa;
  border-top: 1px solid #eee;
}

.payment {
  font-size: 14px;
  color: #f5a623;
}

.total {
  font-size: 16px;
  font-weight: bold;
}

.empty-state {
  text-align: center;
  padding: 50px;
  background: white;
  border-radius: 8px;
  color: #999;
}

.empty-state p {
  margin-bottom: 20px;
}

.go-market-btn {
  display: inline-block;
  background: #4a8c3a;
  color: white;
  padding: 10px 30px;
  border-radius: 4px;
}
</style>
