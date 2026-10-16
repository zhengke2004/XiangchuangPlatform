import axios from 'axios'
import { useAuthStore } from '@/stores/auth'

const instance = axios.create({
  baseURL: '/api',
  timeout: 10000
})

instance.interceptors.request.use(config => {
  const authStore = useAuthStore()
  if (authStore.token) {
    config.headers.Authorization = `Bearer ${authStore.token}`
  }
  return config
}, error => {
  return Promise.reject(error)
})

instance.interceptors.response.use(response => {
  return response.data
}, error => {
  if (error.response?.status === 401) {
    const authStore = useAuthStore()
    authStore.logout()
    window.location.href = '/login'
  }
  return Promise.reject(error)
})

export default {
  auth: {
    login(data) {
      return instance.post('/auth/login', data)
    },
    register(data) {
      return instance.post('/auth/register', data)
    },
    logout() {
      return instance.post('/auth/logout')
    },
    check() {
      return instance.get('/auth/check')
    },
    getMe() {
      return instance.get('/auth/me')
    }
  },
  products: {
    list(params) {
      return instance.get('/products', { params })
    },
    recommendations(limit = 3) {
      return instance.get('/products/recommendations', { params: { limit } })
    },
    hot(limit = 5) {
      return instance.get('/products/hot', { params: { limit } })
    },
    search(keyword, limit = 20) {
      return instance.get('/products/search', { params: { keyword, limit } })
    },
    detail(id) {
      return instance.get(`/products/${id}`)
    },
    reviews(id, page = 1, pageSize = 10) {
      return instance.get(`/products/${id}/reviews`, { params: { page, pageSize } })
    },
    addReview(id, data) {
      return instance.post(`/products/${id}/reviews`, data)
    },
    filters() {
      return instance.get('/products/filters/all')
    }
  },
  activities: {
    list(params) {
      return instance.get('/activities', { params })
    },
    theme(theme) {
      return instance.get(`/activities/theme/${theme}`)
    },
    calculate(data) {
      return instance.post('/activities/calculate', data)
    },
    recommendations(limit = 3) {
      return instance.get('/activities/recommendations', { params: { limit } })
    },
    detail(id) {
      return instance.get(`/activities/${id}`)
    },
    availability(id, date) {
      return instance.get(`/activities/${id}/availability`, { params: { date } })
    },
    availableDates(id) {
      return instance.get(`/activities/${id}/available-dates`)
    },
    reviews(id, page = 1, pageSize = 10) {
      return instance.get(`/activities/${id}/reviews`, { params: { page, pageSize } })
    },
    addReview(id, data) {
      return instance.post(`/activities/${id}/reviews`, data)
    }
  },
  orders: {
    create(data) {
      return instance.post('/orders', data)
    },
    list() {
      return instance.get('/orders')
    }
  },
  admin: {
    stats() {
      return instance.get('/admin/stats')
    },
    users: {
      list(params) {
        return instance.get('/admin/users', { params })
      },
      updateStatus(id, data) {
        return instance.put(`/admin/users/${id}/status`, data)
      },
      updateType(id, data) {
        return instance.put(`/admin/users/${id}/type`, data)
      }
    },
    goods: {
      list(params) {
        return instance.get('/admin/goods', { params })
      },
      create(data) {
        return instance.post('/admin/goods', data)
      },
      update(id, data) {
        return instance.put(`/admin/goods/${id}`, data)
      },
      updateStatus(id, data) {
        return instance.put(`/admin/goods/${id}/status`, data)
      },
      remove(id) {
        return instance.delete(`/admin/goods/${id}`)
      }
    },
    activities: {
      list(params) {
        return instance.get('/admin/activities', { params })
      },
      create(data) {
        return instance.post('/admin/activities', data)
      },
      update(id, data) {
        return instance.put(`/admin/activities/${id}`, data)
      },
      updateStatus(id, data) {
        return instance.put(`/admin/activities/${id}/status`, data)
      },
      remove(id) {
        return instance.delete(`/admin/activities/${id}`)
      },
      availability(id) {
        return instance.get(`/admin/activities/${id}/availability`)
      },
      addAvailability(id, data) {
        return instance.post(`/admin/activities/${id}/availability`, data)
      },
      deleteAvailability(id, availabilityId) {
        return instance.delete(`/admin/activities/${id}/availability/${availabilityId}`)
      }
    },
    orders: {
      list(params) {
        return instance.get('/admin/orders', { params })
      },
      detail(id) {
        return instance.get(`/admin/orders/${id}`)
      },
      updateStatus(id, data) {
        return instance.put(`/admin/orders/${id}/status`, data)
      }
    },
    reviews: {
      list(params) {
        return instance.get('/admin/reviews', { params })
      },
      remove(id) {
        return instance.delete(`/admin/reviews/${id}`)
      }
    },
    news: {
      list(params) {
        return instance.get('/admin/news', { params })
      },
      publish(data) {
        return instance.post('/admin/news', data)
      },
      remove(title, content) {
        return instance.delete('/admin/news', { params: { title, content } })
      }
    }
  }
}