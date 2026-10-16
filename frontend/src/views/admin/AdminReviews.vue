<template>
  <div class="page">
    <el-card shadow="never">
      <div class="toolbar">
        <el-select v-model="query.itemType" placeholder="全部类型" clearable style="width: 130px" @change="load">
          <el-option label="商品" value="商品" />
          <el-option label="活动" value="活动" />
        </el-select>
        <el-select v-model="query.rating" placeholder="全部评分" clearable style="width: 130px" @change="load">
          <el-option v-for="n in 5" :key="n" :label="`${n}星`" :value="n" />
        </el-select>
        <el-button type="primary" @click="load">查询</el-button>
      </div>

      <el-table :data="list" stripe v-loading="loading">
        <el-table-column prop="reviewId" label="ID" width="60" />
        <el-table-column prop="userId" label="用户ID" width="80" />
        <el-table-column prop="itemType" label="类型" width="80">
          <template #default="{ row }">
            <el-tag :type="row.itemType === '活动' ? 'warning' : 'success'" size="small">{{ row.itemType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="itemId" label="条目ID" width="80" />
        <el-table-column label="评分" width="150">
          <template #default="{ row }">
            <el-rate :model-value="row.rating" disabled />
          </template>
        </el-table-column>
        <el-table-column prop="content" label="评价内容" min-width="220" show-overflow-tooltip />
        <el-table-column prop="createdAt" label="评价时间" width="160">
          <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="90" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="danger" link @click="remove(row)">删除</el-button>
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
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ page: 1, pageSize: 10, itemType: '', rating: null })

async function load() {
  loading.value = true
  try {
    const res = await api.admin.reviews.list({ ...query })
    if (res.code === 200) {
      list.value = res.data.list
      total.value = Number(res.data.total)
    }
  } finally {
    loading.value = false
  }
}

function remove(row) {
  ElMessageBox.confirm(`确认删除该条评价(ID:${row.reviewId})?`, '删除评价', { type: 'warning' })
    .then(async () => {
      const res = await api.admin.reviews.remove(row.reviewId)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        load()
      }
    })
    .catch(() => {})
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
