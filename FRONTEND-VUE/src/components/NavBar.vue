<template>
  <nav class="bg-blue-700 text-white px-6 py-3 flex items-center justify-between shadow">
    <span class="font-bold text-lg">Northwind</span>
    <div class="flex items-center gap-4 text-sm">
      <RouterLink to="/products" class="hover:underline">Productos</RouterLink>
      <RouterLink to="/orders/new" class="hover:underline">Nueva Orden</RouterLink>
      <span class="text-blue-200">{{ auth.user?.fullName }}</span>
      <button @click="handleLogout" class="bg-blue-900 hover:bg-blue-800 px-3 py-1 rounded-lg">
        Salir
      </button>
    </div>
  </nav>
</template>

<script setup>
import { useAuthStore } from '../stores/auth'
import { useRouter } from 'vue-router'

const auth   = useAuthStore()
const router = useRouter()

function handleLogout() {
  auth.logout()
  router.push('/login')
}
</script>
