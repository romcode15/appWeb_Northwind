import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useCartStore = defineStore('cart', () => {
  const items = ref([])  // { product, quantity, discount }

  function addItem(product, quantity = 1, discount = 0) {
    const existing = items.value.find(i => i.product.productId === product.productId)
    if (existing) {
      existing.quantity += quantity
    } else {
      items.value.push({ product, quantity, discount })
    }
  }

  function removeItem(productId) {
    items.value = items.value.filter(i => i.product.productId !== productId)
  }

  function updateQuantity(productId, quantity) {
    const item = items.value.find(i => i.product.productId === productId)
    if (item) item.quantity = quantity
  }

  function clear() {
    items.value = []
  }

  const total = computed(() =>
    items.value.reduce((sum, i) => {
      const sub = i.product.unitPrice * i.quantity * (1 - (i.discount || 0))
      return sum + sub
    }, 0)
  )

  return { items, addItem, removeItem, updateQuantity, clear, total }
})
