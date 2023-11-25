<script setup>
import { ref, reactive, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getArticles, searchArticles } from "@/api/board";

import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";

dayjs.locale("ko");
dayjs.extend(relativeTime);

const route = useRoute();
const router = useRouter();

const word = ref("");

const search = ref({
  boardNo: 0,
  nickName: "",
  subject: "",
  content: "",
});

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

const articles = ref([]);

const userId = ref("");
const columns = ref([
  {
    title: "글번호",
    dataIndex: "articleNo",
    key: "articleNo",
    width: 100,
  },
  {
    title: "제목",
    dataIndex: "subject",
    key: "subject",
    ellipsis: true,
  },
  {
    title: "작성자",
    dataIndex: "nickName",
    key: "nickName",
    width: 100,
  },
  {
    title: "작성일",
    dataIndex: "registTime",
    key: "registTime",
    width: 200,
  },
  {
    title: "조회",
    dataIndex: "hit",
    key: "hit",
    width: 100,
  },
]);

const boardNo = ref(route.params.boardNo);
watch(
  () => route.params.boardNo,
  (newValue, oldValue) => {
    //console.log(newValue, oldValue);
    boardNo.value = newValue;
    word.value = "";
    getArticles(newValue, success, fail);
  }
);

if (window.sessionStorage.getItem("loginUser")) {
  userId.value = JSON.parse(window.sessionStorage.getItem("loginUser")).userId;
}

const success = (response) => {
  if (response.status == 200) {
    articles.value = response.data;
    articles.value = articles.value.map((article) => {
      let registTime = new Date(article.registTime).toLocaleString();

      article.registTime = registTime.substr(0, 13);
      return article;
    });
    // console.log(articles.value);
  } else if (response.status == 204) {
    articles.value = [];
  }
};

const fail = (error) => console.log(error);

getArticles(boardNo.value, success, fail);

const searchList = async () => {
  let key = document.querySelector("#key").value;
  if (key) {
    search.value[key] = word.value;
  }
  search.value.boardNo = boardNo.value;
  // console.log(search);
  await searchArticles(search.value, success, fail);
  document.querySelector("#key").value = "";
  word.value = "";
  search.value = {
    boardNo: boardNo.value,
    nickName: "",
    subject: "",
    content: "",
  };
};
</script>

<template>
  <div class="row justify-content-center">
    <div class="col-md-3">
      <div class="border-end bg-white mt-5" id="sidebar-wrapper">
        <div class="list-group list-group-flush">
          <RouterLink
            class="list-group-item list-group-item-action list-group-item-light p-3"
            :to="{ name: 'board-list', params: { boardNo: '10' } }"
            >자유게시판</RouterLink
          >
          <RouterLink
            class="list-group-item list-group-item-action list-group-item-light p-3"
            :to="{ name: 'board-list', params: { boardNo: '11' } }"
            >경상도/부산/울산</RouterLink
          >
          <RouterLink
            class="list-group-item list-group-item-action list-group-item-light p-3"
            :to="{ name: 'board-list', params: { boardNo: '12' } }"
            >전라도/광주</RouterLink
          >
          <RouterLink
            class="list-group-item list-group-item-action list-group-item-light p-3"
            :to="{ name: 'board-list', params: { boardNo: '13' } }"
            >서울</RouterLink
          >
          <RouterLink
            class="list-group-item list-group-item-action list-group-item-light p-3"
            :to="{ name: 'board-list', params: { boardNo: '14' } }"
            >경기도/인천</RouterLink
          >
          <RouterLink
            class="list-group-item list-group-item-action list-group-item-light p-3"
            :to="{ name: 'board-list', params: { boardNo: '15' } }"
            >충청도/대전</RouterLink
          >
          <RouterLink
            class="list-group-item list-group-item-action list-group-item-light p-3"
            :to="{ name: 'board-list', params: { boardNo: '16' } }"
            >강원도</RouterLink
          >
          <RouterLink
            class="list-group-item list-group-item-action list-group-item-light p-3"
            :to="{ name: 'board-list', params: { boardNo: '17' } }"
            >제주도</RouterLink
          >
        </div>
      </div>
    </div>
    <div class="col-lg-8 col-md-9 col-sm-12">
      <div class="row align-self-center mb-2">
        <div class="col-md-2 text-start">
          <router-link
            :to="{
              name: 'board-write',
              params: {
                boardNo: route.params.boardNo,
              },
            }"
            class="btn btn-outline-primary btn-sm">
            글쓰기
          </router-link>
        </div>
        <div class="col-md-7 offset-3">
          <div class="d-flex" id="form-search" action="">
            <select
              name="key"
              id="key"
              class="form-select form-select-sm ms-5 me-1 w-50"
              aria-label="검색조건">
              <option value="" selected>검색조건</option>
              <option value="nickName">작성자</option>
              <option value="subject">제목</option>
              <option value="content">내용</option>
            </select>
            <div class="input-group input-group-sm">
              <input
                type="text"
                name="word"
                id="word"
                class="form-control"
                v-model="word"
                placeholder="검색어"
                @keyup.enter="searchList" />
              <button
                id="btn-search"
                class="btn btn-dark"
                type="button"
                @click="searchList">
                검색
              </button>
            </div>
          </div>
        </div>
      </div>

      <a-table
        :data-source="articles"
        :columns="columns"
        :pagination="{ position: ['bottomCenter'] }">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'subject'">
            <router-link
              :to="{
                name: 'board-detail',
                params: {
                  articleNo: record.articleNo,
                  boardNo: record.boardNo,
                },
              }"
              class="article-title link-dark"
              style="text-decoration: none">
              {{ record.subject }}
            </router-link>
          </template>
        </template>
      </a-table>
    </div>
    <div class="row"></div>
  </div>
</template>

<style scoped></style>
