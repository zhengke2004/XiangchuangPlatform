<template>
  <div class="activity-detail">
    <div class="container">
      <div v-if="activity" class="detail-content">
        <div class="activity-image">
          <img :src="activity.activity_image" :alt="activity.activity_name" />
        </div>
        <div class="activity-info">
          <h1>{{ activity.activity_name }}</h1>
          <div class="activity-rating">
            <span>⭐ {{ activity.activity_rating }}</span>
          </div>
          <p class="activity-desc">{{ activity.activity_description }}</p>
          <div class="activity-meta">
            <span class="meta-item">📍 {{ activity.activity_location }}</span>
            <span class="meta-item">⏱️ {{ activity.activity_duration }}</span>
            <span class="meta-item">👥 {{ activity.activity_capacity }}人</span>
            <span class="meta-item">🎯 {{ activity.activity_theme }}</span>
          </div>
          <div class="price-section">
            <span class="price">¥{{ activity.activity_price }}</span>
            <span class="unit">/人</span>
          </div>
          <div class="action-section">
            <button class="book-btn">立即预订</button>
          </div>
        </div>
      </div>

      <div class="booking-form">
        <h2>预订信息</h2>
        <form @submit.prevent="handleBooking" class="form">
          <div class="form-row">
            <label>选择日期</label>
            <input type="date" v-model="booking.date" />
          </div>
          <div class="form-row">
            <label>参与人数</label>
            <div class="quantity-control">
              <button type="button" @click="booking.participants = Math.max(1, booking.participants - 1)">-</button>
              <span>{{ booking.participants }}</span>
              <button type="button" @click="booking.participants++">+</button>
            </div>
          </div>
          <div class="form-row">
            <label>联系人</label>
            <input type="text" v-model="booking.contact" placeholder="请输入联系人姓名" />
          </div>
          <div class="form-row">
            <label>联系电话</label>
            <input type="tel" v-model="booking.phone" placeholder="请输入联系电话" />
          </div>
          <div class="form-row">
            <label>备注信息</label>
            <textarea v-model="booking.remark" placeholder="请输入备注信息"></textarea>
          </div>
          <button type="submit" class="submit-btn">确认预订</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import api from '@/api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const activity = ref(null)

const booking = reactive({
  date: '',
  participants: 1,
  contact: '',
  phone: '',
  remark: ''
})

async function loadActivity() {
  try {
    const response = await api.activities.detail(route.params.id)
    activity.value = response.data?.activity || null
  } catch (e) {
    console.error('获取活动详情失败', e)
  }
}

function handleBooking() {
  if (!booking.date || !booking.contact || !booking.phone) {
    ElMessage.warning('请填写完整预订信息')
    return
  }
  ElMessage.success('预订成功！')
}

onMounted(() => {
  loadActivity()
})
</script>

<style scoped>
.activity-detail {
  padding: 30px 0;
}

.detail-content {
  display: flex;
  gap: 40px;
  margin-bottom: 30px;
}

.activity-image {
  width: 500px;
  flex-shrink: 0;
}

.activity-image img {
  width: 100%;
  height: 400px;
  object-fit: cover;
  border-radius: 8px;
}

.activity-info {
  flex: 1;
}

.activity-info h1 {
  font-size: 28px;
  margin-bottom: 15px;
}

.activity-rating {
  font-size: 16px;
  margin-bottom: 15px;
}

.activity-desc {
  font-size: 16px;
  color: #666;
  margin-bottom: 20px;
}

.activity-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 20px;
}

.meta-item {
  font-size: 14px;
  color: #666;
}

.price-section {
  margin-bottom: 30px;
}

.price {
  font-size: 36px;
  font-weight: bold;
  color: #e74c3c;
}

.unit {
  font-size: 16px;
  color: #666;
}

.action-section {
  margin-bottom: 30px;
}

.book-btn {
  padding: 15px 40px;
  background: #4a8c3a;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: bold;
}

.booking-form {
  background: white;
  padding: 30px;
  border-radius: 8px;
}

.booking-form h2 {
  font-size: 24px;
  margin-bottom: 20px;
}

.form {
  display: flex;
  flex-direction: column;
}

.form-row {
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
}

.form-row label {
  font-size: 14px;
  margin-bottom: 8px;
}

.form-row input,
.form-row textarea {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.form-row textarea {
  height: 100px;
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: 15px;
  border: 1px solid #ddd;
  padding: 10px;
  width: fit-content;
}

.quantity-control button {
  width: 35px;
  height: 35px;
  font-size: 20px;
  border: none;
  background: none;
}

.quantity-control span {
  font-size: 18px;
  font-weight: bold;
}

.submit-btn {
  background: #4a8c3a;
  color: white;
  border: none;
  padding: 15px;
  border-radius: 4px;
  font-size: 16px;
  font-weight: bold;
}
</style>