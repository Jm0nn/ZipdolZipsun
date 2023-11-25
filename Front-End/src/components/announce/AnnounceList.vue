<script setup>
import { ref, reactive } from "vue";
import { getAnnounce, searchArticles } from "@/api/board";

const articles = ref([]);

const word = ref("");

const search = reactive({
  boardNo: 1,
  nickName: "",
  subject: "",
  content: "",
});

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

const fail = (error) => console.log(JSON.stringify(error));

getAnnounce(success, fail);

const searchList = async () => {
  search.subject = word.value;
  // console.log(search);
  await searchArticles(search, success, fail);
};
</script>

<template>
  <div class="row justify-content-center">
    <div class="col-lg-8 col-md-10 col-sm-12">
      <div class="row align-self-center mb-2">
        <div class="col-md-2 text-start">
          <router-link
            :to="{
              name: 'announce-write',
              params: {
                boardNo: '01',
              },
            }"
            class="btn btn-outline-primary btn-sm"
            v-if="userId == 'admin'">
            글쓰기
          </router-link>
        </div>
        <div class="col-md-7"></div>
        <div class="col-md-3">
          <div class="input-group input-group-sm">
            <input
              type="text"
              name="word"
              id="word"
              class="form-control"
              v-model="word"
              placeholder="제목 검색"
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

      <a-table
        :data-source="articles"
        :columns="columns"
        :pagination="{ position: ['bottomCenter'] }">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'subject'">
            <router-link
              :to="{
                name: 'announce-detail',
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
