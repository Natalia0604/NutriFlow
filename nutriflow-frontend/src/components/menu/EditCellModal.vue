<script setup lang="ts">
import { ref, computed } from 'vue'
import type { MealCell, FoodSearchResult } from '@/types'
import FoodSearchPanel from '@/components/meal/FoodSearchPanel.vue'
import PhotoUploader from '@/components/meal/PhotoUploader.vue'

const props = defineProps<{
  cell: MealCell | { id: number; dayIndex: number; mealIndex: number; mealType?: string; logged?: boolean; source?: string; items?: any[]; totalKcal?: number }
}>()
const emit = defineEmits<{ save: [any]; cancel: [] }>()

const DAY_LABELS  = ['週一', '週二', '週三', '週四', '週五', '週六', '週日']
const MEAL_LABELS = ['早餐', '午餐', '晚餐']
const MEAL_EMOJI  = ['🍳', '🍱', '🍲']

const dayLabel  = DAY_LABELS[props.cell.dayIndex]
const mealLabel = MEAL_LABELS[props.cell.mealIndex]
const mealEmoji = MEAL_EMOJI[props.cell.mealIndex]

type EditItem = { foodName: string; kcal: number; protein?: number; carbs?: number; fat?: number; sodium?: number }

const mealType = ref<'外食' | '自炊'>(() => {
  const t = props.cell.mealType
  return (t === '外食' || t === '自炊') ? t : '外食'
})
const note = ref((props.cell as MealCell).note ?? '')
const logTime = ref((props.cell as MealCell).logTime ?? '')
const items = ref<EditItem[]>(
  (props.cell.items ?? []).map(i => ({
    foodName: i.foodName, kcal: i.kcal,
    protein: i.protein, carbs: i.carbs, fat: i.fat, sodium: i.sodium
  }))
)

// Quick-add
const quickName = ref('')
const quickKcal = ref<number | null>(null)
const showSearch = ref(false)
const showPhoto  = ref(false)

const totalKcal    = computed(() => items.value.reduce((s, i) => s + (i.kcal ?? 0), 0))
const totalProtein = computed(() => items.value.reduce((s, i) => s + (i.protein ?? 0), 0))
const totalCarbs   = computed(() => items.value.reduce((s, i) => s + (i.carbs ?? 0), 0))
const totalFat     = computed(() => items.value.reduce((s, i) => s + (i.fat ?? 0), 0))
const hasMacros    = computed(() => items.value.some(i => i.protein != null || i.carbs != null || i.fat != null))

function addFromSearch(food: FoodSearchResult) {
  items.value.push({
    foodName: food.itemName, kcal: food.kcal,
    protein: food.protein, carbs: food.carbs, fat: food.fat, sodium: food.sodium
  })
  showSearch.value = false
  quickName.value = ''
}

function addFromPhoto(photoItems: any[]) {
  photoItems.forEach(i => items.value.push({ foodName: i.foodName, kcal: i.kcal, protein: i.protein, carbs: i.carbs, fat: i.fat }))
  showPhoto.value = false
}

function addManual() {
  const name = quickName.value.trim()
  if (!name) { showSearch.value = !showSearch.value; return }
  items.value.push({ foodName: name, kcal: quickKcal.value ?? 0 })
  quickName.value = ''
  quickKcal.value = null
}

function removeItem(i: number) { items.value.splice(i, 1) }

function submit() {
  emit('save', {
    dayIndex:  props.cell.dayIndex,
    mealIndex: props.cell.mealIndex,
    mealType:  mealType.value,
    logTime:   logTime.value || undefined,
    note:      note.value    || undefined,
    items: items.value.map(i => ({
      foodName: i.foodName, kcal: i.kcal,
      protein: i.protein, carbs: i.carbs, fat: i.fat, sodium: i.sodium
    }))
  })
}
</script>

