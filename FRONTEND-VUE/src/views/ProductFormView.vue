<template>
  <div class="min-h-screen bg-gray-50">
    <NavBar />
    <div class="max-w-xl mx-auto px-4 py-8">
      <h1 class="text-2xl font-bold text-gray-800 mb-6">
        {{ isEdit ? 'Editar Producto' : 'Nuevo Producto' }}
      </h1>

      <form @submit.prevent="handleSubmit" class="bg-white rounded-xl shadow p-6 space-y-4">
        <div>
          <label class="label">Nombre del producto</label>
          <input v-model="form.productName" type="text" required class="input" />
        </div>
        <div>
          <label class="label">Categoría</label>
          <select v-model="form.categoryId" required class="input">
            <option value="">-- Seleccionar --</option>
            <option v-for="c in categories" :key="c.categoryId" :value="c.categoryId">
              {{ c.categoryName }}
            </option>
          </select>
        </div>
        <div>
          <label class="label">Proveedor</label>
          <select v-model="form.supplierId" required class="input">
            <option value="">-- Seleccionar --</option>
            <option v-for="s in suppliers" :key="s.supplierId" :value="s.supplierId">
              {{ s.companyName }}
            </option>
          </select>
        </div>
        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="label">Precio unitario</label>
            <input v-model.number="form.unitPrice" type="number" step="0.01" min="0.01" required class="input" />
          </div>
          <div>
            <label class="label">Unidades en stock</label>
            <input v-model.number="form.unitsInStock" type="number" min="0" required class="input" />
          </div>
        </div>
        <div class="flex items-center gap-2">
          <input :checked="form.discontinued === 1"
            @change="form.discontinued = $event.target.checked ? 1 : 0"
            type="checkbox" id="discontinued" class="w-4 h-4" />
          <label for="discontinued" class="text-sm text-gray-700">Descontinuado</label>
        </div>

        <p v-if="error" class="text-red-500 text-sm">{{ error }}</p>

        <div class="flex gap-3 pt-2">
          <button type="submit" :disabled="loading"
            class="bg-blue-600 hover:bg-blue-700 text-white px-5 py-2 rounded-lg font-medium">
            {{ loading ? 'Guardando...' : (isEdit ? 'Actualizar' : 'Crear') }}
          </button>
          <RouterLink to="/products"
            class="bg-gray-200 hover:bg-gray-300 text-gray-700 px-5 py-2 rounded-lg font-medium">
            Cancelar
          </RouterLink>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import NavBar from '../components/NavBar.vue'
import api from '../api/axios'

const route    = useRoute()
const router   = useRouter()
const isEdit   = computed(() => !!route.params.id)
const loading  = ref(false)
const error    = ref('')

const categories = ref([])
const suppliers  = ref([])

const form = ref({
  productName: '',
  categoryId: '',
  supplierId: '',
  unitPrice: '',
  unitsInStock: 0,
  discontinued: 0
})

onMounted(async () => {
  const [cats, sups] = await Promise.all([
    api.get('/categories'),
    api.get('/suppliers')
  ])
  categories.value = cats.data
  suppliers.value  = sups.data

  if (isEdit.value) {
    const { data } = await api.get(`/products/${route.params.id}`)
    form.value = {
      productName:   data.productName,
      categoryId:    data.categoryId,
      supplierId:    data.supplierId,
      unitPrice:     data.unitPrice,
      unitsInStock:  data.unitsInStock,
      discontinued:  data.discontinued
    }
  }
})

async function handleSubmit() {
  error.value   = ''
  loading.value = true
  try {
    if (isEdit.value) {
      await api.put(`/products/${route.params.id}`, form.value)
    } else {
      await api.post('/products', form.value)
    }
    router.push('/products')
  } catch (e) {
    error.value = e.response?.data?.error || 'Error al guardar el producto'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.label { @apply block text-sm font-medium text-gray-600 mb-1; }
.input { @apply w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-400; }
</style>
