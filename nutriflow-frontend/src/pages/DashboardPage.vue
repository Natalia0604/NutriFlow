<script setup lang="ts">
import { ref, computed } from 'vue'
import { today } from '@/utils/date'
import { useDailyNutrition } from '@/composables/useNutrition'
import { useDailyMeals } from '@/composables/useMeal'
import { useProfile } from '@/composables/useProfile'
import { useCatProfile } from '@/composables/useCatProfile'
import { useRouter } from 'vue-router'
import { useToast } from '@/composables/useToast'
import CatHeroCard from '@/components/cat/CatHeroCard.vue'
import LoadingSpinner from '@/components/base/LoadingSpinner.vue'
import type { CatBreed } from '@/constants/CatBreeds'

const toast = useToast()

const date = ref(today())
const router = useRouter()

const { data: profile } = useProfile()
const { data: cat } = useCatProfile()
const { data: nutrition, isLoading: loadingNutrition } = useDailyNutrition(date)
const { data: logs, deleteLog } = useDailyMeals(date)

async function handleDeleteSlot(logIds: number[]) {
  for (const id of logIds) {
    await deleteLog.mutateAsync(id)
  }
  toast.success('已刪除')
}

const MEAL_ICONS = ['☀️', '🌤️', '🌙']
const MEAL_NAMES = ['早餐', '午餐', '晚餐']

const mealSlots = computed(() => {
  return [0, 1, 2].map(idx => {
    const matching = (logs.value ?? []).filter(l => l.mealIndex === idx)
    const totalKcal = matching.reduce((s, l) => s + (l.totalKcal ?? 0), 0)
    return { idx, name: MEAL_NAMES[idx], icon: MEAL_ICONS[idx], logs: matching, totalKcal }
  })
})

// Tips derived from nutrition
const tips = computed(() => {
  if (!nutrition.value) return []
  const n = nutrition.value
  const list: { type: 'warn' | 'good'; text: string }[] = []
  if (n.totalProtein < 50) list.push({ type: 'warn', text: `💪 今日蛋白質僅 ${Math.round(n.totalProtein)}g，建議補充雞肉或豆腐` })
  if (n.totalFat > 70) list.push({ type: 'warn', text: `⚠️ 今日油脂偏高，建議晚餐選清蒸或水煮` })
  if (n.totalSodium > 2000) list.push({ type: 'warn', text: `🧂 鈉攝取偏高，多喝水幫助代謝` })
  if (n.totalCarbs >= 100 && n.totalCarbs <= 250) list.push({ type: 'good', text: '✅ 碳水攝取正常，繼續保持！' })
  if (n.kcalProgress >= 80 && n.kcalProgress <= 110) list.push({ type: 'good', text: '✅ 今日熱量控制得很好！' })
  if (list.length === 0) list.push({ type: 'good', text: '🐾 記錄越多，建議越準確！繼續加油～' })
  return list
})


</script>

