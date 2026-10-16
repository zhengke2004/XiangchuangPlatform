import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/api'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || null)
  const user = ref(null)
  const isLogin = computed(() => !!token.value)

  async function login(phone, password) {
    const response = await api.auth.login({ phone, password })
    token.value = response.data.token
    user.value = response.data.user
    localStorage.setItem('token', token.value)
    localStorage.setItem('user', JSON.stringify(user.value))
  }

  async function register(data) {
    await api.auth.register(data)
  }

  async function logout() {
    await api.auth.logout()
    token.value = null
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  async function checkLogin() {
    try {
      const response = await api.auth.check()
      if (response.data.isLogin) {
        user.value = response.data.user
        return true
      }
    } catch (e) {
      // ignore
    }
    return false
  }

  function loadUserFromStorage() {
    const storedUser = localStorage.getItem('user')
    if (storedUser) {
      user.value = JSON.parse(storedUser)
    }
  }

  return {
    token,
    user,
    isLogin,
    login,
    register,
    logout,
    checkLogin,
    loadUserFromStorage
  }
})