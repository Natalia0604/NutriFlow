<script setup lang="ts">
import { CAT_BREEDS } from '@/constants/CatBreeds'
import type { CatBreed } from '@/constants/CatBreeds'

const props = defineProps<{ modelValue: string }>()
const emit = defineEmits<{ 'update:modelValue': [value: CatBreed] }>()
</script>

<template>
  <div class="grid grid-cols-3 gap-2.5">
    <button
      v-for="breed in CAT_BREEDS"
      :key="breed.value"
      type="button"
      class="breed-card flex flex-col items-center gap-1.5 p-2 rounded-2xl border-2 transition-all duration-200 bg-white cursor-pointer"
      :class="modelValue === breed.value
        ? 'border-orange-400 bg-orange-50 shadow-md scale-105'
        : 'border-gray-200 hover:border-orange-200 hover:scale-105 hover:shadow-sm'"
      @click="emit('update:modelValue', breed.value)"
    >
      <!-- SVG cat illustration -->
      <svg class="w-16 h-16" viewBox="0 0 120 120" v-html="breed.svg" />
      <span class="text-xs font-bold text-center leading-tight"
        :class="modelValue === breed.value ? 'text-orange-600' : 'text-gray-700'">
        {{ breed.label }}
      </span>
      <span class="text-[10px] text-gray-400 font-medium">{{ breed.tag }}</span>
    </button>
  </div>
</template>
