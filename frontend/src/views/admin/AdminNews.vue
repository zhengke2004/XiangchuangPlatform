<template>
  <div class="page">
    <el-row :gutter="16">
      <el-col :span="8">
        <el-card shadow="never" class="publish-card">
          <template #header><span>发布资讯</span></template>
          <el-form :model="form" label-position="top">
            <el-form-item label="标题" required>
              <el-input v-model="form.title" placeholder="资讯标题" maxlength="50" show-word-limit />
            </el-form-item>
            <el-form-item label="内容" required>
              <el-input v-model="form.content" type="textarea" :rows="5" placeholder="资讯内容,发布后将推送给目标用户" />
            </el-form-item>
            <el-form-item label="目标人群">
              <el-select v-model="form.targetType" style="width: 100%">
                <el-option v-for="t in targets" :key="t" :label="t" :value="t" />
              </el-select>
            </el-form-item>
            <el-button type="primary" style="width: 100%" :loading="publishing" @click="publish">发布资讯</el-button>
          </el-form>
        </el-card>
      </el-col>

      <el-col :span="16">
        <el-card shadow="never">
          <template #header><span>已发布资讯</span></template>
          <el-table :data="list" stripe v-loading="loading">
            <el-table-column prop="title" label="标题" min-width="150" show-overflow-tooltip />
            <el-table-column prop="content" label="内容" min-width="220" show-overflow-tooltip />
            <el-table-column prop="recipients" label="接收人数" width="90">
              <template #default="{ row }">{{ row.recipients }}</template>
            </el-table-column>
            <el-table-column label="发布时间" width="160">
              <template #default="{ row }">{{ formatTime(row.publishedAt) }}</template>
            </el-table-column>
            <el-table-column label="操作" width="90" fixed="right">
              <template #default="{ row }">
                <el-button size="small" type="danger" link @click="remove(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="!loading && !list.length" description="暂无资讯" :image-size="70" />
          <el-pagination
            class="pager"
            layout="total, prev, pager, next"
            :total="total"
            :page-size="query.pageSize"
            :current-page="query.page"
            @current-change="p => { query.page = p; load() }"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

const targets = ['全部', '普通用户', '农户', '商家', '管理员']

const form = reactive({ title: '', content: '', targetType: '全部' })
const publishing = ref(false)

const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ page: 1, pageSize: 10 })

async function load() {
  loading.value = true
  try {
    const res = await api.admin.news.list({ ...query })
    if (res.code === 200) {
      list.value = res.data.list
      total.value = Number(res.data.total)
    }
  } finally {
    loading.value = false
  }
}

async function publish() {
  if (!form.title || !form.content) {
    ElMessage.warning('请填写标题和内容')
    return
  }
  publishing.value = true
  try {
    const res = await api.admin.news.publish({ ...form })
    if (res.code === 200) {
      ElMessage.success(`发布成功,已通知${res.data}位用户`)
      form.title = ''
      form.content = ''
      load()
    } else {
      ElMessage.error(res.message || '发布失败')
    }
  } finally {
    publishing.value = false
  }
}

function remove(row) {
  ElMessageBox.confirm(`确认删除资讯「${row.title}」?该资讯将从所有用户的消息中移除。`, '删除资讯', { type: 'warning' })
    .then(async () => {
      const res = await api.admin.news.remove(row.title, row.content)
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
.publish-card {
  height: 100%;
}

.pager {
  margin-top: 16px;
  justify-content: flex-end;
}
</style>
