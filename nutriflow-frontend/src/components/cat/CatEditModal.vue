<script setup lang="ts">
import { ref } from 'vue'
import type { CatProfile } from '@/types'
import CatBreedPicker from './CatBreedPicker.vue'
import type { CatBreed } from '@/constants/CatBreeds'

const props = defineProps<{ cat: CatProfile }>()
const emit = defineEmits<{ save: [Partial<CatProfile>]; cancel: [] }>()

const breed = ref<CatBreed>((props.cat.breed as CatBreed) ?? 'orange')
const name = ref(props.cat.name ?? '')
</script>

<template>
  <Teleport to="body">
    <div class="fixed inset-0 bg-black/40 z-50 flex items-end justify-center p-4" @click.self="$emit('cancel')">
      <div class="bg-white rounded-2xl w-full max-w-sm p-6 space-y-4">
        <h3 class="font-bold text-gray-800 text-lg">編輯貓咪資料</h3>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">貓咪名字</label>
          <input v-model="name" class="input" placeholder="請輸入名字" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">選擇品種</label>
          <CatBreedPicker v-model="breed" />
        </div>

        <div class="flex gap-3">
          <button class="btn-secondary flex-1" @click="$emit('cancel')">取消</button>
          <button class="btn-primary flex-1" @click="$emit('save', { name, breed })">儲存</button>
        </div>
      </div>
    </div>
  </Teleport>
</template>
