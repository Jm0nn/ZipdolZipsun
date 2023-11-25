<script setup></script>

<template>
  <div style="height: 22px"></div>
  <div class="bg-light" style="height: 40px; padding-top: 8px">
    <span style="width: 116px; display: inline-block"></span>
    <router-link class="mx-4" :to="{ name: 'news-view' }">뉴스</router-link>
    <router-link class="mx-4" :to="{ name: 'blog-view' }">블로그</router-link>
    <router-link class="mx-4" :to="{ name: 'research-view' }">
      정책/연구자료
    </router-link>
  </div>
  <div style="padding-top: 10px">
    <RouterView />
    <div style="height: 50px"></div>
  </div>
</template>

<style scoped>
.mx-4 {
  display: inline-block;
  transition: 0.3s;
  font-weight: bold;
  text-decoration: none;
}
.mx-4:hover {
  -webkit-transform: scale(1.2);
  transform: scale(1.2);
}
</style>
