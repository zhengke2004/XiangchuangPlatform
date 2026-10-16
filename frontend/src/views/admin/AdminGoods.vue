<template>
  <div class="page">
    <el-card shadow="never">
      <div class="toolbar">
        <el-input
          v-model="query.keyword"
          placeholder="搜索商品名称"
          clearable
          style="width: 200px"
          @keyup.enter="load"
          @clear="load"
        />
        <el-select v-model="query.category" placeholder="全部分类" clearable style="width: 150px" @change="load">
          <el-option v-for="c in categories" :key="c" :label="c" :value="c" />
        </el-select>
        <el-select v-model="query.isActive" placeholder="全部状态" clearable style="width: 130px" @change="load">
          <el-option label="已上架" :value="true" />
          <el-option label="已下架" :value="false" />
        </el-select>
        <el-button type="primary" @click="load">查询</el-button>
        <div class="spacer" />
        <el-button type="success" @click="openCreate">新增商品</el-button>
      </div>

      <el-table :data="list" stripe v-loading="loading">
        <el-table-column prop="goodsId" label="ID" width="60" />
        <el-table-column label="图片" width="80">
          <template #default="{ row }">
            <el-image :src="row.goodsPicture" fit="cover" style="width: 48px; height: 48px; border-radius: 4px">
              <template #error>
                <div class="img-fallback">无图</div>
              </template>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="goodsName" label="商品名称" min-width="160" show-overflow-tooltip />
        <el-table-column prop="goodsCategory" label="分类" width="110" />
        <el-table-column prop="goodsArea" label="产地" width="100" />
        <el-table-column prop="goodsPrice" label="价格(元)" width="100">
          <template #default="{ row }">¥{{ row.goodsPrice }}</template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="80" />
        <el-table-column prop="salesCount" label="销量" width="80" />
        <el-table-column prop="isActive" label="上架" width="80">
          <template #default="{ row }">
            <el-switch :model-value="row.isActive" @change="val => changeStatus(row, val)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" link @click="openEdit(row)">编辑</el-button>
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

    <el-dialog v-model="dialogVisible" :title="editingId ? '编辑商品' : '新增商品'" width="640px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="商品名称" required>
          <el-input v-model="form.goodsName" placeholder="请输入商品名称" />
        </el-form-item>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="分类">
              <el-select v-model="form.goodsCategory" placeholder="选择分类" style="width: 100%">
                <el-option v-for="c in categories" :key="c" :label="c" :value="c" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="季节">
              <el-select v-model="form.goodsTime" placeholder="选择季节" style="width: 100%">
                <el-option v-for="s in seasons" :key="s" :label="s" :value="s" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="产地">
              <el-input v-model="form.goodsArea" placeholder="如:浙江·杭州" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生产者">
              <el-input v-model="form.goodsProducer" placeholder="生产者/合作社" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="价格(元)" required>
              <el-input-number v-model="form.goodsPrice" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="库存">
              <el-input-number v-model="form.stock" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="规格标准">
          <el-input v-model="form.goodsStandard" placeholder="如:500g/袋" />
        </el-form-item>
        <el-form-item label="产地地址">
          <el-input v-model="form.goodsPAddress" placeholder="详细产地地址" />
        </el-form-item>
        <el-form-item label="商品图片">
          <el-input v-model="form.goodsPicture" placeholder="图片URL,如 /images/products/xxx.jpg" />
        </el-form-item>
        <el-form-item label="商品介绍">
          <el-input v-model="form.goodsIntroduce" type="textarea" :rows="3" placeholder="商品介绍" />
        </el-form-item>
        <el-form-item label="是否上架">
          <el-switch v-model="form.isActive" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

const categories = ['有机农产品', '时令生鲜', '手工艺品', '加工食品', '乡村特产']
const seasons = ['春', '夏', '秋', '冬', '全年']

const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ page: 1, pageSize: 10, keyword: '', category: '', isActive: null })

const dialogVisible = ref(false)
const editingId = ref(null)
const saving = ref(false)
const emptyForm = {
  goodsName: '', goodsCategory: '', goodsTime: '', goodsArea: '',
  goodsProducer: '', goodsPAddress: '', goodsIntroduce: '',
  goodsPrice: 0, stock: 100, goodsStandard: '', goodsPicture: '', isActive: true
}
const form = reactive({ ...emptyForm })

async function load() {
  loading.value = true
  try {
    const res = await api.admin.goods.list({ ...query })
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
  editingId.value = row.goodsId
  Object.keys(emptyForm).forEach(k => { form[k] = row[k] ?? emptyForm[k] })
  dialogVisible.value = true
}

async function save() {
  if (!form.goodsName || form.goodsPrice == null) {
    ElMessage.warning('请填写商品名称和价格')
    return
  }
  saving.value = true
  try {
    const res = editingId.value
      ? await api.admin.goods.update(editingId.value, { ...form })
      : await api.admin.goods.create({ ...form })
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
  await api.admin.goods.updateStatus(row.goodsId, { isActive: val })
  ElMessage.success(val ? '已上架' : '已下架')
  row.isActive = val
}

function remove(row) {
  ElMessageBox.confirm(`确认删除商品「${row.goodsName}」?`, '删除商品', { type: 'warning' })
    .then(async () => {
      const res = await api.admin.goods.remove(row.goodsId)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    })
    .catch(() => {})
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

.img-fallback {
  width: 48px;
  height: 48px;
  border-radius: 4px;
  background: #f0f0f0;
  color: #aaa;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
}
</style>
