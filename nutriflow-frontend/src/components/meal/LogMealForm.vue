<script setup lang="ts">
import { ref, computed } from 'vue'
import { today, MEAL_LABELS } from '@/utils/date'
import FoodSearchPanel from './FoodSearchPanel.vue'
import PhotoUploader from './PhotoUploader.vue'
import type { FoodSearchResult } from '@/types'

const emit = defineEmits<{ submit: [any]; cancel: [] }>()

const mealDate = ref(today())
const mealIndex = ref(0)
const storeName = ref('')
const note = ref('')
const mode = ref<'search' | 'photo'>('search')

const items = ref<Array<{ foodName: string; kcal: number; protein?: number; carbs?: number; fat?: number; sodium?: number; dataSource?: string; photoUsed?: boolean }>>([])

const totalKcal = computed(() => items.value.reduce((s, i) => s + i.kcal, 0))

function addFromSearch(food: FoodSearchResult) {
  items.value.push({
    foodName: food.itemName,
    kcal: food.kcal,
    protein: food.protein,
    carbs: food.carbs,
    fat: food.fat,
    sodium: food.sodium,
    dataSource: food.source
  })
}

function addFromPhoto(photoItems: any[]) {
  photoItems.forEach(i => items.value.push({ ...i, photoUsed: true, dataSource: 'ai' }))
}

function removeItem(idx: number) {
  items.value.splice(idx, 1)
}

function submit() {
  emit('submit', {
    mealDate: mealDate.value,
    mealIndex: mealIndex.value,
    storeName: storeName.value || undefined,
    note: note.value || undefined,
    items: items.value
  })
}
</script>

<template>
  <div class="space-y-4">
    <div class="grid grid-cols-2 gap-3">
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">日期</label>
        <input v-model="mealDate" type="date" class="input" />
      </div>
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">餐別</label>
        <select v-model="mealIndex" class="input">
          <option v-for="(label, i) in MEAL_LABELS" :key="i" :value="i">{{ label }}</option>
        </select>
      </div>
    </div>

    <input v-model="storeName" class="input" placeholder="店家名稱（選填）" />

    <div class="flex gap-2">
      <button
        class="flex-1 py-2 rounded-xl text-sm font-medium transition-colors"
        :class="mode === 'search' ? 'bg-primary-500 text-white' : 'bg-gray-100 text-gray-600'"
        @click="mode = 'search'"
      >🔍 搜尋食物</button>
      <button
        class="flex-1 py-2 rounded-xl text-sm font-medium transition-colors"
        :class="mode === 'photo' ? 'bg-primary-500 text-white' : 'bg-gray-100 text-gray-600'"
        @click="mode = 'photo'"
      >📷 拍照辨識</button>
    </div>

    <FoodSearchPanel v-if="mode === 'search'" @select="addFromSearch" />
    <PhotoUploader v-else @analyzed="addFromPhoto" />

    <div v-if="items.length > 0" class="space-y-2">
      <h4 class="text-sm font-semibold text-gray-700">已選食物</h4>
      <div
        v-for="(item, i) in items"
        :key="i"
        class="flex items-center justify-between bg-orange-50 rounded-xl px-3 py-2"
      >
        <div>
          <p class="text-sm font-medium text-gray-800">{{ item.foodName }}</p>
          <p class="text-xs text-gray-400">{{ Math.round(item.kcal) }} kcal</p>
        </div>
        <button class="text-gray-300 hover:text-red-400" @click="removeItem(i)">✕</button>
      </div>
      <div class="flex justify-between text-sm font-bold text-primary-500 px-1">
        <span>合計</span>
        <span>{{ Math.round(totalKcal) }} kcal</span>
      </div>
    </div>

    <textarea v-model="note" class="input" rows="2" placeholder="備注（選填）" />

    <div class="flex gap-3">
      <button class="btn-secondary flex-1" @click="$emit('cancel')">取消</button>
      <button class="btn-primary flex-1" :disabled="items.length === 0" @click="submit">記錄餐點</button>
    </div>
  </div>
</template>
