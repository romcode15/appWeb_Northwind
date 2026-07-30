<template>
  <div class="min-h-screen bg-gray-50">
    <NavBar />
    <div class="max-w-6xl mx-auto px-4 py-6">
      <h1 class="text-2xl font-bold text-gray-800 mb-6">Nueva Orden</h1>

      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <!-- Panel izquierdo: selección -->
        <div class="space-y-5">
          <!-- Cliente -->
          <div class="bg-white rounded-xl shadow p-5">
            <h2 class="font-semibold text-gray-700 mb-3">Cliente</h2>
            <select v-model="selectedCustomer" class="input w-full">
              <option value="">-- Seleccionar cliente --</option>
              <option v-for="c in customers" :key="c.customerId" :value="c">
                {{ c.companyName }}
              </option>
            </select>
          </div>

          <!-- Productos -->
          <div class="bg-white rounded-xl shadow p-5">
            <h2 class="font-semibold text-gray-700 mb-3">Agregar producto</h2>
            <select v-model="selectedProduct" class="input w-full mb-3">
              <option value="">-- Seleccionar producto --</option>
              <option v-for="p in activeProducts" :key="p.productId" :value="p">
                {{ p.productName }} — ${{ p.unitPrice }} (stock: {{ p.unitsInStock }})
              </option>
            </select>
            <div class="flex gap-2">
              <input v-model.number="qty" type="number" min="1" placeholder="Cantidad"
                class="input w-24" />
              <button @click="addToCart"
                class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg text-sm font-medium">
                Agregar
              </button>
            </div>
            <p v-if="addError" class="text-red-500 text-sm mt-2">{{ addError }}</p>
          </div>
        </div>

        <!-- Panel derecho: carrito -->
        <div class="bg-white rounded-xl shadow p-5">
          <h2 class="font-semibold text-gray-700 mb-4">Carrito</h2>

          <div v-if="cart.items.length === 0" class="text-gray-400 text-sm text-center py-8">
            El carrito está vacío
          </div>

          <div v-else>
            <table class="w-full text-sm mb-4">
              <thead class="text-xs uppercase text-gray-500 border-b">
                <tr>
                  <th class="py-2 text-left">Producto</th>
                  <th class="py-2 text-right">Precio</th>
                  <th class="py-2 text-right">Cant.</th>
                  <th class="py-2 text-right">Subtotal</th>
                  <th class="py-2"></th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in cart.items" :key="item.product.productId"
                  class="border-b">
                  <td class="py-2">{{ item.product.productName }}</td>
                  <td class="py-2 text-right">${{ item.product.unitPrice }}</td>
                  <td class="py-2 text-right">
                    <input v-model.number="item.quantity" type="number" min="1"
                      :max="item.product.unitsInStock"
                      class="w-16 border rounded px-1 text-right text-sm" />
                  </td>
                  <td class="py-2 text-right font-medium">
                    ${{ (item.product.unitPrice * item.quantity).toFixed(2) }}
                  </td>
                  <td class="py-2 text-right">
                    <button @click="cart.removeItem(item.product.productId)"
                      class="text-red-400 hover:text-red-600 text-xs">✕</button>
                  </td>
                </tr>
              </tbody>
            </table>

            <div class="flex justify-between font-bold text-gray-800 text-base border-t pt-3">
              <span>Total</span>
              <span>${{ cart.total.toFixed(2) }}</span>
            </div>

            <p v-if="orderError" class="text-red-500 text-sm mt-3">{{ orderError }}</p>

            <button @click="confirmOrder" :disabled="confirming"
              class="mt-4 w-full bg-green-600 hover:bg-green-700 text-white font-semibold py-2 rounded-lg">
              {{ confirming ? 'Procesando...' : 'Confirmar Orden' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import NavBar from '../components/NavBar.vue'
import api from '../api/axios'
import { useCartStore } from '../stores/cart'
import { useAuthStore } from '../stores/auth'

const router   = useRouter()
const cart     = useCartStore()
const auth     = useAuthStore()

const customers       = ref([])
const products        = ref([])
const selectedCustomer = ref('')
const selectedProduct  = ref('')
const qty              = ref(1)
const addError         = ref('')
const orderError       = ref('')
const confirming       = ref(false)

const activeProducts = computed(() =>
  products.value.filter(p => p.discontinued !== 1)
)

onMounted(async () => {
  cart.clear()
  try {
    const [c, p] = await Promise.all([
      api.get('/customers'),
      api.get('/products', { params: { page: 0, size: 9999 } })
    ])
    customers.value = c.data
    // /products ahora devuelve un Page<ProductResponse>, los datos están en .content
    products.value  = p.data.content ?? p.data
  } catch (e) {
    orderError.value = 'Error al cargar clientes o productos'
  }
})

function addToCart() {
  addError.value = ''
  if (!selectedProduct.value) { addError.value = 'Selecciona un producto'; return }
  if (!qty.value || qty.value <= 0) { addError.value = 'La cantidad debe ser mayor que cero'; return }
  if (qty.value > selectedProduct.value.unitsInStock) {
    addError.value = `Stock insuficiente (máx: ${selectedProduct.value.unitsInStock})`; return
  }
  cart.addItem(selectedProduct.value, qty.value)
  selectedProduct.value = ''
  qty.value = 1
}

async function confirmOrder() {
  orderError.value = ''
  if (!selectedCustomer.value) { orderError.value = 'Selecciona un cliente'; return }
  if (cart.items.length === 0) { orderError.value = 'El carrito está vacío'; return }

  confirming.value = true
  try {
    const payload = {
      customerId: selectedCustomer.value.customerId,
      appUserId:  auth.user.userId,
      items: cart.items.map(i => ({
        productId: i.product.productId,
        quantity:  i.quantity,
        discount:  i.discount || 0
      }))
    }
    const { data } = await api.post('/orders', payload)
    cart.clear()
    router.push(`/orders/${data.orderId}`)
  } catch (e) {
    orderError.value = e.response?.data?.error || 'Error al confirmar la orden'
  } finally {
    confirming.value = false
  }
}
</script>

<style scoped>
.input { @apply border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-400; }
</style>
