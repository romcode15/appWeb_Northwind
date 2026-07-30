<template>
  <div class="min-h-screen bg-gray-50">
    <NavBar />
    <div class="max-w-3xl mx-auto px-4 py-8">
      <div v-if="loading" class="text-center text-gray-400 py-10">Cargando orden...</div>

      <div v-else-if="error" class="text-red-500 text-center py-10">{{ error }}</div>

      <div v-else class="bg-white rounded-xl shadow p-6">
        <!-- Encabezado -->
        <div class="flex items-center justify-between mb-6">
          <div>
            <h1 class="text-2xl font-bold text-gray-800">Orden #{{ order.orderId }}</h1>
            <p class="text-sm text-gray-500 mt-1">{{ order.orderDate }}</p>
          </div>
          <span class="bg-green-100 text-green-700 px-3 py-1 rounded-full text-sm font-semibold">
            Registrada
          </span>
        </div>

        <!-- Info -->
        <div class="grid grid-cols-2 gap-4 mb-6 text-sm">
          <div>
            <p class="text-gray-500">Cliente</p>
            <p class="font-medium">{{ order.customerName }}</p>
          </div>
          <div>
            <p class="text-gray-500">Responsable</p>
            <p class="font-medium">{{ order.appUserFullName }}</p>
          </div>
        </div>

        <!-- Detalles -->
        <h2 class="font-semibold text-gray-700 mb-3">Productos</h2>
        <table class="w-full text-sm mb-4">
          <thead class="text-xs uppercase text-gray-500 bg-gray-50 border-b">
            <tr>
              <th class="px-3 py-2 text-left">Producto</th>
              <th class="px-3 py-2 text-right">Precio</th>
              <th class="px-3 py-2 text-right">Cant.</th>
              <th class="px-3 py-2 text-right">Desc.</th>
              <th class="px-3 py-2 text-right">Subtotal</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="d in order.details" :key="d.productId" class="border-b">
              <td class="px-3 py-2">{{ d.productName }}</td>
              <td class="px-3 py-2 text-right">${{ d.unitPrice }}</td>
              <td class="px-3 py-2 text-right">{{ d.quantity }}</td>
              <td class="px-3 py-2 text-right">{{ (d.discount * 100).toFixed(0) }}%</td>
              <td class="px-3 py-2 text-right font-medium">${{ Number(d.subtotal).toFixed(2) }}</td>
            </tr>
          </tbody>
        </table>

        <div class="flex justify-between font-bold text-gray-800 text-lg border-t pt-3">
          <span>Total</span>
          <span>${{ Number(order.total).toFixed(2) }}</span>
        </div>

        <div class="mt-6 flex gap-3">
          <RouterLink to="/orders/new"
            class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg text-sm font-medium">
            Nueva Orden
          </RouterLink>
          <RouterLink to="/products"
            class="bg-gray-200 hover:bg-gray-300 text-gray-700 px-4 py-2 rounded-lg text-sm font-medium">
            Ir a Productos
          </RouterLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import NavBar from '../components/NavBar.vue'
import api from '../api/axios'

const route   = useRoute()
const order   = ref(null)
const loading = ref(true)
const error   = ref('')

onMounted(async () => {
  try {
    const { data } = await api.get(`/orders/${route.params.id}`)
    order.value = data
  } catch {
    error.value = 'No se pudo cargar la orden'
  } finally {
    loading.value = false
  }
})
</script>
