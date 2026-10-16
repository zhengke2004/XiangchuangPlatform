<template>
  <div class="page">
    <el-card shadow="never">
      <div class="toolbar">
        <el-input
          v-model="query.keyword"
          placeholder="搜索活动名称"
          clearable
          style="width: 200px"
          @keyup.enter="load"
          @clear="load"
        />
        <el-select v-model="query.type" placeholder="全部类型" clearable style="width: 150px" @change="load">
          <el-option v-for="t in types" :key="t" :label="t" :value="t" />
        </el-select>
        <el-select v-model="query.isActive" placeholder="全部状态" clearable style="width: 130px" @change="load">
          <el-option label="已上架" :value="true" />
          <el-option label="已下架" :value="false" />
        </el-select>
        <el-button type="primary" @click="load">查询</el-button>
        <div class="spacer" />
        <el-button type="success" @click="openCreate">发布活动</el-button>
      </div>

      <el-table :data="list" stripe v-loading="loading">
        <el-table-column prop="activityId" label="ID" width="60" />
        <el-table-column prop="activityName" label="活动名称" min-width="160" show-overflow-tooltip />
        <el-table-column prop="activityType" label="类型" width="100" />
        <el-table-column prop="activityTheme" label="主题" width="120" show-overflow-tooltip />
        <el-table-column prop="activityLocation" label="地点" width="110" show-overflow-tooltip />
        <el-table-column prop="activityPrice" label="价格(元)" width="95">
          <template #default="{ row }">¥{{ row.activityPrice }}</template>
        </el-table-column>
        <el-table-column label="预订/容量" width="100">
          <template #default="{ row }">{{ row.currentBooked }}/{{ row.maxParticipants }}</template>
        </el-table-column>
        <el-table-column prop="activityRating" label="评分" width="70" />
        <el-table-column prop="isActive" label="上架" width="80">
          <template #default="{ row }">
            <el-switch :model-value="row.isActive" @change="val => changeStatus(row, val)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" link @click="openEdit(row)">编辑</el-button>
            <el-button size="small" type="warning" link @click="openDates(row)">可用日期</el-button>
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

    <!-- 活动 新增/编辑 弹窗 -->
    <el-dialog v-model="dialogVisible" :title="editingId ? '编辑活动' : '发布活动'" width="640px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="活动名称" required>
          <el-input v-model="form.activityName" placeholder="请输入活动名称" />
        </el-form-item>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="类型">
              <el-select v-model="form.activityType" placeholder="选择类型" style="width: 100%">
                <el-option v-for="t in types" :key="t" :label="t" :value="t" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主题">
              <el-input v-model="form.activityTheme" placeholder="如:亲子研学" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="活动地点">
              <el-input v-model="form.activityLocation" placeholder="如:杭州·余杭" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="活动时长">
              <el-input v-model="form.activityDuration" placeholder="如:半天/全天" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="详细地址">
          <el-input v-model="form.activityAddress" placeholder="详细地址" />
        </el-form-item>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="价格(元)" required>
              <el-input-number v-model="form.activityPrice" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大人数">
              <el-input-number v-model="form.maxParticipants" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="活动图片">
          <el-input v-model="form.activityImages" placeholder="图片URL,多张用逗号分隔" />
        </el-form-item>
        <el-form-item label="活动介绍">
          <el-input v-model="form.activityDescription" type="textarea" :rows="3" placeholder="活动介绍" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="save">保存</el-button>
      </template>
    </el-dialog>

    <!-- 可用日期配置弹窗 -->
    <el-dialog v-model="datesVisible" :title="`可用日期配置 - ${currentActivity?.activityName || ''}`" width="620px">
      <div class="date-add">
        <el-date-picker v-model="dateForm.availableDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 150px" />
        <el-input-number v-model="dateForm.availableSpots" :min="1" placeholder="名额" controls-position="right" style="width: 120px" />
        <el-input-number v-model="dateForm.priceAdjustment" :precision="2" placeholder="价格调整" controls-position="right" style="width: 130px" />
        <el-button type="primary" @click="addDate">添加</el-button>
      </div>
      <el-table :data="dateList" stripe max-height="320">
        <el-table-column prop="availableDate" label="日期" width="130" />
        <el-table-column prop="availableSpots" label="名额" width="90" />
        <el-table-column prop="priceAdjustment" label="价格调整(元)" width="130" />
        <el-table-column prop="isAvailable" label="可预订" width="90">
          <template #default="{ row }">
            <el-tag :type="row.isAvailable ? 'success' : 'info'" size="small">{{ row.isAvailable ? '是' : '否' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="90">
          <template #default="{ row }">
            <el-button size="small" type="danger" link @click="removeDate(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

const types = ['农耕体验', '果园采摘', '乡村美食', '田园骑行', '手工艺体验']

const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ page: 1, pageSize: 10, keyword: '', type: '', isActive: null })

