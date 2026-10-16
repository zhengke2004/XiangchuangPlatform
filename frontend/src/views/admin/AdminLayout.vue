<template>
  <div class="admin-layout">
    <aside class="admin-aside">
      <div class="admin-logo">
        <span class="logo-title">农游农家乐平台</span>
        <span class="logo-sub">管理后台</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        class="admin-menu"
        background-color="#2c4d24"
        text-color="rgba(255,255,255,0.75)"
        active-text-color="#ffffff"
      >
        <el-menu-item index="/admin">
          <span>数据总览</span>
        </el-menu-item>
        <el-menu-item index="/admin/users">
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/goods">
          <span>商品管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/activities">
          <span>活动管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/orders">
          <span>订单管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/reviews">
          <span>评价管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/news">
          <span>资讯管理</span>
        </el-menu-item>
      </el-menu>
    </aside>

    <div class="admin-main">
      <header class="admin-header">
        <h2 class="page-title">{{ pageTitle }}</h2>
        <div class="header-right">
          <span class="admin-name">{{ authStore.user?.username }}(管理员)</span>
          <el-button size="small" @click="goFront">返回前台</el-button>
          <el-button size="small" type="danger" plain @click="handleLogout">退出登录</el-button>
        </div>
      </header>
      <main class="admin-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const route = useRoute()
const router = useRouter()

const activeMenu = computed(() => route.path)

const titles = {
  '/admin': '数据总览',
  '/admin/users': '用户管理',
  '/admin/goods': '商品管理',
  '/admin/activities': '活动管理',
  '/admin/orders': '订单管理',
  '/admin/reviews': '评价管理',
  '/admin/news': '资讯管理'
}

const pageTitle = computed(() => titles[route.path] || '管理后台')

onMounted(() => {
  authStore.loadUserFromStorage()
})

function goFront() {
  router.push('/')
}

async function handleLogout() {
  await authStore.logout()
  ElMessage.success('退出成功')
  router.push('/login')
}
</script>

<style scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
  background: #f5f7f6;
}

.admin-aside {
  width: 220px;
  background: #2c4d24;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
}

.admin-logo {
  padding: 22px 20px;
  display: flex;
  flex-direction: column;
  gap: 4px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.12);
}

.logo-title {
  color: #fff;
  font-size: 17px;
  font-weight: bold;
}

.logo-sub {
  color: rgba(255, 255, 255, 0.6);
  font-size: 13px;
}

.admin-menu {
  border-right: none;
}

.admin-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.admin-header {
  height: 60px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.page-title {
  font-size: 17px;
  color: #333;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.admin-name {
  font-size: 14px;
  color: #666;
}

.admin-content {
  flex: 1;
  padding: 24px;
  overflow: auto;
}
</style>
