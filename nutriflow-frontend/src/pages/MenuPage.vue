<script setup lang="ts">
import { ref, computed } from 'vue'
import { dayjs, getWeekStart, today } from '@/utils/date'
import { useMenu } from '@/composables/useMenu'
import { useCatProfile } from '@/composables/useCatProfile'
import type { MealCell } from '@/types'
import WeekMenuGrid from '@/components/menu/WeekMenuGrid.vue'
import GenerateMenuModal from '@/components/menu/GenerateMenuModal.vue'
import EditCellModal from '@/components/menu/EditCellModal.vue'
import { useToast } from '@/composables/useToast'

const weekStart = ref(getWeekStart())
const toast = useToast()
const todayStr = today()
const showGenerateModal = ref(false)
const editingCell = ref<any | null>(null)
const pendingMenuId = ref<number | null>(null)

const { data: menu, isLoading, generate, createBlank, updateCell } = useMenu(weekStart)
const { data: cat } = useCatProfile()

// ── Week label (e.g. "4月第3週") ──────────────────────────────────────
const weekLabel = computed(() => {
  const d = dayjs(weekStart.value)
  const month = d.month() + 1
  const weekNum = d.isoWeek() - d.startOf('month').isoWeek() + 1
  const nums = ['一', '二', '三', '四', '五']
  return `${month}月第${nums[Math.max(0, weekNum - 1)] ?? weekNum}週`
})

function prevWeek() { weekStart.value = dayjs(weekStart.value).subtract(7, 'day').format('YYYY-MM-DD') }
function nextWeek() { weekStart.value = dayjs(weekStart.value).add(7, 'day').format('YYYY-MM-DD') }

// ── Stats ─────────────────────────────────────────────────────────────
const cells = computed(() => menu.value?.cells ?? [])

const stats = computed(() => {
  const total   = 21
  const logged  = cells.value.filter(c => c.logged).length
  const filled  = cells.value.filter(c => c.items.length > 0).length
  const unfilled = total - logged
  const rate    = Math.round(logged / total * 100)
  const loggedCells = cells.value.filter(c => c.logged)
  const avgKcal = loggedCells.length > 0
    ? Math.round(loggedCells.reduce((s, c) => s + c.totalKcal, 0) / loggedCells.length)
    : 0
  return { total, logged, filled, unfilled, rate, avgKcal }
})

const catName = computed(() => cat.value?.name ?? '小橘')

const catMsg = computed(() => {
  const r = stats.value.rate
  if (r === 0)  return `本週還沒開始記錄，快來規劃吧！`
  if (r < 40)   return `還有 ${stats.value.unfilled} 格餐點沒記錄，${catName.value}在等你填喔 🍱`
  if (r < 80)   return `繼續加油！本週完成度 ${r}%，${catName.value}幫你加油 💪`
  if (r < 100)  return `快完成囉！再記錄 ${stats.value.unfilled} 餐就滿分了 🎉`
  return `完美完成！${catName.value}幫你打 100 分 🏆`
})

// ── Cell click ────────────────────────────────────────────────────────
async function onCellClick(payload: { dayIndex: number; mealIndex: number; cell: MealCell | null }) {
  // Ensure a menu exists first
  if (!menu.value) {
    try {
      const newMenu = await createBlank.mutateAsync({ weekStart: weekStart.value })
      pendingMenuId.value = newMenu.id
    } catch {
      toast.error('建立菜單失敗，請稍後再試')
      return
    }
  }
  editingCell.value = payload.cell ?? {
    id: 0,
    dayIndex:  payload.dayIndex,
    mealIndex: payload.mealIndex,
    mealType:  '',
    logged:    false,
    source:    'manual',
    items:     [],
    totalKcal: 0,
  }
}

async function onGenerate(params: any) {
  try {
    await generate.mutateAsync(params)
    showGenerateModal.value = false
    toast.success('菜單生成成功！')
  } catch {
    toast.error('菜單生成失敗，請稍後再試')
  }
}

async function onEditSave(data: any) {
  const menuId = menu.value?.id ?? pendingMenuId.value
  if (!menuId) return
  try {
    await updateCell.mutateAsync({ menuId, data })
    editingCell.value = null
    toast.success('已更新')
  } catch {
    toast.error('更新失敗')
  }
}
</script>

