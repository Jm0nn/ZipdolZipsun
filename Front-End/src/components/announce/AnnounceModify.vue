<script setup>
import { reactive } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getArticle, putArticle } from "@/api/board";
import AnnounceFormItem from "./item/AnnounceFormItem.vue";

const route = useRoute();
const router = useRouter();

const user = reactive({
  loginUser: JSON.parse(window.sessionStorage.getItem("loginUser")),
});

const article = reactive({
  articleNo: "",
  articleTypeNo: "",
  boardNo: "",
  userNo: "",
  subject: "",
  content: "",
  hit: 0,
  registTime: "",
});

const articleNo = route.params.articleNo;
const boardNo = route.params.boardNo;

const success = (response) => {
  if (response.status == 200) {
    const a = response.data;
    article.articleNo = a.articleNo;
    article.articleTypeNo = a.articleTypeNo;
    article.boardNo = a.boardNo;
    article.userNo = a.userNo;
    article.subject = a.subject;
    article.content = a.content;
    article.hit = a.hit;
    article.registTime = a.registTime;
  }
};

const fail = (error) => console.log(error);

getArticle(boardNo, articleNo, success, fail);

const modifyArticle = () => {
  const success = (response) => {
    if (response.status == 200) {
      alert("수정이 완료되었습니다.");
      router.push({
        name: "announce-detail",
        params: {
          articleNo: article.articleNo,
          boardNo: article.boardNo,
        },
      });
    }
  };

  const fail = (error) => console.log(error);

  putArticle(article, boardNo, articleNo, success, fail);
};
</script>

<template>
  <div class="row justify-content-center">
    <div class="col-lg-8 col-md-10 col-sm-12">
      <announce-form-item
        class="write"
        :article="article"
        @evt-process="modifyArticle">
        <template v-slot:form-submit="prop">
          <button
            type="button"
            class="btn btn-outline-primary mb-3 me-2"
            @click="prop.param">
            글수정
          </button>
        </template>
      </announce-form-item>
    </div>
  </div>
</template>

<style scoped></style>
