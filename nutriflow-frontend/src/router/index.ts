import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: () => import('@/layouts/AppLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        { path: '', redirect: '/dashboard' },
        { path: 'dashboard', name: 'Dashboard', component: () => import('@/pages/DashboardPage.vue') },
        { path: 'menu', name: 'Menu', component: () => import('@/pages/MenuPage.vue') },
        { path: 'log', name: 'Log', component: () => import('@/pages/LogMealPage.vue') },
        { path: 'history', name: 'History', component: () => import('@/pages/HistoryPage.vue') },
        { path: 'weight', name: 'Weight', component: () => import('@/pages/WeightPage.vue') },
        { path: 'nutrition', name: 'Nutrition', component: () => import('@/pages/NutritionPage.vue') },
        { path: 'profile', name: 'Profile', component: () => import('@/pages/ProfilePage.vue') },
        { path: 'cat', name: 'Cat', component: () => import('@/pages/CatPage.vue') }
      ]
    },
    {
      path: '/',
      component: () => import('@/layouts/AuthLayout.vue'),
      meta: { guest: true },
      children: [
        { path: 'login',    name: 'Login',    component: () => import('@/pages/LoginPage.vue') },
        { path: 'register', name: 'Register', component: () => import('@/pages/RegisterPage.vue') }
      ]
    },
    {
      path: '/setup',
      name: 'Setup',
      component: () => import('@/pages/SetupPage.vue'),
      meta: { requiresAuth: true }
    }
  ]
})

router.beforeEach((to, _from, next) => {
  const auth = useAuthStore()
  if (to.meta.requiresAuth && !auth.isLoggedIn) {
    next('/login')
  } else if (to.meta.guest && auth.isLoggedIn) {
    next('/dashboard')
  } else if (to.meta.requiresAuth && auth.isLoggedIn && !auth.isSetupComplete && to.name !== 'Setup') {
    next('/setup')
  } else {
    next()
  }
})

export default router
