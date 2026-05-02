<script setup lang="ts">
import CatMascot from './CatMascot.vue'
import { computed } from 'vue'

const props = defineProps<{ advice: string; progress?: number }>()

const mood = computed(() => {
  if (!props.progress) return 'normal' as const
  if (props.progress < 70) return 'sad' as const
  if (props.progress > 120) return 'sad' as const
  if (props.progress >= 90 && props.progress <= 110) return 'happy' as const
  return 'normal' as const
})
</script>

<template>
  <div class="card flex items-center gap-4 bg-gradient-to-r from-orange-50 to-amber-50">
    <CatMascot :mood="mood" size="md" />
    <div class="flex-1">
      <p class="text-sm font-medium text-gray-700 leading-relaxed">{{ advice }}</p>
    </div>
  </div>
</template>
