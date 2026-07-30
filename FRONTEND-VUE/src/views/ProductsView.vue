<template>
  <div class="min-h-screen bg-gray-50">
    <NavBar />
    <div class="max-w-6xl mx-auto px-4 py-6">
      <div class="flex items-center justify-between mb-4">
        <h1 class="text-2xl font-bold text-gray-800">Productos</h1>
        <RouterLink to="/products/new"
          class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg text-sm font-medium">
          + Nuevo Producto
        </RouterLink>
      </div>

      <p v-if="error" class="text-red-500 mb-3">{{ error }}</p>

      <div v-if="loading" class="text-center text-gray-400 py-10">Cargando...</div>

      <div v-else class="overflow-x-auto bg-white rounded-xl shadow">
        <table class="w-full text-sm text-left">
          <thead class="bg-blue-50 text-blue-700 uppercase text-xs">
            <tr>
              <th class="px-4 py-3">ID</th>
              <th class="px-4 py-3">Nombre</th>
              <th class="px-4 py-3">Categoría</th>
              <th class="px-4 py-3">Proveedor</th>
              <th class="px-4 py-3">Precio</th>
              <th class="px-4 py-3">Stock</th>
              <th class="px-4 py-3">Estado</th>
              <th class="px-4 py-3">Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="p in products" :key="p.productId"
              class="border-t hover:bg-gray-50">
              <td class="px-4 py-2 text-gray-500">{{ p.productId }}</td>
              <td class="px-4 py-2 font-medium">{{ p.productName }}</td>
              <td class="px-4 py-2">{{ p.categoryName }}</td>
              <td class="px-4 py-2">{{ p.supplierName }}</td>
              <td class="px-4 py-2">${{ p.unitPrice }}</td>
              <td class="px-4 py-2">{{ p.unitsInStock }}</td>
              <td class="px-4 py-2">
                <span :class="p.discontinued === 1 ? 'bg-red-100 text-red-600' : 'bg-green-100 text-green-700'"
                  class="px-2 py-0.5 rounded-full text-xs font-semibold">
                  {{ p.discontinued === 1 ? 'Descontinuado' : 'Activo' }}
                </span>
              </td>
              <td class="px-4 py-2 flex gap-2">
                <RouterLink :to="`/products/${p.productId}/edit`"
                  class="text-blue-600 hover:underline">Editar</RouterLink>
                <button @click="disableProduct(p.productId)"
                  class="text-red-500 hover:underline">Deshabilitar</button>
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

const products = ref([])
const loading  = ref(true)
const error    = ref('')

async function fetchProducts() {
  loading.value = true
  try {
    const { data } = await api.get('/products')
    products.value = data
  } catch {
    error.value = 'Error al cargar los productos'
  } finally {
    loading.value = false
  }
}

async function disableProduct(id) {
  if (!confirm('¿Deshabilitar este producto?')) return
  try {
    await api.delete(`/products/${id}`)
    await fetchProducts()
  } catch (e) {
    alert(e.response?.data?.error || 'Error al deshabilitar')
  }
}

onMounted(fetchProducts)
</script>