const dialogVisible = ref(false)
const editingId = ref(null)
const saving = ref(false)
const emptyForm = {
  activityName: '', activityType: '', activityTheme: '', activityLocation: '',
  activityAddress: '', activityDuration: '', activityPrice: 0, maxParticipants: 20,
  activityDescription: '', activityImages: ''
}
const form = reactive({ ...emptyForm })

const datesVisible = ref(false)
const currentActivity = ref(null)
const dateList = ref([])
const dateForm = reactive({ availableDate: '', availableSpots: 20, priceAdjustment: 0 })

async function load() {
  loading.value = true
  try {
    const res = await api.admin.activities.list({ ...query })
    if (res.code === 200) {
      list.value = res.data.list
      total.value = Number(res.data.total)
    }
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editingId.value = null
  Object.assign(form, emptyForm)
  dialogVisible.value = true
}

function openEdit(row) {
  editingId.value = row.activityId
  Object.keys(emptyForm).forEach(k => { form[k] = row[k] ?? emptyForm[k] })
  dialogVisible.value = true
}

async function save() {
  if (!form.activityName || form.activityPrice == null) {
    ElMessage.warning('请填写活动名称和价格')
    return
  }
  saving.value = true
  try {
    const res = editingId.value
      ? await api.admin.activities.update(editingId.value, { ...form })
      : await api.admin.activities.create({ ...form })
    if (res.code === 200) {
      ElMessage.success('保存成功')
      dialogVisible.value = false
      load()
    } else {
      ElMessage.error(res.message || '保存失败')
    }
  } finally {
    saving.value = false
  }
}

async function changeStatus(row, val) {
  await api.admin.activities.updateStatus(row.activityId, { isActive: val })
  ElMessage.success(val ? '已上架' : '已下架')
  row.isActive = val
}

function remove(row) {
  ElMessageBox.confirm(`确认删除活动「${row.activityName}」及其可用日期配置?`, '删除活动', { type: 'warning' })
    .then(async () => {
      const res = await api.admin.activities.remove(row.activityId)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    })
    .catch(() => {})
}

async function openDates(row) {
  currentActivity.value = row
  datesVisible.value = true
  const res = await api.admin.activities.availability(row.activityId)
  if (res.code === 200) {
    dateList.value = res.data
  }
}

async function addDate() {
  if (!dateForm.availableDate) {
    ElMessage.warning('请选择日期')
    return
  }
  const res = await api.admin.activities.addAvailability(currentActivity.value.activityId, { ...dateForm })
  if (res.code === 200) {
    ElMessage.success('添加成功')
    dateForm.availableDate = ''
    dateForm.availableSpots = 20
    dateForm.priceAdjustment = 0
    const list = await api.admin.activities.availability(currentActivity.value.activityId)
    dateList.value = list.data
  } else {
    ElMessage.error(res.message || '添加失败')
  }
}

async function removeDate(row) {
  const res = await api.admin.activities.deleteAvailability(currentActivity.value.activityId, row.availabilityId)
  if (res.code === 200) {
    ElMessage.success('删除成功')
    dateList.value = dateList.value.filter(d => d.availabilityId !== row.availabilityId)
  }
}

onMounted(load)
</script>

<style scoped>
.toolbar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  align-items: center;
}

.spacer {
  flex: 1;
}

.pager {
  margin-top: 16px;
  justify-content: flex-end;
}

.date-add {
  display: flex;
  gap: 10px;
  margin-bottom: 14px;
}
</style>
