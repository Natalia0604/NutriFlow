<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { userService } from '@/services/user'
import { useAuthStore } from '@/stores/auth'
import { useToast } from '@/composables/useToast'
import CatBreedPicker from '@/components/cat/CatBreedPicker.vue'
import CatSvg from '@/components/cat/CatSvg.vue'
import type { CatBreed } from '@/constants/CatBreeds'
import { CAT_BREED_ICONS, CAT_BREED_NAMES } from '@/constants/CatBreeds'

const router = useRouter()
const auth = useAuthStore()
const toast = useToast()

const step = ref(0)
const loading = ref(false)

// Step 0 — Cat breed
const catBreed = ref<CatBreed>('orange')
const catName = ref('')

// Step 1 — Basic info
const gender = ref<'female' | 'male'>('female')
const age = ref(28)
const weight = ref(60)
const height = ref(165)
const activity = ref(1.55)

const ACTIVITY_OPTIONS = [
  { value: 1.2,   label: '🪑 久坐（幾乎不運動）' },
  { value: 1.375, label: '🚶 輕度活動（每週 1–3 天）' },
  { value: 1.55,  label: '🏃 中度活動（每週 3–5 天）' },
  { value: 1.725, label: '🏋️ 高度活動（每週 6–7 天）' },
  { value: 1.9,   label: '⚡ 非常活躍（體力工作）' },
]

const tdee = computed(() => {
  const bmr = gender.value === 'female'
    ? 10 * weight.value + 6.25 * height.value - 5 * age.value - 161
    : 10 * weight.value + 6.25 * height.value - 5 * age.value + 5
  return Math.round(bmr * activity.value)
})

// Step 2 — Food preferences
const FOOD_CATS = [
  { key: '蛋白質', icon: '💪', foods: ['雞肉', '豬肉', '牛肉', '海鮮', '蛋', '豆腐', '豆漿', '鮭魚', '鮪魚罐頭'] },
  { key: '主食',   icon: '🌾', foods: ['白飯', '糙米', '地瓜', '麵條', '烏龍麵', '吐司', '燕麥', '玉米', '義大利麵'] },
  { key: '蔬菜',   icon: '🥦', foods: ['花椰菜', '菠菜', '番茄', '紅蘿蔔', '小黃瓜', '高麗菜', '空心菜', '蘑菇'] },
  { key: '水果',   icon: '🍎', foods: ['香蕉', '蘋果', '芭樂', '葡萄', '藍莓', '奇異果', '草莓'] },
  { key: '其他',   icon: '✨', foods: ['堅果', '優格', '牛奶', '起司', '橄欖油', '黑芝麻'] },
]
const foodTab = ref('蛋白質')
const selectedFoods = ref<Set<string>>(new Set(['雞肉', '蛋', '豆腐', '海鮮', '白飯']))
const customFoodInput = ref('')
const customFoods = ref<string[]>([])

function toggleFood(food: string) {
  if (selectedFoods.value.has(food)) selectedFoods.value.delete(food)
  else selectedFoods.value.add(food)
}
function addCustomFood() {
  const val = customFoodInput.value.trim()
  if (!val) return
  customFoods.value.push(val)
  selectedFoods.value.add(val)
  customFoodInput.value = ''
}
function removeCustomFood(food: string) {
  customFoods.value = customFoods.value.filter(f => f !== food)
  selectedFoods.value.delete(food)
}

const selectedFoodList = computed(() => [...selectedFoods.value])

// Step dots
const TOTAL_STEPS = 3
const dots = computed(() =>
  Array.from({ length: TOTAL_STEPS }, (_, i) =>
    i < step.value ? 'done' : i === step.value ? 'active' : 'idle'
  )
)

