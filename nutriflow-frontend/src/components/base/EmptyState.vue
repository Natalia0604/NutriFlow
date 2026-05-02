<script setup lang="ts">
defineProps<{ icon?: string; title: string; description?: string }>()
</script>

<template>
  <div class="flex flex-col items-center justify-center py-16 text-center px-4">
    <div v-if="icon" class="text-5xl mb-4">{{ icon }}</div>
    <h3 class="text-gray-600 font-semibold text-lg mb-1">{{ title }}</h3>
    <p v-if="description" class="text-gray-400 text-sm">{{ description }}</p>
    <slot />
  </div>
</template>
