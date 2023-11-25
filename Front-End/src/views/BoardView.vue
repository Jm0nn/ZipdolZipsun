<script setup>
import { ref, computed } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();

const boardNo = computed(() => route.params.boardNo);

const boardName = ref({
  10: "자유",
  11: "경상도/부산/울산",
  12: "전라도/광주",
  13: "서울",
  14: "경기도/인천",
  15: "충청도/대전",
  16: "강원도",
  17: "제주도",
});
</script>

<template>
  <div class="gray-head">
    <h2 class="mt-4 py-3 shadow-sm bg-light text-center">
      {{ boardName[boardNo] }} 게시판
    </h2>
  </div>
  <RouterView />
</template>

<style scoped></style>
