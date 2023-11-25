<script setup>
// import { ref, onMounted } from "vue";
// import { getPolicy } from "@/api/information";

// const policyList = ref([]);

// const pgno = ref(1);

// onMounted(() => {
//   getPolicyList();
// });

// const prev = () => {
//   if (pgno.value > 1) {
//     pgno.value--;
//     getPolicyList();
//   }
// };

// const next = () => {
//   pgno.value++;
//   getPolicyList();
// };

// const getPolicyList = () => {
//   getPolicy(pgno.value, ({ status, data }) => {
//     if (status === 200) {
//       policyList.value = data;
//     }
//   });
// };
</script>

<template>
  <!-- <div>
    <h1>PolicyPage</h1>
    {{ policyList }}
    <button @click="prev">이전</button>
    <button @click="next">다음</button>
  </div> -->
</template>

<style scoped></style>