<template>
  <div class="min-h-screen bg-[#fdf6f0]">

    <!-- Header -->
    <div class="sticky top-0 z-10 bg-[#fdf6f0]/90 backdrop-blur-sm px-4 pt-4 pb-2 flex items-center justify-between">
      <div class="flex items-center gap-2">
        <span class="text-2xl">🐱</span>
        <span class="font-black text-gray-800 text-lg">NutriFlow</span>
      </div>
      <div v-if="stats.logged > 0" class="flex items-center gap-1 bg-orange-400 text-white text-xs font-black px-3 py-1.5 rounded-full shadow-sm">
        🔥 {{ stats.logged }}餐已記錄
      </div>
    </div>

    <div class="px-4 pb-6 space-y-4">

      <!-- Week navigation + AI button -->
      <div class="flex items-center justify-between">
        <div class="flex items-center gap-2">
          <h2 class="font-black text-gray-800 text-lg">本週菜單</h2>
          <div class="flex items-center gap-1 bg-orange-100 text-orange-500 text-xs font-bold px-2.5 py-1 rounded-full">
            <button class="hover:text-orange-700" @click="prevWeek">‹</button>
            <span>{{ weekLabel }}</span>
            <button class="hover:text-orange-700" @click="nextWeek">›</button>
          </div>
        </div>
        <button
          class="flex items-center gap-1 bg-orange-400 hover:bg-orange-500 text-white text-xs font-black px-3 py-2 rounded-xl shadow-sm transition-colors"
          @click="showGenerateModal = true"
        >✨ AI 生成</button>
      </div>

      <!-- Loading -->
      <div v-if="isLoading || generate.isPending.value || createBlank.isPending.value" class="flex flex-col items-center py-8 gap-3">
        <div class="w-8 h-8 border-4 border-orange-300 border-t-orange-500 rounded-full animate-spin" />
        <p class="text-xs text-gray-400">載入中...</p>
      </div>

      <template v-else>
        <!-- Legend -->
        <div class="flex items-center gap-3 text-[11px] text-gray-500">
          <span class="flex items-center gap-1"><span class="inline-block w-3 h-3 rounded-sm bg-emerald-100 border border-emerald-400"></span>自炊</span>
          <span class="flex items-center gap-1"><span class="inline-block w-3 h-3 rounded-sm bg-white border border-teal-200"></span>外食</span>
          <span class="flex items-center gap-1"><span class="inline-block w-3 h-3 rounded-sm bg-emerald-400"></span>已記錄</span>
          <span class="flex items-center gap-1"><span class="inline-block w-3 h-3 rounded-sm bg-orange-100 border-2 border-orange-400"></span>今天</span>
        </div>

        <!-- Grid -->
        <WeekMenuGrid
          :cells="cells"
          :week-start="weekStart"
          :today-str="todayStr"
          @cell-click="onCellClick"
        />

        <!-- Stats row -->
        <div class="grid grid-cols-3 gap-3">
          <div class="bg-white rounded-2xl p-3 text-center shadow-sm border border-gray-100">
            <p class="text-xl font-black text-orange-500">{{ stats.rate }}%</p>
            <p class="text-[11px] text-gray-400 mt-0.5">完成率</p>
          </div>
          <div class="bg-white rounded-2xl p-3 text-center shadow-sm border border-gray-100">
            <p class="text-xl font-black text-gray-700">{{ stats.logged }}</p>
            <p class="text-[11px] text-gray-400 mt-0.5">已記錄餐</p>
          </div>
          <div class="bg-white rounded-2xl p-3 text-center shadow-sm border border-gray-100">
            <p class="text-xl font-black text-gray-700">{{ stats.avgKcal }}</p>
            <p class="text-[11px] text-gray-400 mt-0.5">平均kcal/餐</p>
          </div>
        </div>

        <!-- Cat encouragement -->
        <div class="bg-orange-50 rounded-2xl p-4 flex items-center gap-3 border border-orange-100">
          <span class="text-3xl shrink-0">🐱</span>
          <div>
            <p class="text-sm font-black text-gray-700">本週完成度 {{ stats.rate }}%</p>
            <p class="text-xs text-gray-500 mt-0.5">{{ catMsg }}</p>
          </div>
        </div>

        <!-- Empty state hint (no menu yet) -->
        <div v-if="!menu" class="bg-white rounded-2xl p-5 text-center border border-dashed border-orange-200">
          <p class="text-2xl mb-2">📅</p>
          <p class="text-sm font-bold text-gray-600 mb-1">本週尚無菜單</p>
          <p class="text-xs text-gray-400 mb-3">點選格子手動填入，或讓 AI 幫你規劃</p>
          <button class="btn-primary text-sm px-4 py-2" @click="showGenerateModal = true">✨ AI 生成菜單</button>
        </div>
      </template>
    </div>

    <!-- Modals -->
    <GenerateMenuModal
      v-if="showGenerateModal"
      :week-start="weekStart"
      :loading="generate.isPending.value"
      @generate="onGenerate"
      @cancel="showGenerateModal = false"
    />

    <EditCellModal
      v-if="editingCell"
      :cell="editingCell"
      @save="onEditSave"
      @cancel="editingCell = null"
    />
  </div>
</template>
