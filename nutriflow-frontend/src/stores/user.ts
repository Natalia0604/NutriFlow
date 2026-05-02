import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { UserProfile, CatProfile } from '@/types'

export const useUserStore = defineStore('user', () => {
  const profile = ref<UserProfile | null>(null)
  const catProfile = ref<CatProfile | null>(null)

  function setProfile(p: UserProfile) { profile.value = p }
  function setCatProfile(c: CatProfile) { catProfile.value = c }
  function clear() { profile.value = null; catProfile.value = null }

  return { profile, catProfile, setProfile, setCatProfile, clear }
})
