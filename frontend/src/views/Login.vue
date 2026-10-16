<template>
  <div class="login">
    <div class="container">
      <div class="login-card">
        <div class="login-header">
          <h1>登录</h1>
          <p>欢迎来到乡创农游智能体平台</p>
        </div>
        <form @submit.prevent="handleLogin" class="login-form">
          <div class="form-group">
            <label>手机号</label>
            <input v-model="phone" type="tel" placeholder="请输入手机号" />
          </div>
          <div class="form-group">
            <label>密码</label>
            <input v-model="password" type="password" placeholder="请输入密码" />
          </div>
          <div class="form-group">
            <label>验证码</label>
            <div class="captcha-row">
              <input v-model="captcha" type="text" placeholder="请输入验证码" />
              <button type="button" class="captcha-btn" @click="getCaptcha">{{ captchaText }}</button>
            </div>
          </div>
          <div class="form-group">
            <label class="checkbox-label">
              <input type="checkbox" v-model="rememberMe" />
              记住我
            </label>
          </div>
          <button type="submit" class="login-btn" :disabled="loading">登录</button>
        </form>
        <div class="login-footer">
          <span>还没有账号？</span>
          <router-link to="/register">立即注册</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const router = useRouter()

const phone = ref('')
const password = ref('')
const captcha = ref('')
const rememberMe = ref(false)
const loading = ref(false)
const captchaText = ref('获取验证码')

async function getCaptcha() {
  if (!phone.value) {
    ElMessage.warning('请先输入手机号')
    return
  }
  captchaText.value = '发送中...'
  setTimeout(() => {
    captchaText.value = '获取验证码'
    ElMessage.success('验证码已发送')
  }, 1000)
}

async function handleLogin() {
  if (!phone.value || !password.value || !captcha.value) {
    ElMessage.warning('请填写完整信息')
    return
  }

  loading.value = true
  try {
    await authStore.login(phone.value, password.value)
    ElMessage.success('登录成功')
    router.push('/')
  } catch (e) {
    ElMessage.error('登录失败，请检查账号密码')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login {
  min-height: calc(100vh - 60px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 30px 0;
}

.login-card {
  background: white;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.1);
  width: 400px;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h1 {
  font-size: 28px;
  margin-bottom: 10px;
}

.login-header p {
  color: #666;
}

.login-form {
  display: flex;
  flex-direction: column;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-size: 14px;
  margin-bottom: 8px;
}

.form-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.captcha-row {
  display: flex;
  gap: 10px;
}

.captcha-row input {
  flex: 1;
}

.captcha-btn {
  padding: 12px 20px;
  border: 1px solid #4a8c3a;
  background: white;
  color: #4a8c3a;
  border-radius: 4px;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
}

.login-btn {
  background: #4a8c3a;
  color: white;
  border: none;
  padding: 14px;
  border-radius: 4px;
  font-size: 16px;
  font-weight: bold;
  margin-top: 10px;
}

.login-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.login-footer {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}

.login-footer a {
  color: #4a8c3a;
  margin-left: 5px;
}
</style>