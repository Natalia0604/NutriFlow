<script setup lang="ts">
defineProps<{ title: string; back?: boolean }>()
</script>

<template>
  <header class="sticky top-0 bg-white border-b border-orange-100 z-30 px-4 py-3 flex items-center gap-3">
    <RouterLink v-if="back" to=".." class="text-gray-400 hover:text-gray-600 text-xl">←</RouterLink>
    <h1 class="text-lg font-bold text-gray-800">{{ title }}</h1>
    <slot name="right" />
  </header>
</template>
