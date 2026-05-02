<script setup lang="ts">
import type { CatProfile } from '@/types'
import CatSvg from './CatSvg.vue'
import type { CatBreed } from '@/constants/CatBreeds'
import { CAT_BREED_ICONS } from '@/constants/CatBreeds'

const props = defineProps<{ cat: CatProfile }>()
defineEmits<{ edit: [] }>()
</script>

<template>
  <div class="card text-center space-y-3">
    <div class="flex justify-center">
      <CatSvg :breed="(cat.breed as CatBreed)" :size="120" :animated="true" />
    </div>
    <div>
      <h2 class="text-xl font-bold text-gray-800">{{ cat.name }}</h2>
      <p class="text-gray-500 text-sm flex items-center justify-center gap-1">
        <span>{{ CAT_BREED_ICONS[(cat.breed as CatBreed)] }}</span>
        {{ cat.breed }}
      </p>
    </div>
    <div v-if="cat.bowlWidthMm" class="text-xs text-gray-400">
      碗口寬度 {{ cat.bowlWidthMm }} mm
    </div>
    <button class="btn-secondary w-full" @click="$emit('edit')">編輯貓咪資料</button>
  </div>
</template>
