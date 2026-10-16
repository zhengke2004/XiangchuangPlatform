<template>
  <div class="account">
    <div class="container">
      <div class="account-content">
        <aside class="sidebar">
          <div class="profile-card">
            <img v-if="user?.avatar" :src="user.avatar" alt="头像" class="avatar" />
            <div v-else class="avatar-placeholder">{{ user?.username?.charAt(0) }}</div>
            <h3>{{ user?.username }}</h3>
            <p>{{ user?.phone }}</p>
          </div>
          <nav class="nav">
            <router-link to="/account" class="nav-item" :class="{ active: activeTab === 'profile' }" @click="activeTab = 'profile'">个人资料</router-link>
            <router-link to="/orders" class="nav-item" :class="{ active: activeTab === 'orders' }" @click="activeTab = 'orders'">我的订单</router-link>
            <router-link to="/favorites" class="nav-item" :class="{ active: activeTab === 'favorites' }" @click="activeTab = 'favorites'">我的收藏</router-link>
            <router-link to="/cart" class="nav-item" :class="{ active: activeTab === 'cart' }" @click="activeTab = 'cart'">购物车</router-link>
          </nav>
        </aside>

        <main class="main-content">
          <div v-if="activeTab === 'profile'" class="profile-section">
            <h2>个人资料</h2>
            <div class="form-card">
              <div class="form-row">
                <label>用户名</label>
                <input v-model="profile.username" type="text" />
              </div>
              <div class="form-row">
                <label>手机号</label>
                <input v-model="profile.phone" type="tel" readonly />
              </div>
              <div class="form-row">
                <label>邮箱</label>
                <input v-model="profile.email" type="email" />
              </div>
              <div class="form-row">
                <label>头像</label>
                <input type="file" accept="image/*" @change="handleAvatarUpload" />
              </div>
              <div class="form-row">
                <label>地址</label>
                <input v-model="profile.address" type="text" />
              </div>
              <button class="save-btn" @click="saveProfile">保存修改</button>
            </div>
          </div>
        </main>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const user = ref(null)
const activeTab = ref('profile')

const profile = reactive({
  username: '',
  phone: '',
  email: '',
  address: ''
})

onMounted(() => {
  user.value = authStore.user
  if (user.value) {
    profile.username = user.value.username || ''
    profile.phone = user.value.phone || ''
    profile.email = user.value.email || ''
    profile.address = user.value.address || ''
  }
})

function handleAvatarUpload(event) {
  console.log('头像上传:', event.target.files[0])
}

function saveProfile() {
  ElMessage.success('保存成功')
}
</script>

<style scoped>
.account {
  padding: 30px 0;
}

.account-content {
  display: flex;
  gap: 30px;
}

.sidebar {
  width: 250px;
  flex-shrink: 0;
}

.profile-card {
  background: white;
  padding: 30px;
  border-radius: 8px;
  text-align: center;
  margin-bottom: 20px;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 15px;
}

.avatar-placeholder {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: #4a8c3a;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  margin-bottom: 15px;
}

.profile-card h3 {
  font-size: 18px;
  margin-bottom: 5px;
}

.profile-card p {
  font-size: 14px;
  color: #666;
}

.nav {
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

.nav-item {
  display: block;
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
  font-size: 14px;
  transition: background-color 0.3s;
}

.nav-item:last-child {
  border-bottom: none;
}

.nav-item:hover,
.nav-item.active {
  background: #f0f7ed;
  color: #4a8c3a;
}

.main-content {
  flex: 1;
}

.profile-section h2 {
  font-size: 24px;
  margin-bottom: 20px;
}

.form-card {
  background: white;
  padding: 30px;
  border-radius: 8px;
}

.form-row {
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
}

.form-row label {
  font-size: 14px;
  margin-bottom: 8px;
}

.form-row input {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.save-btn {
  background: #4a8c3a;
  color: white;
  border: none;
  padding: 12px 30px;
  border-radius: 4px;
  font-size: 16px;
}
</style>