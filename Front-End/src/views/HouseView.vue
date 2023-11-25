<script setup>
import { ref, watch } from "vue";
import KakaoMap from "@/components/map/KakaoMap.vue";
import AptInfo from "@/components/map/AptInfo.vue";
import DongInfo from "@/components/map/DongInfo.vue";

const aptInfo = ref({});
const dongInfo = ref({});

const addr = ref("");
const detail = ref(true);

const div = ref(null);

watch(aptInfo, () => {
  div.value.scrollTo(0, 0);
});

watch(dongInfo, () => {
  div.value.scrollTo(0, 0);
});

const getAptInfo = (value) => {
  dongInfo.value = {};
  aptInfo.value = value;
};

const getDongInfo = (value) => {
  aptInfo.value = {};
  dongInfo.value = value;
};

const deleteInfo = () => {
  aptInfo.value = {};
  dongInfo.value = {};
};

const getAddr = (value) => {
  // console.log(value);
  addr.value = value;
};

const setDetail = (value) => {
  detail.value = value;
  // console.log(detail.value);
};
</script>

<template>
  <KakaoMap @send-apt-info="getAptInfo" @send-dong-info="getDongInfo" />

  <div id="info" v-show="aptInfo.name || dongInfo.name" ref="div">
    <div id="top" :class="{ 'sticky-top': detail }">
      <button
        class="btn-close me-auto"
        aria-label="Close"
        @click="deleteInfo"></button>
      {{ addr }}
      <div id="box" class="ms-auto"></div>
    </div>
    <AptInfo
      :info="aptInfo"
      @addr="getAddr"
      @detail="setDetail"
      v-show="aptInfo.name" />
    <DongInfo :info="dongInfo" @addr="getAddr" v-show="dongInfo.name" />
  </div>
</template>

<style scoped>
#info {
  padding-top: 70px;
  width: 25vw;
  height: 100vh;
  border-left: 1px solid #bbbbbb;
  z-index: 3;
  position: absolute;
  top: 0;
  right: 0;
  background-color: white;
  overflow-y: scroll;
  -ms-overflow-style: none;
}

#info::-webkit-scrollbar {
  display: none;
}

#top {
  display: flex;
  height: 34px;
  background-color: white;
  padding: 5px;
  text-align: center;
  border-bottom: 1px solid #bbbbbb;
}

#box {
  display: inline-block;
  width: 24px;
  height: 24px;
}
</style>
