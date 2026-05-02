import { ref } from 'vue'
import { foodService } from '@/services/food'
import type { FoodSearchResult } from '@/types'
import { useDebounce } from '@vueuse/core'

export function useFoodSearch() {
  const query = ref('')
  const store = ref('')
  const results = ref<FoodSearchResult[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  const debouncedQuery = useDebounce(query, 400)

  async function search() {
    if (!debouncedQuery.value.trim()) { results.value = []; return }
    loading.value = true
    error.value = null
    try {
      const res = await foodService.search(debouncedQuery.value, store.value || undefined)
      results.value = res.data.data ?? []
    } catch (e: any) {
      error.value = e?.response?.data?.error?.message ?? '搜尋失敗'
    } finally {
      loading.value = false
    }
  }

  function clear() { query.value = ''; results.value = [] }

  return { query, store, results, loading, error, debouncedQuery, search, clear }
}
