<template>
  <div class="travel">
    <div class="container">
      <div class="travel-header">
        <h1>旅游定制</h1>
        <p>定制您的专属乡村体验，智能规划路线，轻松预订服务</p>
        <p>从农耕体验到民宿生活，从节庆活动到文化探索，我们为您打造独一无二的乡村之旅</p>
      </div>

      <div class="recommendations">
        <h2>热门推荐</h2>
        <div v-if="recommendations.length > 0" class="activity-grid">
          <div v-for="activity in recommendations" :key="activity.activity_id" class="activity-card">
            <h3>{{ activity.activity_name }}</h3>
            <p class="activity-location">{{ activity.activity_location }}</p>
            <p class="activity-price">¥{{ activity.activity_price }}</p>
            <button class="select-btn" @click="selectActivity(activity)">选择</button>
          </div>
        </div>
      </div>

      <div class="travel-content">
        <div class="activity-section">
          <h2>选择旅游主题</h2>
          <div v-if="activities.length > 0" class="activity-list">
            <div v-for="activity in activities" :key="activity.activity_id" class="activity-item" :class="{ selected: selectedActivities.includes(activity.activity_id) }">
              <div class="activity-info">
                <h3>{{ activity.activity_name }}</h3>
                <p>{{ activity.activity_description }}</p>
                <div class="activity-meta">
                  <span>📍 {{ activity.activity_location }}</span>
                  <span>⏱️ {{ activity.activity_duration }}</span>
                  <span>⭐ {{ activity.activity_rating }}</span>
                </div>
              </div>
              <div class="activity-action">
                <span class="price">¥{{ activity.activity_price }}</span>
                <button v-if="!selectedActivities.includes(activity.activity_id)" @click="selectActivity(activity)">选择</button>
                <button v-else @click="deselectActivity(activity.activity_id)">已选择</button>
              </div>
            </div>
          </div>
          <div v-else class="loading">加载中...</div>
        </div>

        <div class="booking-section">
          <h2>预订详情</h2>
          <div class="booking-card">
            <div class="selected-activities">
              <h3>已选活动</h3>
              <div v-if="selectedActivities.length > 0" class="selected-list">
                <div v-for="activity in getSelectedActivityDetails" :key="activity.activity_id" class="selected-item">
                  <span>{{ activity.activity_name }}</span>
                  <span>¥{{ activity.activity_price }}</span>
                  <button @click="deselectActivity(activity.activity_id)">×</button>
                </div>
              </div>
              <div v-else class="empty">请先选择活动项目</div>
            </div>

            <div class="participants">
              <h3>参与人数</h3>
              <div class="quantity-control">
                <button @click="participants = Math.max(1, participants - 1)">-</button>
                <span>{{ participants }}</span>
                <button @click="participants++">+</button>
              </div>
            </div>

            <div class="services">
              <h3>附加服务</h3>
              <label>
                <input type="checkbox" v-model="services.guide" />
                专业导游服务 ¥200/天
              </label>
              <label>
                <input type="checkbox" v-model="services.transport" />
                专车接送服务 ¥300/天
              </label>
              <label>
                <input type="checkbox" v-model="services.photo" />
                旅拍摄影服务 ¥500/套
              </label>
              <label>
                <input type="checkbox" v-model="services.insurance" />
                旅游保险 ¥30/人
              </label>
            </div>

            <div class="cost-summary">
              <div class="cost-item">
                <span>活动费用</span>
                <span>¥{{ costResult.activitiesCost || 0 }}</span>
              </div>
              <div class="cost-item">
                <span>附加服务</span>
                <span>¥{{ costResult.servicesCost || 0 }}</span>
              </div>
              <div class="cost-item">
                <span>交通费用</span>
                <span>¥{{ costResult.transportCost || 0 }}</span>
              </div>
              <div class="cost-total">
                <span>总费用</span>
                <span>¥{{ costResult.total || 0 }}</span>
              </div>
            </div>

            <button class="book-btn" :disabled="selectedActivities.length === 0" @click="book">立即预订</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import api from '@/api'
import { ElMessage } from 'element-plus'

const activities = ref([])
const recommendations = ref([])
const selectedActivities = ref([])
const participants = ref(1)
const services = reactive({
  guide: false,
  transport: false,
  photo: false,
  insurance: false
})
const costResult = reactive({
  activitiesCost: 0,
  servicesCost: 0,
  transportCost: 0,
  total: 0
})

const getSelectedActivityDetails = computed(() => {
  return activities.value.filter(a => selectedActivities.value.includes(a.activity_id))
})

async function loadActivities() {
  try {
    const response = await api.activities.theme('all')
    activities.value = response.data || []
  } catch (e) {
    console.error('获取活动列表失败', e)
  }
}

