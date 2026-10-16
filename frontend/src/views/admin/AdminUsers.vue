<template>
  <div class="page">
    <el-card shadow="never">
      <div class="toolbar">
        <el-input
          v-model="query.keyword"
          placeholder="搜索用户名/手机号"
          clearable
          style="width: 220px"
          @keyup.enter="load"
          @clear="load"
        />
        <el-select v-model="query.userType" placeholder="全部角色" clearable style="width: 140px" @change="load">
          <el-option v-for="t in userTypes" :key="t" :label="t" :value="t" />
        </el-select>
        <el-button type="primary" @click="load">查询</el-button>
      </div>

      <el-table :data="list" stripe v-loading="loading">
        <el-table-column prop="userId" label="ID" width="60" />
        <el-table-column label="头像" width="70">
          <template #default="{ row }">
            <el-avatar :size="36" :src="row.avatar">{{ row.username?.charAt(0) }}</el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="email" label="邮箱" min-width="160" show-overflow-tooltip />
        <el-table-column prop="userType" label="角色" width="110">
          <template #default="{ row }">
            <el-select
              :model-value="row.userType"
              size="small"
              style="width: 96px"
              @change="val => changeType(row, val)"
            >
              <el-option v-for="t in userTypes" :key="t" :label="t" :value="t" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="isActive" label="状态" width="90">
          <template #default="{ row }">
            <el-switch :model-value="row.isActive" @change="val => changeStatus(row, val)" />
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="注册时间" width="170">
          <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
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

const userTypes = ['普通用户', '农户', '商家', '管理员']
const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ page: 1, pageSize: 10, keyword: '', userType: '' })

async function load() {
  loading.value = true
  try {
    const res = await api.admin.users.list({ ...query })
    if (res.code === 200) {
      list.value = res.data.list
      total.value = Number(res.data.total)
    }
  } finally {
    loading.value = false
  }
}

async function changeStatus(row, val) {
  await api.admin.users.updateStatus(row.userId, { isActive: val })
  ElMessage.success(val ? '已启用' : '已停用')
  row.isActive = val
}

function changeType(row, val) {
  ElMessageBox.confirm(`确认将用户「${row.username}」的角色调整为「${val}」?`, '调整角色', { type: 'warning' })
    .then(async () => {
      await api.admin.users.updateType(row.userId, { userType: val })
      ElMessage.success('角色已调整')
      row.userType = val
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
