export function kcalColor(progress: number): string {
  if (progress < 70) return 'text-blue-500'
  if (progress <= 110) return 'text-green-500'
  if (progress <= 130) return 'text-yellow-500'
  return 'text-red-500'
}

export function kcalBarColor(progress: number): string {
  if (progress <= 110) return 'bg-primary-500'
  if (progress <= 130) return 'bg-yellow-400'
  return 'bg-red-400'
}

export function formatKcal(value: number): string {
  return `${Math.round(value)} kcal`
}

export function macroPercent(macro: number, kcal: number, factor: number): number {
  if (!kcal) return 0
  return Math.round((macro * factor / kcal) * 100)
}
