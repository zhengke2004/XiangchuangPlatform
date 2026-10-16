<template>
  <header class="header">
    <div class="header-content">
      <router-link to="/" class="logo">
        <img src="/images/logo.jpg" class="logo-icon" alt="Logo" />
        <span class="logo-text">乡创农游智能体平台</span>
      </router-link>

      <nav class="nav">
        <router-link to="/" class="nav-item" :class="{ active: $route.name === 'Home' }">首页</router-link>
        <router-link to="/market" class="nav-item" :class="{ active: $route.name === 'Market' }">农品市集</router-link>
        <router-link to="/travel" class="nav-item" :class="{ active: $route.name === 'Travel' }">旅游定制</router-link>
        <router-link to="/guide" class="nav-item" :class="{ active: $route.name === 'Guide' }">资讯指南</router-link>
      </nav>

      <div class="user-area">
        <div v-if="authStore.isLogin" class="user-info">
          <router-link to="/cart" class="cart-btn">
            <span class="cart-icon">🛒</span>
          </router-link>
          <router-link to="/account" class="avatar-wrapper">
            <img v-if="authStore.user?.avatar" :src="authStore.user.avatar" alt="头像" class="avatar" />
            <div v-else class="avatar-placeholder">{{ authStore.user?.username?.charAt(0) }}</div>
          </router-link>
          <span class="username">{{ authStore.user?.username }}</span>
          <router-link v-if="authStore.user?.userType === '管理员'" to="/admin" class="admin-link">管理后台</router-link>
          <button class="logout-btn" @click="handleLogout">退出</button>
        </div>
        <div v-else class="auth-links">
          <router-link to="/login" class="login-link">登录</router-link>
          <router-link to="/register" class="register-link">注册</router-link>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

onMounted(() => {
  authStore.loadUserFromStorage()
})

async function handleLogout() {
  await authStore.logout()
  ElMessage.success('退出成功')
  router.push('/')
}
</script>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: rgb(56, 110, 46);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  font-weight: bold;
  color: white;
}

.logo-icon {
  width: 32px;
  height: 32px;
  object-fit: contain;
}

.nav {
  display: flex;
  gap: 30px;
}

.nav-item {
  font-size: 15px;
  color: rgba(255, 255, 255, 0.85);
  transition: color 0.3s;
}

.nav-item:hover,
.nav-item.active {
  color: white;
}

.user-area {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.cart-btn {
  font-size: 20px;
}

.avatar-wrapper {
  display: flex;
  align-items: center;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid white;
}

.avatar-placeholder {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: white;
  color: rgb(56, 110, 46);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: bold;
}

.username {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.85);
}

.admin-link {
  font-size: 13px;
  color: #fff;
  background: rgba(255, 255, 255, 0.18);
  padding: 4px 10px;
  border-radius: 4px;
  transition: background 0.3s;
}

.admin-link:hover {
  background: rgba(255, 255, 255, 0.3);
}

.logout-btn {
  background: none;
  border: none;
  color: rgba(255, 255, 255, 0.7);
  font-size: 13px;
}

.logout-btn:hover {
  color: white;
}

.auth-links {
  display: flex;
  gap: 10px;
}

.login-link {
  font-size: 14px;
  color: white;
  padding: 6px 16px;
  border: 1px solid rgba(255, 255, 255, 0.6);
  border-radius: 4px;
  transition: all 0.3s;
}

.login-link:hover {
  background: rgba(255, 255, 255, 0.15);
}

.register-link {
  background: white;
  color: rgb(56, 110, 46);
  padding: 6px 16px;
  border-radius: 4px;
  font-size: 14px;
  transition: all 0.3s;
}

.register-link:hover {
  background: rgba(255, 255, 255, 0.9);
}
</style>