import dayjs from 'dayjs'
import 'dayjs/locale/zh-tw'
import weekday from 'dayjs/plugin/weekday'
import isoWeek from 'dayjs/plugin/isoWeek'

dayjs.extend(weekday)
dayjs.extend(isoWeek)
dayjs.locale('zh-tw')

export { dayjs }

export const DAY_LABELS = ['週一', '週二', '週三', '週四', '週五', '週六', '週日']
export const MEAL_LABELS = ['早餐', '午餐', '晚餐']

export function getWeekStart(date?: string | Date): string {
  return dayjs(date).startOf('isoWeek').format('YYYY-MM-DD')
}

export function formatDate(date: string | Date, fmt = 'YYYY/MM/DD'): string {
  return dayjs(date).format(fmt)
}

export function today(): string {
  return dayjs().format('YYYY-MM-DD')
}

export function getDayOfWeek(weekStart: string, dayIndex: number): string {
  return dayjs(weekStart).add(dayIndex, 'day').format('MM/DD')
}