<template>
  <div class="pb-6">

    <!-- Top Bar -->
    <div class="sticky top-0 z-30 flex items-center justify-between px-5 py-4"
      style="background:rgba(255,248,240,.85);backdrop-filter:blur(12px);border-bottom:1px solid rgba(237,224,216,.5)">
      <div class="flex items-center gap-1.5 text-xl font-black text-orange-700">
        <span>🐱</span> NutriFlow
      </div>
      <div class="flex items-center gap-2">
        <input v-model="date" type="date"
          class="text-[11px] border-0 bg-orange-100/60 text-orange-700 font-bold rounded-lg px-2 py-1" />
      </div>
    </div>

    <!-- Cat Hero Card -->
    <LoadingSpinner v-if="loadingNutrition" class="mt-8" size="md" />
    <CatHeroCard
      v-else
      :cat-name="cat?.name"
      :cat-breed="(cat?.breed as CatBreed)"
      :user-name="profile?.email?.split('@')[0]"
      :nutrition="nutrition"
    />

    <!-- Today's Meals -->
    <div class="px-5 pt-5 pb-1 flex items-center">
      <span class="text-base font-black text-gray-800">🍽️ 今日餐點</span>
      <span class="ml-auto text-[11px] font-black text-orange-600 bg-orange-100 rounded-full px-3 py-0.5">
        {{ new Date(date).toLocaleDateString('zh-TW', { weekday: 'short' }) }}
      </span>
    </div>

    <!-- Meal scroll cards -->
    <div class="flex gap-3 px-4 pb-2 overflow-x-auto scrollbar-none">
      <div
        v-for="slot in mealSlots"
        :key="slot.idx"
        class="flex-shrink-0 w-[130px] bg-white rounded-2xl p-3.5 border-2 transition-all hover:-translate-y-0.5 relative overflow-hidden"
        :class="slot.logs.length > 0 ? 'border-emerald-300' : 'border-gray-100 cursor-pointer'"
        @click="slot.logs.length === 0 && router.push({ path: '/log', query: { mealIndex: slot.idx, date } })"
      >
        <!-- Top accent for logged meals -->
        <div v-if="slot.logs.length > 0" class="absolute top-0 inset-x-0 h-[3px] bg-gradient-to-r from-emerald-400 to-emerald-300" />

        <!-- Delete button (logged meals only) -->
        <button
          v-if="slot.logs.length > 0"
          class="absolute top-2 right-2 w-5 h-5 flex items-center justify-center text-gray-300 hover:text-red-400 transition-colors text-xs rounded-full hover:bg-red-50 z-10"
          @click.stop="handleDeleteSlot(slot.logs.map(l => l.id))"
          title="刪除這餐"
        >✕</button>

        <div class="text-[11px] font-bold px-2 py-0.5 rounded-full mb-2 w-fit"
          :class="slot.logs.length > 0 ? 'bg-emerald-100 text-emerald-700' : 'bg-orange-50 text-orange-400'">
          {{ slot.logs.length > 0 ? '🍳 已記錄' : '🥡 未記錄' }}
        </div>
        <div class="text-[26px] mb-1">{{ slot.icon }}</div>
        <div class="text-[13px] font-black text-gray-800">{{ slot.name }}</div>
        <template v-if="slot.logs.length > 0">
          <div class="text-[15px] font-black text-orange-600 mt-1">{{ Math.round(slot.totalKcal) }} <span class="text-[10px] font-normal text-gray-400">kcal</span></div>
          <div class="text-[10px] text-gray-400 mt-0.5 truncate">
            {{ slot.logs.flatMap(l => l.items.map(i => i.foodName)).slice(0, 2).join(' + ') }}
          </div>
          <!-- Add more button -->
          <button
            class="mt-2 w-full text-[10px] text-emerald-500 font-bold border border-emerald-200 rounded-lg py-0.5 hover:bg-emerald-50 transition-colors"
            @click.stop="router.push({ path: '/log', query: { mealIndex: slot.idx, date } })"
          >+ 再新增</button>
        </template>
        <template v-else>
          <div class="w-9 h-9 rounded-full bg-orange-50 border-2 border-dashed border-orange-200 flex items-center justify-center text-xl text-orange-400 mx-auto mt-2">+</div>
        </template>
      </div>
    </div>

    <!-- 小橘的提醒 (Nutrition Tips) -->
    <div class="px-5 pt-4 pb-1">
      <span class="text-base font-black text-gray-800">💡 小橘的提醒</span>
    </div>
    <div v-if="loadingNutrition" class="px-4">
      <LoadingSpinner size="sm" />
    </div>
    <div v-else class="px-4 space-y-2">
      <div
        v-for="(tip, i) in tips"
        :key="i"
        class="flex items-center gap-2 rounded-xl px-3 py-2.5 text-[13px] font-bold border-2"
        :class="tip.type === 'warn'
          ? 'bg-pink-50 border-pink-200 text-gray-800'
          : 'bg-emerald-50 border-emerald-200 text-gray-800'"
      >
        {{ tip.text }}
      </div>
    </div>

    <!-- Quick actions -->
    <div class="px-4 mt-5 flex gap-3">
      <RouterLink to="/log" class="flex-1 btn-primary flex items-center justify-center gap-2 text-sm py-3">
        🐾 記錄餐點
      </RouterLink>
      <RouterLink to="/menu" class="flex-1 btn-secondary flex items-center justify-center gap-2 text-sm py-3">
        📅 週菜單
      </RouterLink>
    </div>

  </div>
</template>

<style scoped>
.scrollbar-none::-webkit-scrollbar { display: none; }
.scrollbar-none { scrollbar-width: none; }
</style>
