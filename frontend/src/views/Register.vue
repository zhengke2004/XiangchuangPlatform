<template>
  <div class="register">
    <div class="container">
      <div class="register-card">
        <div class="register-header">
          <h1>注册</h1>
          <p>创建您的乡创农游智能体平台账号</p>
        </div>
        <form @submit.prevent="handleRegister" class="register-form">
          <div class="form-group">
            <label>用户名</label>
            <input v-model="form.username" type="text" placeholder="请输入用户名" />
          </div>
          <div class="form-group">
            <label>手机号</label>
            <input v-model="form.phone" type="tel" placeholder="请输入手机号" />
          </div>
          <div class="form-group">
            <label>密码</label>
            <input v-model="form.password" type="password" placeholder="请输入密码" />
          </div>
          <div class="form-group">
            <label>确认密码</label>
            <input v-model="form.confirmPassword" type="password" placeholder="请再次输入密码" />
          </div>
          <div class="form-group">
            <label>验证码</label>
            <div class="captcha-row">
              <input v-model="form.captcha" type="text" placeholder="请输入验证码" />
              <button type="button" class="captcha-btn" @click="getCaptcha">{{ captchaText }}</button>
            </div>
          </div>
          <div class="form-group">
            <label class="checkbox-label">
              <input type="checkbox" v-model="form.agree" />
              我已阅读并同意<a href="#" class="link">《用户协议》</a>和<a href="#" class="link">《隐私政策》</a>
            </label>
          </div>
          <button type="submit" class="register-btn" :disabled="loading">注册</button>
        </form>
        <div class="register-footer">
          <span>已有账号？</span>
          <router-link to="/login">立即登录</router-link>
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

const form = reactive({
  username: '',
  phone: '',
  password: '',
  confirmPassword: '',
  captcha: '',
  agree: false
})

const loading = ref(false)
const captchaText = ref('获取验证码')

async function getCaptcha() {
  if (!form.phone) {
    ElMessage.warning('请先输入手机号')
    return
  }
  captchaText.value = '发送中...'
  setTimeout(() => {
    captchaText.value = '获取验证码'
    ElMessage.success('验证码已发送')
  }, 1000)
}

async function handleRegister() {
  if (!form.username || !form.phone || !form.password || !form.confirmPassword || !form.captcha) {
    ElMessage.warning('请填写完整信息')
    return
  }

  if (form.password !== form.confirmPassword) {
    ElMessage.warning('两次输入的密码不一致')
    return
  }

  if (!form.agree) {
    ElMessage.warning('请阅读并同意用户协议和隐私政策')
    return
  }

  loading.value = true
  try {
    await authStore.register({
      username: form.username,
      phone: form.phone,
      password: form.password
    })
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch (e) {
    ElMessage.error('注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register {
  min-height: calc(100vh - 60px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 30px 0;
}

.register-card {
  background: white;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.1);
  width: 400px;
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
}

.register-header h1 {
  font-size: 28px;
  margin-bottom: 10px;
}

.register-header p {
  color: #666;
}

.register-form {
  display: flex;
  flex-direction: column;
}

.form-group {
  margin-bottom: 15px;
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
  align-items: flex-start;
  gap: 10px;
  font-size: 13px;
  color: #666;
}

.link {
  color: #4a8c3a;
}

.register-btn {
  background: #4a8c3a;
  color: white;
  border: none;
  padding: 14px;
  border-radius: 4px;
  font-size: 16px;
  font-weight: bold;
  margin-top: 10px;
}

.register-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.register-footer {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}

.register-footer a {
  color: #4a8c3a;
  margin-left: 5px;
}
</style>