async function loadRecommendations() {
  try {
    const response = await api.activities.recommendations(3)
    recommendations.value = response.data || []
  } catch (e) {
    console.error('获取推荐活动失败', e)
  }
}

function selectActivity(activity) {
  if (!selectedActivities.value.includes(activity.activity_id)) {
    selectedActivities.value.push(activity.activity_id)
  }
  calculateCost()
}

function deselectActivity(activityId) {
  selectedActivities.value = selectedActivities.value.filter(id => id !== activityId)
  calculateCost()
}

async function calculateCost() {
  if (selectedActivities.value.length === 0) {
    costResult.activitiesCost = 0
    costResult.servicesCost = 0
    costResult.transportCost = 0
    costResult.total = 0
    return
  }

  try {
    const response = await api.activities.calculate({
      activityIds: selectedActivities.value,
      services: services,
      participants: participants.value
    })
    const data = response.data || {}
    costResult.activitiesCost = data.activitiesCost || 0
    costResult.servicesCost = data.servicesCost || 0
    costResult.transportCost = data.transportCost || 0
    costResult.total = data.total || 0
  } catch (e) {
    console.error('费用计算失败', e)
  }
}

function book() {
  if (selectedActivities.value.length === 0) {
    ElMessage.warning('请先选择活动项目')
    return
  }
  ElMessage.success('预订成功！订单已提交')
}

onMounted(() => {
  loadActivities()
  loadRecommendations()
})
</script>

<style scoped>
.travel {
  padding: 30px 0;
}

.travel-header {
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

.travel-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(74, 140, 58, 0.7);
}

.travel-header h1 {
  font-size: 36px;
  margin-bottom: 15px;
  position: relative;
  z-index: 1;
}

.travel-header p {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 10px;
  position: relative;
  z-index: 1;
}

.recommendations {
  margin-bottom: 40px;
}

.recommendations h2 {
  font-size: 24px;
  margin-bottom: 20px;
}

.activity-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.activity-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.activity-card h3 {
  font-size: 18px;
  margin-bottom: 8px;
}

.activity-location {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
}

.activity-price {
  font-size: 24px;
  font-weight: bold;
  color: #e74c3c;
  margin-bottom: 15px;
}

.select-btn {
  background: #4a8c3a;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
}

.travel-content {
  display: flex;
  gap: 30px;
}

.activity-section {
  flex: 2;
}

.activity-section h2 {
  font-size: 24px;
  margin-bottom: 20px;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.activity-item {
  background: white;
  padding: 20px;
  border-radius: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border: 2px solid transparent;
  transition: border-color 0.3s;
}

.activity-item.selected {
  border-color: #4a8c3a;
}

.activity-info h3 {
  font-size: 18px;
  margin-bottom: 8px;
}

.activity-info p {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
}

.activity-meta {
  display: flex;
  gap: 20px;
  font-size: 13px;
  color: #999;
}

.activity-action {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 10px;
}

.activity-action .price {
  font-size: 20px;
  font-weight: bold;
  color: #e74c3c;
}

.activity-action button {
  padding: 10px 20px;
  border-radius: 4px;
  border: none;
}

.activity-action button:nth-child(2) {
  background: #4a8c3a;
  color: white;
}

.activity-action button:last-child {
  background: #eee;
  color: #666;
}

.booking-section {
  flex: 1;
}

.booking-section h2 {
  font-size: 24px;
  margin-bottom: 20px;
}

.booking-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
}

.booking-card h3 {
  font-size: 16px;
  margin-bottom: 15px;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}

.selected-activities,
.participants,
.services,
.cost-summary {
  margin-bottom: 20px;
}

.selected-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.selected-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 4px;
}

.selected-item button {
  background: none;
  border: none;
  color: #999;
  font-size: 20px;
  cursor: pointer;
}

.empty {
  color: #999;
  text-align: center;
  padding: 20px;
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: 20px;
}

.quantity-control button {
  width: 40px;
  height: 40px;
  font-size: 20px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 4px;
}

.quantity-control span {
  font-size: 20px;
  font-weight: bold;
}

.services label {
  display: block;
  margin-bottom: 10px;
  font-size: 14px;
}

.services input {
  margin-right: 10px;
}

.cost-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.cost-total {
  display: flex;
  justify-content: space-between;
  padding-top: 15px;
  border-top: 1px solid #eee;
  font-size: 18px;
  font-weight: bold;
}

.book-btn {
  width: 100%;
  background: #4a8c3a;
  color: white;
  border: none;
  padding: 15px;
  border-radius: 4px;
  font-size: 16px;
  font-weight: bold;
}

.book-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>