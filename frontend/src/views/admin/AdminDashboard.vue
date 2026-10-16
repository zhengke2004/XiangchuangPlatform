<template>
  <div class="dashboard">
    <el-row :gutter="16">
      <el-col :span="8" v-for="card in topCards" :key="card.label">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-value" :style="{ color: card.color }">{{ card.value }}</div>
          <div class="stat-label">{{ card.label }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="second-row">
      <el-col :span="6" v-for="card in subCards" :key="card.label">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-value small" :style="{ color: card.color }">{{ card.value }}</div>
          <div class="stat-label">{{ card.label }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="table-card">
      <template #header>
        <span>最近订单</span>
      </template>
      <el-table :data="stats.recentOrders || []" stripe>
        <el-table-column prop="orderNo" label="订单号" min-width="200" />
        <el-table-column prop="orderType" label="类型" width="90" />
        <el-table-column prop="totalAmount" label="金额(元)" width="110">
          <template #default="{ row }">¥{{ row.totalAmount }}</template>
        </el-table-column>
        <el-table-column prop="paymentStatus" label="支付状态" width="100">
          <template #default="{ row }">
            <el-tag :type="payTagType(row.paymentStatus)" size="small">{{ row.paymentStatus }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orderStatus" label="订单状态" width="100">
          <template #default="{ row }">
            <el-tag :type="orderTagType(row.orderStatus)" size="small">{{ row.orderStatus }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="contactName" label="联系人" width="110" />
        <el-table-column prop="createdAt" label="下单时间" min-width="170">
          <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!(stats.recentOrders && stats.recentOrders.length)" description="暂无订单数据" :image-size="60" />
    </el-card>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import api from '@/api'

const stats = ref({})

const topCards = computed(() => [
  { label: '用户总数', value: stats.value.userCount ?? '-', color: '#3b7a3b' },
  { label: '商品总数', value: stats.value.goodsCount ?? '-', color: '#b88230' },
  { label: '活动总数', value: stats.value.activityCount ?? '-', color: '#3a6ea5' }
])

const subCards = computed(() => [
  { label: '订单总数', value: stats.value.orderCount ?? '-', color: '#7a3b3b' },
  { label: '待处理订单', value: stats.value.pendingOrders ?? '-', color: '#b85630' },
  { label: '评价总数', value: stats.value.reviewCount ?? '-', color: '#6a4b9a' },
  { label: '已付款总额(元)', value: stats.value.totalRevenue ?? '-', color: '#2c6e49' }
])

function payTagType(status) {
  return { 已付款: 'success', 已退款: 'info', 支付失败: 'danger' }[status] || 'warning'
}

function orderTagType(status) {
  return { 已完成: 'success', 进行中: 'primary', 已取消: 'info' }[status] || 'warning'
}

function formatTime(t) {
  return t ? String(t).replace('T', ' ').slice(0, 19) : '-'
}

onMounted(async () => {
  const res = await api.admin.stats()
  if (res.code === 200) {
    stats.value = res.data
  }
})
</script>

<style scoped>
.stat-card {
  text-align: center;
}

.stat-value {
  font-size: 30px;
  font-weight: bold;
}

.stat-value.small {
  font-size: 24px;
}

.stat-label {
  margin-top: 6px;
  color: #888;
  font-size: 13px;
}

.second-row {
  margin-top: 16px;
}

.table-card {
  margin-top: 16px;
}
</style>