<template>
  <Teleport to="body">
    <div
      class="fixed inset-0 bg-black/40 z-50 flex items-end justify-center"
      @click.self="$emit('cancel')"
    >
      <div class="bg-white rounded-t-3xl w-full max-w-sm max-h-[92vh] flex flex-col">

        <!-- Header -->
        <div class="flex items-center justify-between px-5 pt-5 pb-3 shrink-0">
          <div class="flex items-center gap-2">
            <span class="text-2xl">{{ mealEmoji }}</span>
            <div>
              <h3 class="font-black text-gray-800 text-base leading-tight">{{ dayLabel }} {{ mealLabel }}</h3>
              <div v-if="(cell as any).logged" class="flex items-center gap-1 mt-0.5">
                <span class="text-[11px] text-emerald-500 font-semibold">✅ 已記錄</span>
              </div>
            </div>
          </div>
          <button class="text-gray-300 hover:text-gray-500 text-2xl leading-none" @click="$emit('cancel')">×</button>
        </div>

        <div class="overflow-y-auto flex-1 px-5 pb-3 space-y-4">

          <!-- 外食 / 自炊 toggle -->
          <div class="flex gap-2">
            <button
              v-for="t in ['外食', '自炊']"
              :key="t"
              class="flex-1 py-2.5 rounded-2xl text-sm font-bold border-2 transition-all flex items-center justify-center gap-1.5"
              :class="mealType === t
                ? 'border-orange-400 bg-orange-50 text-orange-500'
                : 'border-gray-100 bg-white text-gray-400'"
              @click="mealType = t as any"
            >
              <span>{{ t === '外食' ? '🏠' : '🔍' }}</span>
              <span>{{ t }}</span>
            </button>
          </div>

          <!-- Food items -->
          <div>
            <p class="text-sm font-black text-gray-700 mb-2">
              餐點內容
              <span class="text-gray-400 font-normal text-xs">（可新增多項）</span>
            </p>

            <div class="space-y-2">
              <div
                v-for="(item, i) in items"
                :key="i"
                class="flex items-center gap-2 bg-gray-50 rounded-2xl px-4 py-3 border border-gray-100"
              >
                <span class="flex-1 text-sm text-gray-800 font-medium truncate">{{ item.foodName }}</span>
                <input
                  v-model.number="item.kcal"
                  type="number"
                  min="0"
                  class="w-16 text-right text-orange-500 font-black text-sm bg-transparent outline-none border-b border-transparent focus:border-orange-300 shrink-0"
                />
                <span class="text-gray-400 text-xs shrink-0">kcal</span>
                <button class="text-gray-300 hover:text-red-400 shrink-0 ml-1" @click="removeItem(i)">✕</button>
              </div>

              <!-- Quick-add row -->
              <div class="flex gap-2">
                <input
                  v-model="quickName"
                  class="flex-1 rounded-2xl border border-gray-200 px-4 py-3 text-sm outline-none focus:border-orange-300 bg-white min-w-0"
                  placeholder="食物名稱，例：雞腿飯"
                  @focus="showSearch = true"
                  @keydown.enter="addManual"
                />
                <input
                  v-model.number="quickKcal"
                  type="number"
                  min="0"
                  class="w-20 rounded-2xl border border-gray-200 px-3 py-3 text-sm outline-none focus:border-orange-300 bg-white text-center shrink-0"
                  placeholder="kcal"
                  @keydown.enter="addManual"
                />
                <button
                  class="shrink-0 bg-orange-400 hover:bg-orange-500 text-white rounded-2xl px-4 py-3 text-sm font-black transition-colors"
                  @click="addManual"
                >+ 新增</button>
              </div>

              <!-- Food search panel (collapsible) -->
              <div v-if="showSearch" class="border border-orange-100 rounded-2xl p-3 bg-orange-50/40">
                <FoodSearchPanel @select="addFromSearch" />
              </div>

              <!-- Photo estimation -->
              <button
                class="w-full flex items-center gap-3 border border-dashed border-gray-200 rounded-2xl px-4 py-3 text-left hover:bg-gray-50 transition-colors"
                @click="showPhoto = !showPhoto"
              >
                <span class="text-xl">📷</span>
                <div>
                  <p class="text-sm font-semibold text-gray-700">拍照估算熱量</p>
                  <p class="text-xs text-gray-400">支援信用卡參照物，自動辨識食材</p>
                </div>
                <span class="ml-auto text-gray-300 text-sm">›</span>
              </button>
              <PhotoUploader v-if="showPhoto" @analyzed="addFromPhoto" />
            </div>
          </div>

          <!-- Total box -->
          <div v-if="items.length > 0" class="bg-orange-50 border border-orange-100 rounded-2xl px-4 py-3">
            <p class="text-xs font-black text-orange-400 mb-1">這餐合計</p>
            <p class="text-2xl font-black text-orange-500">{{ Math.round(totalKcal) }} <span class="text-base">kcal</span></p>
            <p v-if="hasMacros" class="text-xs text-orange-300 mt-1">
              蛋白質≈{{ Math.round(totalProtein) }}g
              碳水≈{{ Math.round(totalCarbs) }}g
              脂肪≈{{ Math.round(totalFat) }}g
              <span class="opacity-70">（估算）</span>
            </p>
          </div>

          <!-- Notes -->
          <div>
            <p class="text-sm font-black text-gray-700 mb-1">備註 <span class="font-normal text-gray-400 text-xs">（選填）</span></p>
            <textarea
              v-model="note"
              class="w-full rounded-2xl border border-gray-200 px-4 py-3 text-sm outline-none focus:border-orange-300 resize-none"
              rows="2"
              placeholder="例：少油少鹽、外帶回家吃..."
            />
          </div>
        </div>

        <!-- Footer -->
        <div class="flex gap-3 px-5 py-4 border-t border-gray-100 shrink-0">
          <button class="flex-1 py-3 rounded-2xl border border-gray-200 text-sm font-bold text-gray-500 hover:bg-gray-50 transition-colors" @click="$emit('cancel')">取消</button>
          <button class="flex-1 py-3 rounded-2xl bg-orange-400 hover:bg-orange-500 text-white text-sm font-black transition-colors" @click="submit">儲存</button>
        </div>
      </div>
    </div>
  </Teleport>
</template>
