<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  mood?: 'happy' | 'normal' | 'sad' | 'excited'
  size?: 'sm' | 'md' | 'lg'
  animated?: boolean
}>()

const emoji = computed(() => {
  switch (props.mood) {
    case 'happy': return '😸'
    case 'sad': return '😿'
    case 'excited': return '🙀'
    default: return '😺'
  }
})

const sizeClass = computed(() => ({
  'text-4xl': props.size === 'sm',
  'text-6xl': props.size === 'md' || !props.size,
  'text-8xl': props.size === 'lg'
}))
</script>

<template>
  <div
    class="select-none"
    :class="[sizeClass, { 'animate-bounce': animated }]"
  >
    {{ emoji }}
  </div>
</template>
