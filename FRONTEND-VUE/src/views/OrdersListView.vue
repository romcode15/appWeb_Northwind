<template>
  <div class="min-h-screen bg-gray-50">
    <NavBar />
    <div class="max-w-6xl mx-auto px-4 py-6">
      <div class="flex items-center justify-between mb-4">
        <h1 class="text-2xl font-bold text-gray-800">Órdenes registradas</h1>
        <RouterLink to="/orders/new"
          class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg text-sm font-medium">
          + Nueva Orden
        </RouterLink>
      </div>

      <div v-if="loading" class="text-center text-gray-400 py-10">Cargando...</div>

      <div v-else-if="orders.length === 0" class="text-center text-gray-400 py-10">
        No hay órdenes registradas aún.
      </div>

      <div v-else class="overflow-x-auto bg-white rounded-xl shadow">
        <table class="w-full text-sm text-left">
          <thead class="bg-blue-50 text-blue-700 uppercase text-xs">
            <tr>
              <th class="px-4 py-3"># Orden</th>
              <th class="px-4 py-3">Fecha</th>
              <th class="px-4 py-3">Cliente</th>
              <th class="px-4 py-3">Responsable</th>
              <th class="px-4 py-3 text-right">Total</th>
              <th class="px-4 py-3 text-center">Productos</th>
              <th class="px-4 py-3"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="o in orders" :key="o.orderId" class="border-t hover:bg-gray-50">
              <td class="px-4 py-2 font-semibold text-blue-700">#{{ o.orderId }}</td>
              <td class="px-4 py-2 text-gray-500">{{ o.orderDate }}</td>
              <td class="px-4 py-2">{{ o.customerName }}</td>
              <td class="px-4 py-2">{{ o.appUserFullName }}</td>
              <td class="px-4 py-2 text-right font-medium">${{ Number(o.total).toFixed(2) }}</td>
              <td class="px-4 py-2 text-center text-gray-500">{{ o.details?.length ?? 0 }}</td>
              <td class="px-4 py-2 text-right">
                <RouterLink :to="`/orders/${o.orderId}`"
                  class="text-blue-600 hover:underline text-xs font-medium">
                  Ver detalle →
                </RouterLink>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import NavBar from '../components/NavBar.vue'
import api from '../api/axios'

const orders  = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const { data } = await api.get('/orders')
    orders.value = data
  } catch {
    orders.value = []
  } finally {
    loading.value = false
  }
})
</script>
