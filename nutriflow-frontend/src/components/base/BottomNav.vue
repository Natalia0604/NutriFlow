<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const leftItems = [
  { name: 'Dashboard', to: '/dashboard', icon: '🏠', label: '首頁' },
  { name: 'Menu',      to: '/menu',      icon: '📅', label: '週菜單' },
]
const rightItems = [
  { name: 'Nutrition', to: '/nutrition', icon: '📊', label: '統計' },
  { name: 'Profile',   to: '/profile',   icon: '👤', label: '我的' },
]
</script>

<template>
  <nav class="fixed bottom-0 left-1/2 -translate-x-1/2 z-40 w-full max-w-[420px]"
    style="background:rgba(255,248,240,.92);backdrop-filter:blur(16px);border-top:2px solid #EDE0D8;">
    <div class="flex items-center justify-around pb-safe pt-2.5 pb-2.5 relative">

      <!-- Left items -->
      <RouterLink
        v-for="item in leftItems"
        :key="item.name"
        :to="item.to"
        class="flex flex-col items-center gap-0.5 px-4 py-1 rounded-xl -webkit-tap-highlight-transparent"
        :class="route.name === item.name ? 'text-orange-500' : 'text-gray-400'"
      >
        <span class="text-[22px] leading-none"
          :class="route.name === item.name ? 'drop-shadow-[0_0_4px_rgba(255,140,66,.5)]' : ''">
          {{ item.icon }}
        </span>
        <span class="text-[10px] font-black tracking-wide"
          :class="route.name === item.name ? 'text-orange-600' : 'text-gray-400'">
          {{ item.label }}
        </span>
      </RouterLink>

      <!-- FAB -->
      <div class="relative flex-shrink-0" style="margin-top:-18px">
        <div class="fab-ring absolute inset-[-4px] rounded-full border-[3px] border-orange-300/40" />
        <button
          class="w-[58px] h-[58px] rounded-full flex items-center justify-center text-[26px] text-white border-none cursor-pointer transition-transform active:scale-95"
          style="background:linear-gradient(135deg,#FF8C42,#E07030);box-shadow:0 6px 20px rgba(255,140,66,.45);"
          @click="router.push('/log')"
        >🐾</button>
      </div>

      <!-- Right items -->
      <RouterLink
        v-for="item in rightItems"
        :key="item.name"
        :to="item.to"
        class="flex flex-col items-center gap-0.5 px-4 py-1 rounded-xl"
        :class="route.name === item.name ? 'text-orange-500' : 'text-gray-400'"
      >
        <span class="text-[22px] leading-none"
          :class="route.name === item.name ? 'drop-shadow-[0_0_4px_rgba(255,140,66,.5)]' : ''">
          {{ item.icon }}
        </span>
        <span class="text-[10px] font-black tracking-wide"
          :class="route.name === item.name ? 'text-orange-600' : 'text-gray-400'">
          {{ item.label }}
        </span>
      </RouterLink>

    </div>
  </nav>
</template>

<style scoped>
.fab-ring {
  animation: fab-ring 2s ease-in-out infinite;
}
@keyframes fab-ring {
  0%,100% { transform: scale(1);   opacity: 1; }
  50%      { transform: scale(1.2); opacity: 0; }
}
</style>
