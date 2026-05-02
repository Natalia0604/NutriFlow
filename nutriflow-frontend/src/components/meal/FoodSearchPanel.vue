<script setup lang="ts">
import { ref, watch } from 'vue'
import { useFoodSearch } from '@/composables/useFoodSearch'
import { useCustomFood } from '@/composables/useCustomFood'
import type { FoodSearchResult, CustomFood } from '@/types'
import LoadingSpinner from '@/components/base/LoadingSpinner.vue'
import CustomFoodForm from './CustomFoodForm.vue'

const emit = defineEmits<{ select: [FoodSearchResult] }>()

const tab = ref<'search' | 'library' | 'new'>('search')

// ── Search tab ─────────────────────────────────────────────────────────
const { query, results, loading, error, debouncedQuery, search } = useFoodSearch()
watch(debouncedQuery, search)

// ── Library tab ────────────────────────────────────────────────────────
const { data: customFoods, isFetching: loadingLibrary, create, update, remove } = useCustomFood()
const editingFood = ref<CustomFood | null>(null)

function selectCustomFood(food: CustomFood) {
  emit('select', {
    itemName: food.foodName,
    sizeLabel: food.sizeLabel,
    kcal: food.kcal,
    protein: food.protein,
    carbs: food.carbs,
    fat: food.fat,
    sodium: food.sodium,
    source: 'custom'
  })
}

function saveToLibrary(food: FoodSearchResult) {
  create.mutate({
    foodName: food.itemName,
    sizeLabel: food.sizeLabel,
    kcal: food.kcal,
    protein: food.protein,
    carbs: food.carbs,
    fat: food.fat,
    sodium: food.sodium
  })
}

function onUpdateFood(id: number, data: any) {
  update.mutate({ id, data })
  editingFood.value = null
}

function onDeleteFood(id: number) {
  if (confirm('確定要刪除此食品嗎？')) remove.mutate(id)
}
</script>

<template>
  <div class="space-y-3">
    <!-- Tabs -->
    <div class="flex gap-1 bg-gray-100 rounded-xl p-1">
      <button
        v-for="({ key, label }) in [
          { key: 'search',  label: '🔍 搜尋' },
          { key: 'library', label: '📚 食品庫' },
          { key: 'new',     label: '➕ 新增' },
        ]"
        :key="key"
        class="flex-1 py-1.5 text-xs font-semibold rounded-lg transition-all"
        :class="tab === key ? 'bg-white text-orange-500 shadow-sm' : 'text-gray-500'"
        @click="tab = (key as any)"
      >{{ label }}</button>
    </div>

    <!-- Search tab -->
    <template v-if="tab === 'search'">
      <input
        v-model="query"
        class="input text-sm"
        placeholder="搜尋食物名稱..."
        type="search"
      />
      <div v-if="loading" class="py-4 flex justify-center"><LoadingSpinner /></div>
      <div v-else class="space-y-1.5 max-h-52 overflow-y-auto">
        <div
          v-for="item in results"
          :key="`${item.storeName}-${item.itemName}`"
          class="flex items-center gap-2 p-2.5 rounded-xl border border-gray-100 hover:border-orange-200 hover:bg-orange-50 transition-colors"
        >
          <button class="flex-1 text-left" @click="emit('select', item)">
            <p class="font-medium text-gray-800 text-sm leading-tight">{{ item.itemName }}</p>
            <p class="text-xs text-gray-400">
              {{ item.storeName ?? (item.source === 'custom' ? '我的食品庫' : '食物資料庫') }}
              · {{ Math.round(item.kcal) }} kcal
            </p>
          </button>
          <button
            v-if="item.source !== 'custom'"
            class="shrink-0 text-[11px] px-2 py-1 rounded-lg bg-orange-100 text-orange-500 font-semibold hover:bg-orange-200 transition-colors"
            title="儲存到食品庫"
            @click.stop="saveToLibrary(item)"
          >儲存</button>
        </div>
        <p v-if="error" class="text-center text-red-400 text-sm py-4">{{ error }}</p>
        <p v-else-if="query && !loading && results.length === 0" class="text-center text-gray-400 text-sm py-4">
          找不到「{{ query }}」的相關食物
        </p>
      </div>
    </template>

    <!-- Library tab -->
    <template v-else-if="tab === 'library'">
      <div v-if="loadingLibrary" class="py-4 flex justify-center"><LoadingSpinner /></div>
      <div v-else-if="!customFoods?.length" class="text-center text-gray-400 text-sm py-6">
        <p class="text-2xl mb-2">🍱</p>
        <p>尚無自訂食品，前往「新增」頁面建立</p>
      </div>
      <div v-else class="space-y-1.5 max-h-52 overflow-y-auto">
        <template v-for="food in customFoods" :key="food.id">
          <!-- Edit form inline -->
          <div v-if="editingFood?.id === food.id" class="border border-orange-200 rounded-xl p-3 bg-orange-50">
            <CustomFoodForm
              :initial="food"
              @save="onUpdateFood(food.id, $event)"
              @cancel="editingFood = null"
            />
          </div>
          <!-- Normal row -->
          <div
            v-else
            class="flex items-center gap-2 p-2.5 rounded-xl border border-gray-100 hover:border-orange-200 hover:bg-orange-50 transition-colors"
          >
            <button class="flex-1 text-left" @click="selectCustomFood(food)">
              <p class="font-medium text-gray-800 text-sm leading-tight">{{ food.foodName }}</p>
              <p class="text-xs text-gray-400">
                {{ food.sizeLabel ?? '—' }} · {{ Math.round(food.kcal) }} kcal
              </p>
            </button>
            <button class="shrink-0 text-gray-300 hover:text-orange-400 text-sm px-1" @click.stop="editingFood = food">✏️</button>
            <button class="shrink-0 text-gray-300 hover:text-red-400 text-sm px-1" @click.stop="onDeleteFood(food.id)">🗑️</button>
          </div>
        </template>
      </div>
    </template>

    <!-- New food tab -->
    <template v-else>
      <CustomFoodForm
        @save="(data) => { create.mutate(data); tab = 'library' }"
        @cancel="tab = 'search'"
      />
    </template>
  </div>
</template>
