<template>
  <div class="page">
    <el-card shadow="never">
      <div class="toolbar">
        <el-input
          v-model="query.keyword"
          placeholder="订单号/联系人/电话"
          clearable
          style="width: 200px"
          @keyup.enter="load"
          @clear="load"
        />
        <el-select v-model="query.orderType" placeholder="全部类型" clearable style="width: 120px" @change="load">
          <el-option label="商品" value="商品" />
          <el-option label="旅游" value="旅游" />
        </el-select>
        <el-select v-model="query.orderStatus" placeholder="订单状态" clearable style="width: 130px" @change="load">
          <el-option v-for="s in orderStatuses" :key="s" :label="s" :value="s" />
        </el-select>
        <el-select v-model="query.paymentStatus" placeholder="支付状态" clearable style="width: 130px" @change="load">
          <el-option v-for="s in payStatuses" :key="s" :label="s" :value="s" />
        </el-select>
        <el-button type="primary" @click="load">查询</el-button>
      </div>

      <el-table :data="list" stripe v-loading="loading">
        <el-table-column prop="orderNo" label="订单号" min-width="190" show-overflow-tooltip />
        <el-table-column prop="orderType" label="类型" width="80">
          <template #default="{ row }">
            <el-tag :type="row.orderType === '旅游' ? 'warning' : 'success'" size="small">{{ row.orderType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="金额(元)" width="100">
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
        <el-table-column prop="contactName" label="联系人" width="100" />
        <el-table-column prop="contactPhone" label="联系电话" width="120" />
        <el-table-column prop="travelDate" label="出行日期" width="110">
          <template #default="{ row }">{{ row.travelDate || '-' }}</template>
        </el-table-column>
        <el-table-column prop="createdAt" label="下单时间" width="160">
          <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" link @click="openDetail(row)">详情</el-button>
            <el-button size="small" type="warning" link @click="openEdit(row)">改状态</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        class="pager"
        layout="total, prev, pager, next"
        :total="total"
        :page-size="query.pageSize"
        :current-page="query.page"
        @current-change="p => { query.page = p; load() }"
      />
    </el-card>

    <!-- 订单详情 -->
    <el-dialog v-model="detailVisible" title="订单详情" width="640px">
      <el-descriptions :column="2" border v-if="detail.order">
        <el-descriptions-item label="订单号" :span="2">{{ detail.order.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="类型">{{ detail.order.orderType }}</el-descriptions-item>
        <el-descriptions-item label="金额">¥{{ detail.order.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="支付方式">{{ detail.order.paymentMethod || '-' }}</el-descriptions-item>
        <el-descriptions-item label="支付状态">{{ detail.order.paymentStatus }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">{{ detail.order.orderStatus }}</el-descriptions-item>
        <el-descriptions-item label="参与人数">{{ detail.order.participants || '-' }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ detail.order.contactName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detail.order.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="收货/联系地址" :span="2">{{ detail.order.contactAddress || '-' }}</el-descriptions-item>
        <el-descriptions-item label="出行日期">{{ detail.order.travelDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="下单时间">{{ formatTime(detail.order.createdAt) }}</el-descriptions-item>
        <el-descriptions-item label="特殊需求" :span="2">{{ detail.order.specialRequests || '-' }}</el-descriptions-item>
      </el-descriptions>
      <h4 style="margin: 14px 0 8px">订单明细</h4>
      <el-table :data="detail.items || []" stripe size="small">
        <el-table-column prop="item_name" label="条目" min-width="140" />
        <el-table-column prop="item_type" label="类型" width="80" />
        <el-table-column prop="quantity" label="数量" width="70" />
        <el-table-column prop="price" label="单价" width="90" />
        <el-table-column prop="subtotal" label="小计" width="90" />
      </el-table>
    </el-dialog>

    <!-- 修改状态 -->
    <el-dialog v-model="editVisible" title="修改订单状态" width="420px">
      <el-form label-width="90px">
        <el-form-item label="订单状态">
          <el-select v-model="editForm.orderStatus" style="width: 100%">
            <el-option v-for="s in orderStatuses" :key="s" :label="s" :value="s" />
          </el-select>
        </el-form-item>
        <el-form-item label="支付状态">
          <el-select v-model="editForm.paymentStatus" style="width: 100%">
            <el-option v-for="s in payStatuses" :key="s" :label="s" :value="s" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="saveStatus">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/api'

const orderStatuses = ['待处理', '进行中', '已完成', '已取消']
const payStatuses = ['待付款', '已付款', '已退款', '支付失败']

const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ page: 1, pageSize: 10, keyword: '', orderType: '', orderStatus: '', paymentStatus: '' })

const detailVisible = ref(false)
const detail = ref({ order: null, items: [] })

const editVisible = ref(false)
const editId = ref(null)
const editForm = reactive({ orderStatus: '', paymentStatus: '' })

async function load() {
  loading.value = true
  try {
    const res = await api.admin.orders.list({ ...query })
    if (res.code === 200) {
      list.value = res.data.list
      total.value = Number(res.data.total)
    }
  } finally {
    loading.value = false
  }
}

async function openDetail(row) {
  const res = await api.admin.orders.detail(row.orderId)
  if (res.code === 200) {
    detail.value = res.data
    detailVisible.value = true
  }
}

function openEdit(row) {
  editId.value = row.orderId
  editForm.orderStatus = row.orderStatus
  editForm.paymentStatus = row.paymentStatus
  editVisible.value = true
}

async function saveStatus() {
  const res = await api.admin.orders.updateStatus(editId.value, { ...editForm })
  if (res.code === 200) {
    ElMessage.success('状态已更新')
    editVisible.value = false
    load()
  } else {
    ElMessage.error(res.message || '更新失败')
  }
}

function payTagType(status) {
  return { 已付款: 'success', 已退款: 'info', 支付失败: 'danger' }[status] || 'warning'
}

function orderTagType(status) {
  return { 已完成: 'success', 进行中: 'primary', 已取消: 'info' }[status] || 'warning'
}

function formatTime(t) {
  return t ? String(t).replace('T', ' ').slice(0, 19) : '-'
}

onMounted(load)
</script>

<style scoped>
.toolbar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.pager {
  margin-top: 16px;
  justify-content: flex-end;
}
</style>