async function finish() {
  loading.value = true
  try {
    // Save cat
    await userService.updateCat({
      breed: catBreed.value,
      name: catName.value || CAT_BREED_NAMES[catBreed.value]
    })
    // Save user profile
    await userService.updateProfile({
      age: age.value,
      weight: weight.value,
      height: height.value,
      gender: gender.value === 'female' ? 'FEMALE' : 'MALE',
      activityLevel: String(activity.value),
      isSetupComplete: true
    })
    // addPreference is idempotent on the backend (checks before insert), safe to call multiple times
    for (const food of selectedFoodList.value) {
      await userService.addPreference({ foodName: food, category: '偏好', isCustom: false })
    }
    auth.isSetupComplete = true
    router.push('/dashboard')
  } catch {
    toast.error('設定失敗，請稍後再試')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="min-h-screen pb-10" style="background:linear-gradient(160deg,#FFF8F0,#FFE4CC 60%,#FFF0E6)">

    <!-- Top bar -->
    <div class="flex items-center justify-between px-5 pt-5 pb-3">
      <div class="flex items-center gap-1.5 text-xl font-black text-orange-700">
        <span>🐱</span> NutriFlow
      </div>
      <span class="text-[13px] text-gray-400 font-bold">建立你的檔案</span>
    </div>

    <!-- Card -->
    <div class="mx-4 bg-white rounded-3xl shadow-md border-2 border-gray-100 p-6">

      <!-- Step dots -->
      <div class="flex gap-1.5 justify-center mb-6">
        <div
          v-for="(d, i) in dots"
          :key="i"
          class="h-2 rounded-full transition-all duration-400"
          :class="{
            'w-6 bg-orange-400': d === 'active',
            'w-2 bg-emerald-400': d === 'done',
            'w-2 bg-gray-200': d === 'idle',
          }"
        />
      </div>

      <!-- ── STEP 0: Choose cat ── -->
      <template v-if="step === 0">
        <h2 class="text-[22px] font-black text-gray-800 mb-1.5">🐱 選擇你的貓咪夥伴</h2>
        <p class="text-[14px] text-gray-500 font-semibold mb-5 leading-relaxed">
          牠會陪你記錄每一天的飲食，吃得健康牠就開心！
        </p>

        <CatBreedPicker v-model="catBreed" />

        <!-- Selected preview -->
        <div class="mt-4 bg-orange-50 rounded-2xl px-4 py-3 flex items-center gap-3 border-2 border-orange-100">
          <span class="text-2xl">{{ CAT_BREED_ICONS[catBreed] }}</span>
          <div>
            <p class="text-[13px] font-black text-orange-600">已選擇：{{ catBreed === 'orange' ? '橘貓小橘' : catBreed === 'calico' ? '三花小花' : catBreed === 'black' ? '黑貓小墨' : catBreed === 'white' ? '白貓小雪' : catBreed === 'tabby' ? '虎斑小虎' : '布偶小棉' }}</p>
            <p class="text-[11px] text-gray-400 font-semibold">牠會在你的首頁陪伴你 🐾</p>
          </div>
        </div>

        <!-- Cat name input (optional) -->
        <div class="mt-3">
          <label class="block text-[12px] font-black text-gray-500 mb-1.5">幫牠取個名字（選填）</label>
          <input
            v-model="catName"
            class="input text-sm"
            :placeholder="`預設名字：${CAT_BREED_NAMES[catBreed]}`"
          />
        </div>

        <button class="btn-primary w-full mt-6 py-4 text-base flex items-center justify-center gap-2" @click="step = 1">
          確定，繼續 <span>→</span>
        </button>
      </template>

      <!-- ── STEP 1: Basic info ── -->
      <template v-else-if="step === 1">
        <h2 class="text-[22px] font-black text-gray-800 mb-1.5">⚖️ 基本資料</h2>
        <p class="text-[14px] text-gray-500 font-semibold mb-5 leading-relaxed">
          讓小橘幫你計算每天需要多少熱量！
        </p>

        <!-- Gender -->
        <div class="text-[12px] font-black text-gray-500 mb-2">性別</div>
        <div class="flex gap-3 mb-5">
          <button
            class="flex-1 border-2 rounded-xl py-3 text-center text-2xl cursor-pointer transition-all"
            :class="gender === 'female' ? 'border-orange-400 bg-orange-50' : 'border-gray-200 bg-white'"
            @click="gender = 'female'"
          >
            ♀️<div class="text-[12px] font-black mt-1" :class="gender === 'female' ? 'text-orange-600' : 'text-gray-400'">女性</div>
          </button>
          <button
            class="flex-1 border-2 rounded-xl py-3 text-center text-2xl cursor-pointer transition-all"
            :class="gender === 'male' ? 'border-orange-400 bg-orange-50' : 'border-gray-200 bg-white'"
            @click="gender = 'male'"
          >
            ♂️<div class="text-[12px] font-black mt-1" :class="gender === 'male' ? 'text-orange-600' : 'text-gray-400'">男性</div>
          </button>
        </div>

        <div class="grid grid-cols-2 gap-3 mb-3">
          <div>
            <label class="block text-[12px] font-black text-gray-500 mb-1.5">年齡</label>
            <input v-model.number="age" type="number" min="15" max="90" class="input text-sm" placeholder="歲" />
          </div>
          <div>
            <label class="block text-[12px] font-black text-gray-500 mb-1.5">體重 (kg)</label>
            <input v-model.number="weight" type="number" min="30" max="200" class="input text-sm" placeholder="kg" />
          </div>
        </div>
        <div class="mb-3">
          <label class="block text-[12px] font-black text-gray-500 mb-1.5">身高 (cm)</label>
          <input v-model.number="height" type="number" min="130" max="220" class="input text-sm" placeholder="cm" />
        </div>
        <div class="mb-5">
          <label class="block text-[12px] font-black text-gray-500 mb-1.5">活動量</label>
          <select v-model.number="activity" class="input text-sm">
            <option v-for="opt in ACTIVITY_OPTIONS" :key="opt.value" :value="opt.value">{{ opt.label }}</option>
          </select>
        </div>

        <!-- TDEE result -->
        <div class="rounded-2xl p-4 text-center border-2 border-orange-100" style="background:linear-gradient(135deg,#FFE4CC,#FFCDA8)">
          <p class="text-[13px] text-gray-500 font-bold mb-1">你的 TDEE（每日消耗熱量）</p>
          <p class="text-[40px] font-black text-orange-700 leading-none">{{ tdee.toLocaleString() }}</p>
          <p class="text-[14px] text-gray-500 font-semibold mt-1">kcal / 天</p>
        </div>

        <div class="flex gap-3 mt-6">
          <button class="btn-secondary flex-1 py-3" @click="step = 0">← 上一步</button>
          <button class="btn-primary flex-1 py-3 flex items-center justify-center gap-2" @click="step = 2">
            繼續 <span>→</span>
          </button>
        </div>
      </template>

      <!-- ── STEP 2: Food preferences ── -->
      <template v-else-if="step === 2">
        <h2 class="text-[22px] font-black text-gray-800 mb-1.5">🍗 你喜歡吃什麼？</h2>
        <p class="text-[14px] text-gray-500 font-semibold mb-4 leading-relaxed">
          選越多，小橘幫你規劃的菜單越好吃！
        </p>

        <!-- Category tabs -->
        <div class="flex gap-2 mb-4 overflow-x-auto pb-1 scrollbar-none">
          <button
            v-for="cat in FOOD_CATS"
            :key="cat.key"
            class="flex-shrink-0 border-2 rounded-full px-3.5 py-1.5 text-[12px] font-black transition-all"
            :class="foodTab === cat.key
              ? 'border-orange-400 bg-orange-50 text-orange-600'
              : 'border-gray-200 bg-white text-gray-500'"
            @click="foodTab = cat.key"
          >
            {{ cat.icon }} {{ cat.key }}
          </button>
        </div>

        <!-- Food chips -->
        <div v-for="cat in FOOD_CATS" :key="cat.key">
          <div v-if="foodTab === cat.key" class="flex flex-wrap gap-2 mb-4">
            <button
              v-for="food in cat.foods"
              :key="food"
              class="border-2 rounded-full px-3.5 py-1.5 text-[13px] font-bold transition-all"
              :class="selectedFoods.has(food)
                ? 'border-orange-400 bg-orange-50 text-orange-600'
                : 'border-gray-200 bg-white text-gray-500'"
              @click="toggleFood(food)"
            >{{ food }}</button>
          </div>
        </div>

        <!-- Custom food input -->
        <div class="bg-orange-50/60 rounded-2xl p-3.5 border-2 border-dashed border-orange-200 mb-4">
          <p class="text-[12px] font-black text-gray-500 mb-2">➕ 自訂食材</p>
          <div class="flex gap-2">
            <input
              v-model="customFoodInput"
              class="input flex-1 py-2 text-sm"
              placeholder="輸入你喜歡的食物…"
              @keydown.enter="addCustomFood"
            />
            <button class="btn-primary px-4 py-2 text-sm whitespace-nowrap" @click="addCustomFood">新增</button>
          </div>
          <div class="flex flex-wrap gap-2 mt-2">
            <span
              v-for="food in customFoods"
              :key="food"
              class="border-2 border-orange-300 bg-orange-50 text-orange-600 rounded-full px-3 py-1 text-[12px] font-bold flex items-center gap-1"
            >
              {{ food }}
              <button class="text-gray-400 hover:text-pink-400 text-[11px]" @click="removeCustomFood(food)">✕</button>
            </span>
          </div>
        </div>

        <!-- Selected summary -->
        <div class="bg-emerald-50 rounded-xl px-4 py-3 border-2 border-emerald-200 mb-5">
          <p class="text-[12px] font-black text-gray-500 mb-1">
            已選擇 <span class="text-orange-600">{{ selectedFoodList.length }}</span> 種食材
          </p>
          <p class="text-[12px] text-gray-400 font-semibold leading-relaxed">
            {{ selectedFoodList.slice(0, 8).join('、') }}{{ selectedFoodList.length > 8 ? '…' : '' }}
          </p>
        </div>

        <div class="flex gap-3">
          <button class="btn-secondary flex-1 py-3" @click="step = 1">← 上一步</button>
          <button
            class="btn-primary flex-2 py-3 flex items-center justify-center gap-2"
            style="flex:2"
            :disabled="loading"
            @click="finish"
          >
            <span v-if="loading">儲存中…</span>
            <span v-else>🐱 開始使用！</span>
          </button>
        </div>
      </template>

    </div>
  </div>
</template>

<style scoped>
.scrollbar-none::-webkit-scrollbar { display: none; }
.scrollbar-none { scrollbar-width: none; }
</style>
