<script setup lang="ts">
import type { CatBreed } from '@/constants/CatBreeds'

const props = defineProps<{
  breed?: CatBreed | string
  size?: number
  animated?: boolean
}>()
</script>

<template>
  <svg
    :width="size ?? 110"
    :height="size ?? 110"
    viewBox="0 0 200 200"
    :class="{ 'cat-breathe': animated !== false }"
    style="filter:drop-shadow(0 6px 16px rgba(107,63,30,.18));cursor:pointer;"
    xmlns="http://www.w3.org/2000/svg"
  >
    <!-- Default: orange cat (big version for hero) -->
    <template v-if="!breed || breed === 'orange'">
      <ellipse cx="100" cy="145" rx="52" ry="42" fill="#FFBB66"/>
      <ellipse cx="100" cy="150" rx="30" ry="24" fill="#FFE0A8"/>
      <path d="M148,165 Q175,145 168,120 Q162,100 148,110 Q158,120 155,138 Q152,155 148,165Z" fill="#FFBB66"/>
      <ellipse cx="100" cy="90" rx="44" ry="42" fill="#FFBB66"/>
      <path d="M68,48 Q72,32 84,44" stroke="#E8943A" stroke-width="2" fill="none" opacity=".5"/>
      <path d="M64,56 Q70,40 82,52" stroke="#E8943A" stroke-width="2" fill="none" opacity=".4"/>
      <polygon points="66,58 58,30 82,52" fill="#FFBB66"/>
      <polygon points="134,58 142,30 118,52" fill="#FFBB66"/>
      <polygon points="68,55 63,38 80,53" fill="#FFB3C6"/>
      <polygon points="132,55 137,38 120,53" fill="#FFB3C6"/>
      <path d="M82,84 Q88,78 94,84" fill="none" stroke="#3D2B1F" stroke-width="4" stroke-linecap="round"/>
      <path d="M106,84 Q112,78 118,84" fill="none" stroke="#3D2B1F" stroke-width="4" stroke-linecap="round"/>
      <ellipse cx="78" cy="96" rx="10" ry="6" fill="#FFB3C6" opacity=".55"/>
      <ellipse cx="122" cy="96" rx="10" ry="6" fill="#FFB3C6" opacity=".55"/>
      <ellipse cx="100" cy="100" rx="5" ry="3.5" fill="#FF85A1"/>
      <path d="M95,104 Q100,110 105,104" fill="none" stroke="#3D2B1F" stroke-width="2.5" stroke-linecap="round"/>
      <line x1="60" y1="98" x2="88" y2="101" stroke="#3D2B1F" stroke-width="1.5" stroke-linecap="round" opacity=".6"/>
      <line x1="60" y1="104" x2="88" y2="103" stroke="#3D2B1F" stroke-width="1.5" stroke-linecap="round" opacity=".6"/>
      <line x1="140" y1="98" x2="112" y2="101" stroke="#3D2B1F" stroke-width="1.5" stroke-linecap="round" opacity=".6"/>
      <line x1="140" y1="104" x2="112" y2="103" stroke="#3D2B1F" stroke-width="1.5" stroke-linecap="round" opacity=".6"/>
      <ellipse cx="76" cy="182" rx="18" ry="11" fill="#FFBB66"/>
      <ellipse cx="124" cy="182" rx="18" ry="11" fill="#FFBB66"/>
      <ellipse cx="68" cy="177" rx="5" ry="4" fill="#FFD090"/>
      <ellipse cx="76" cy="175" rx="5" ry="4" fill="#FFD090"/>
      <ellipse cx="84" cy="177" rx="5" ry="4" fill="#FFD090"/>
      <ellipse cx="116" cy="177" rx="5" ry="4" fill="#FFD090"/>
      <ellipse cx="124" cy="175" rx="5" ry="4" fill="#FFD090"/>
      <ellipse cx="132" cy="177" rx="5" ry="4" fill="#FFD090"/>
    </template>

    <template v-else-if="breed === 'black'">
      <ellipse cx="100" cy="145" rx="52" ry="42" fill="#2C2C2C"/>
      <ellipse cx="100" cy="150" rx="30" ry="24" fill="#3D3D3D"/>
      <path d="M148,165 Q175,145 168,120 Q162,100 148,110 Q158,120 155,138 Q152,155 148,165Z" fill="#2C2C2C"/>
      <ellipse cx="100" cy="90" rx="44" ry="42" fill="#2C2C2C"/>
      <polygon points="66,58 58,30 82,52" fill="#2C2C2C"/>
      <polygon points="134,58 142,30 118,52" fill="#2C2C2C"/>
      <polygon points="68,55 63,38 80,53" fill="#7B4F6E"/>
      <polygon points="132,55 137,38 120,53" fill="#7B4F6E"/>
      <ellipse cx="82" cy="84" rx="10" ry="12" fill="#7FD18A"/>
      <ellipse cx="118" cy="84" rx="10" ry="12" fill="#7FD18A"/>
      <ellipse cx="82" cy="85" rx="5" ry="8" fill="#1A1A1A"/>
      <ellipse cx="118" cy="85" rx="5" ry="8" fill="#1A1A1A"/>
      <circle cx="78" cy="81" r="2.5" fill="white" opacity=".8"/>
      <circle cx="114" cy="81" r="2.5" fill="white" opacity=".8"/>
      <ellipse cx="78" cy="96" rx="10" ry="6" fill="#7B4F6E" opacity=".5"/>
      <ellipse cx="122" cy="96" rx="10" ry="6" fill="#7B4F6E" opacity=".5"/>
      <ellipse cx="100" cy="100" rx="5" ry="3.5" fill="#C46E9A"/>
      <path d="M95,104 Q100,110 105,104" fill="none" stroke="#888" stroke-width="2.5" stroke-linecap="round"/>
      <line x1="60" y1="98" x2="88" y2="101" stroke="#888" stroke-width="1.5" stroke-linecap="round" opacity=".4"/>
      <line x1="140" y1="98" x2="112" y2="101" stroke="#888" stroke-width="1.5" stroke-linecap="round" opacity=".4"/>
      <ellipse cx="76" cy="182" rx="18" ry="11" fill="#2C2C2C"/>
      <ellipse cx="124" cy="182" rx="18" ry="11" fill="#2C2C2C"/>
    </template>

    <template v-else-if="breed === 'white'">
      <ellipse cx="100" cy="145" rx="52" ry="42" fill="#F8F8FF"/>
      <ellipse cx="100" cy="150" rx="30" ry="24" fill="#FFF5F8"/>
      <path d="M148,165 Q175,145 168,120 Q162,100 148,110 Q158,120 155,138 Q152,155 148,165Z" fill="#F0F0F8"/>
      <ellipse cx="100" cy="90" rx="44" ry="42" fill="#F8F8FF"/>
      <polygon points="66,58 58,30 82,52" fill="#F8F8FF"/>
      <polygon points="134,58 142,30 118,52" fill="#F8F8FF"/>
      <polygon points="68,55 63,38 80,53" fill="#FFD6E7"/>
      <polygon points="132,55 137,38 120,53" fill="#FFD6E7"/>
      <ellipse cx="82" cy="84" rx="10" ry="12" fill="#7EC8E3"/>
      <ellipse cx="118" cy="84" rx="10" ry="12" fill="#7EC8E3"/>
      <ellipse cx="82" cy="85" rx="5" ry="8" fill="#1A1A1A"/>
      <ellipse cx="118" cy="85" rx="5" ry="8" fill="#1A1A1A"/>
      <circle cx="78" cy="81" r="2.5" fill="white" opacity=".9"/>
      <circle cx="114" cy="81" r="2.5" fill="white" opacity=".9"/>
      <ellipse cx="78" cy="96" rx="10" ry="6" fill="#FFD6E7" opacity=".6"/>
      <ellipse cx="122" cy="96" rx="10" ry="6" fill="#FFD6E7" opacity=".6"/>
      <ellipse cx="100" cy="100" rx="5" ry="3.5" fill="#FFB3C6"/>
      <path d="M95,104 Q100,110 105,104" fill="none" stroke="#CCC" stroke-width="2.5" stroke-linecap="round"/>
      <line x1="60" y1="98" x2="88" y2="101" stroke="#CCC" stroke-width="1.5" stroke-linecap="round" opacity=".5"/>
      <line x1="140" y1="98" x2="112" y2="101" stroke="#CCC" stroke-width="1.5" stroke-linecap="round" opacity=".5"/>
      <ellipse cx="76" cy="182" rx="18" ry="11" fill="#F8F8FF"/>
      <ellipse cx="124" cy="182" rx="18" ry="11" fill="#F8F8FF"/>
    </template>

    <template v-else-if="breed === 'tabby'">
      <ellipse cx="100" cy="145" rx="52" ry="42" fill="#C8A882"/>
      <ellipse cx="100" cy="150" rx="30" ry="24" fill="#E8D4B8"/>
      <path d="M148,165 Q175,145 168,120 Q162,100 148,110 Q158,120 155,138 Q152,155 148,165Z" fill="#C8A882"/>
      <ellipse cx="100" cy="90" rx="44" ry="42" fill="#C8A882"/>
      <path d="M72,68 Q85,62 98,68" stroke="#8B6914" stroke-width="2.5" fill="none" opacity=".6"/>
      <path d="M102,68 Q115,62 128,68" stroke="#8B6914" stroke-width="2.5" fill="none" opacity=".6"/>
      <path d="M68,78 Q86,72 104,78" stroke="#8B6914" stroke-width="2.5" fill="none" opacity=".5"/>
      <path d="M75,55 Q82,46 90,55 Q97,46 105,55" stroke="#8B6914" stroke-width="2" fill="none" opacity=".6"/>
      <polygon points="66,58 58,30 82,52" fill="#C8A882"/>
      <polygon points="134,58 142,30 118,52" fill="#C8A882"/>
      <polygon points="68,55 63,38 80,53" fill="#FFB3C6"/>
      <polygon points="132,55 137,38 120,53" fill="#FFB3C6"/>
      <path d="M82,84 Q88,78 94,84" fill="none" stroke="#3D2B1F" stroke-width="4" stroke-linecap="round"/>
      <path d="M106,84 Q112,78 118,84" fill="none" stroke="#3D2B1F" stroke-width="4" stroke-linecap="round"/>
      <ellipse cx="78" cy="96" rx="10" ry="6" fill="#FFB3C6" opacity=".5"/>
      <ellipse cx="122" cy="96" rx="10" ry="6" fill="#FFB3C6" opacity=".5"/>
      <ellipse cx="100" cy="100" rx="5" ry="3.5" fill="#FF85A1"/>
      <path d="M95,104 Q100,110 105,104" fill="none" stroke="#3D2B1F" stroke-width="2.5" stroke-linecap="round"/>
      <line x1="60" y1="98" x2="88" y2="101" stroke="#3D2B1F" stroke-width="1.5" stroke-linecap="round" opacity=".5"/>
      <line x1="140" y1="98" x2="112" y2="101" stroke="#3D2B1F" stroke-width="1.5" stroke-linecap="round" opacity=".5"/>
      <ellipse cx="76" cy="182" rx="18" ry="11" fill="#C8A882"/>
      <ellipse cx="124" cy="182" rx="18" ry="11" fill="#C8A882"/>
    </template>

    <template v-else-if="breed === 'calico'">
      <ellipse cx="100" cy="145" rx="52" ry="42" fill="#F5F5F0"/>
      <ellipse cx="100" cy="150" rx="30" ry="24" fill="#FFF8F4"/>
      <path d="M148,165 Q175,145 168,120 Q162,100 148,110 Q158,120 155,138 Q152,155 148,165Z" fill="#F5F5F0"/>
      <ellipse cx="100" cy="90" rx="44" ry="42" fill="#F5F5F0"/>
      <ellipse cx="80" cy="82" rx="18" ry="14" fill="#FFBB66" opacity=".8"/>
      <ellipse cx="118" cy="95" rx="16" ry="12" fill="#2C2C2C" opacity=".7"/>
      <ellipse cx="95" cy="140" rx="14" ry="10" fill="#FFBB66" opacity=".65"/>
      <polygon points="66,58 58,30 82,52" fill="#F5F5F0"/>
      <polygon points="134,58 142,30 118,52" fill="#F5F5F0"/>
      <polygon points="68,55 63,38 80,53" fill="#FFB3C6"/>
      <polygon points="132,55 137,38 120,53" fill="#FFB3C6"/>
      <path d="M82,84 Q88,78 94,84" fill="none" stroke="#3D2B1F" stroke-width="4" stroke-linecap="round"/>
      <path d="M106,84 Q112,78 118,84" fill="none" stroke="#3D2B1F" stroke-width="4" stroke-linecap="round"/>
      <ellipse cx="78" cy="96" rx="10" ry="6" fill="#FFB3C6" opacity=".5"/>
      <ellipse cx="122" cy="96" rx="10" ry="6" fill="#FFB3C6" opacity=".5"/>
      <ellipse cx="100" cy="100" rx="5" ry="3.5" fill="#FF85A1"/>
      <path d="M95,104 Q100,110 105,104" fill="none" stroke="#3D2B1F" stroke-width="2.5" stroke-linecap="round"/>
      <line x1="60" y1="98" x2="88" y2="101" stroke="#3D2B1F" stroke-width="1.5" stroke-linecap="round" opacity=".5"/>
      <line x1="140" y1="98" x2="112" y2="101" stroke="#3D2B1F" stroke-width="1.5" stroke-linecap="round" opacity=".5"/>
      <ellipse cx="76" cy="182" rx="18" ry="11" fill="#F5F5F0"/>
      <ellipse cx="124" cy="182" rx="18" ry="11" fill="#F5F5F0"/>
    </template>

    <template v-else-if="breed === 'ragdoll'">
      <ellipse cx="100" cy="148" rx="56" ry="44" fill="#F0EAE0"/>
      <ellipse cx="100" cy="152" rx="32" ry="26" fill="#FFF8F0"/>
      <ellipse cx="100" cy="130" rx="36" ry="12" fill="#F8F4EC"/>
      <path d="M148,165 Q175,145 168,120 Q162,100 148,110 Q158,120 155,138 Q152,155 148,165Z" fill="#E8E0D4"/>
      <ellipse cx="100" cy="88" rx="46" ry="44" fill="#F0EAE0"/>
      <ellipse cx="100" cy="92" rx="30" ry="22" fill="#C8B49A" opacity=".3"/>
      <polygon points="64,56 54,24 80,48" fill="#F0EAE0"/>
      <polygon points="136,56 146,24 120,48" fill="#F0EAE0"/>
      <polygon points="66,53 59,30 78,47" fill="#C8A888"/>
      <polygon points="134,53 141,30 122,47" fill="#C8A888"/>
      <ellipse cx="82" cy="82" rx="11" ry="13" fill="#5BB8D4"/>
      <ellipse cx="118" cy="82" rx="11" ry="13" fill="#5BB8D4"/>
      <ellipse cx="82" cy="84" rx="5.5" ry="9" fill="#1A1A1A"/>
      <ellipse cx="118" cy="84" rx="5.5" ry="9" fill="#1A1A1A"/>
      <circle cx="78" cy="79" r="3" fill="white" opacity=".85"/>
      <circle cx="114" cy="79" r="3" fill="white" opacity=".85"/>
      <ellipse cx="78" cy="98" rx="11" ry="6" fill="#FFD6E7" opacity=".5"/>
      <ellipse cx="122" cy="98" rx="11" ry="6" fill="#FFD6E7" opacity=".5"/>
      <ellipse cx="100" cy="102" rx="5.5" ry="4" fill="#D4849A"/>
      <path d="M94,107 Q100,114 106,107" fill="none" stroke="#999" stroke-width="2.5" stroke-linecap="round"/>
      <line x1="58" y1="100" x2="88" y2="103" stroke="#CCC" stroke-width="1.5" stroke-linecap="round" opacity=".6"/>
      <line x1="142" y1="100" x2="112" y2="103" stroke="#CCC" stroke-width="1.5" stroke-linecap="round" opacity=".6"/>
      <ellipse cx="76" cy="182" rx="20" ry="12" fill="#F0EAE0"/>
      <ellipse cx="124" cy="182" rx="20" ry="12" fill="#F0EAE0"/>
    </template>
  </svg>
</template>

<style scoped>
.cat-breathe {
  animation: cat-breathe 3s ease-in-out infinite;
}
@keyframes cat-breathe {
  0%,100% { transform: translateY(0) scale(1); }
  50%      { transform: translateY(-4px) scale(1.02); }
}
</style>
