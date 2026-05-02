import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { WeekMenu } from '@/types'
import dayjs from 'dayjs'

export const useMenuStore = defineStore('menu', () => {
  const currentMenu = ref<WeekMenu | null>(null)
  const currentWeekStart = ref<string>(dayjs().startOf('week').format('YYYY-MM-DD'))

  function setMenu(menu: WeekMenu) { currentMenu.value = menu }
  function setWeekStart(date: string) { currentWeekStart.value = date }

  return { currentMenu, currentWeekStart, setMenu, setWeekStart }
})
