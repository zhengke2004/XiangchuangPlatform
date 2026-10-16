import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('@/views/Home.vue')
    },
    {
      path: '/market',
      name: 'Market',
      component: () => import('@/views/Market.vue')
    },
    {
      path: '/travel',
      name: 'Travel',
      component: () => import('@/views/Travel.vue')
    },
    {
      path: '/guide',
      name: 'Guide',
      component: () => import('@/views/Guide.vue')
    },
    {
      path: '/account',
      name: 'Account',
      component: () => import('@/views/Account.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/Login.vue'),
      meta: { guestOnly: true }
    },
    {
      path: '/register',
      name: 'Register',
      component: () => import('@/views/Register.vue'),
      meta: { guestOnly: true }
    },
    {
      path: '/product/:id',
      name: 'ProductDetail',
      component: () => import('@/views/ProductDetail.vue')
    },
    {
      path: '/activity/:id',
      name: 'ActivityDetail',
      component: () => import('@/views/ActivityDetail.vue')
    },
    {
      path: '/orders',
      name: 'Orders',
      component: () => import('@/views/Orders.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/favorites',
      name: 'Favorites',
      component: () => import('@/views/Favorites.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/cart',
      name: 'Cart',
      component: () => import('@/views/Cart.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/admin',
      component: () => import('@/views/admin/AdminLayout.vue'),
      meta: { requiresAdmin: true },
      children: [
        { path: '', name: 'AdminDashboard', component: () => import('@/views/admin/AdminDashboard.vue') },
        { path: 'users', name: 'AdminUsers', component: () => import('@/views/admin/AdminUsers.vue') },
        { path: 'goods', name: 'AdminGoods', component: () => import('@/views/admin/AdminGoods.vue') },
        { path: 'activities', name: 'AdminActivities', component: () => import('@/views/admin/AdminActivities.vue') },
        { path: 'orders', name: 'AdminOrders', component: () => import('@/views/admin/AdminOrders.vue') },
        { path: 'reviews', name: 'AdminReviews', component: () => import('@/views/admin/AdminReviews.vue') },
        { path: 'news', name: 'AdminNews', component: () => import('@/views/admin/AdminNews.vue') }
      ]
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('@/views/NotFound.vue')
    }
  ]
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  // 刷新后从localStorage恢复用户信息
  if (!authStore.user && authStore.isLogin) {
    authStore.loadUserFromStorage()
  }

  if (to.meta.requiresAuth && !authStore.isLogin) {
    next('/login')
  } else if (to.meta.requiresAdmin) {
    if (!authStore.isLogin) {
      next('/login')
    } else if (authStore.user?.userType !== '管理员') {
      next('/')
    } else {
      next()
    }
  } else if (to.meta.guestOnly && authStore.isLogin) {
    next('/')
  } else {
    next()
  }
})

export default router