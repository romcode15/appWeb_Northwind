import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

import LoginView from '../views/LoginView.vue'
import ProductsView from '../views/ProductsView.vue'
import ProductFormView from '../views/ProductFormView.vue'
import OrderNewView from '../views/OrderNewView.vue'
import OrderDetailView from '../views/OrderDetailView.vue'

const routes = [
  { path: '/',          redirect: '/login' },
  { path: '/login',     component: LoginView },
  { path: '/products',  component: ProductsView,    meta: { requiresAuth: true } },
  { path: '/products/new',       component: ProductFormView, meta: { requiresAuth: true } },
  { path: '/products/:id/edit',  component: ProductFormView, meta: { requiresAuth: true } },
  { path: '/orders/new',         component: OrderNewView,    meta: { requiresAuth: true } },
  { path: '/orders/:id',         component: OrderDetailView, meta: { requiresAuth: true } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to) => {
  const auth = useAuthStore()
  if (to.meta.requiresAuth && !auth.user) {
    return '/login'
  }
})

export default router
