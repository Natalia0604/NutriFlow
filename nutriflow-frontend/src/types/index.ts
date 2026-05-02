export interface ApiResponse<T> {
  success: boolean
  data?: T
  error?: { code: string; message: string }
}

export interface PageResponse<T> {
  content: T[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  last: boolean
}

export interface AuthResponse {
  accessToken: string
  tokenType: string
  userId: number
  email: string
  isSetupComplete: boolean
}

export interface UserProfile {
  id: number
  email: string
  gender: string
  age: number
  weight: number
  height: number
  activityLevel: string
  hasWeightGoal: boolean
  targetWeight?: number
  goalWeeks?: number
  isSetupComplete: boolean
  tdee: number
  targetCalories: number
}

export type CatBreed = 'orange' | 'calico' | 'black' | 'white' | 'tabby' | 'ragdoll'

export interface CatProfile {
  id: number
  breed: CatBreed | string
  name: string
  knucklePxRatio?: number
  bowlWidthMm?: number
}

export interface FoodPreference {
  id: number
  foodName: string
  category: string
  isCustom: boolean
  isAllergy: boolean
}

export interface MealItem {
  id?: number
  foodName: string
  kcal: number
  protein?: number
  carbs?: number
  fat?: number
  sodium?: number
  dataSource?: string
  confidence?: number
  sortOrder?: number
}

export interface MealCell {
  id: number
  dayIndex: number
  mealIndex: number
  mealType: string
  logged: boolean
  logTime?: string
  note?: string
  source: string
  items: MealItem[]
  totalKcal: number
}

export interface WeekMenu {
  id: number
  weekStart: string
  aiComment?: string
  generatedBy: string
  cells: MealCell[]
}

export interface MealLogItem {
  id?: number
  foodName: string
  kcal: number
  kcalRangeMin?: number
  kcalRangeMax?: number
  protein?: number
  carbs?: number
  fat?: number
  sodium?: number
  dataSource?: string
  confidence?: number
  photoUsed?: boolean
  referenceType?: string
  sortOrder?: number
}

export interface MealLog {
  id: number
  mealDate: string
  mealIndex: number
  mealType?: string
  mealCellId?: number
  storeName?: string
  note?: string
  loggedAt: string
  items: MealLogItem[]
  totalKcal: number
}

export interface WeightRecord {
  id: number
  weight: number
  recordDate: string
  note?: string
}

export interface CustomFood {
  id: number
  foodName: string
  sizeLabel?: string
  kcal: number
  protein?: number
  carbs?: number
  fat?: number
  sodium?: number
  note?: string
  createdAt?: string
}

export interface SaveCustomFoodRequest {
  foodName: string
  sizeLabel?: string
  kcal: number
  protein?: number
  carbs?: number
  fat?: number
  sodium?: number
  note?: string
}

export interface FoodSearchResult {
  id?: number
  storeName?: string
  itemName: string
  sizeLabel?: string
  kcal: number
  protein?: number
  carbs?: number
  fat?: number
  sodium?: number
  sugar?: number
  dataUrl?: string
  source: string
}

export interface DailyNutrition {
  date: string
  totalKcal: number
  totalProtein: number
  totalCarbs: number
  totalFat: number
  totalSodium: number
  targetKcal: number
  kcalProgress: number
  advice: string
}

export interface WeeklyNutrition {
  weekStart: string
  weekEnd: string
  days: DailyNutrition[]
  avgKcal: number
  avgProtein: number
  avgCarbs: number
  avgFat: number
  targetKcal: number
}
