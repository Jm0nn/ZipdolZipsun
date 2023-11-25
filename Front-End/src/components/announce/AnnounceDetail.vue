<script setup>
import { ref, computed, reactive } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getArticle, delArticle, updateHit } from "@/api/board";

const route = useRoute();
const router = useRouter();

const article = ref({});

const articleNo = route.params.articleNo;
const boardNo = route.params.boardNo;

const user = reactive({
  loginUser: JSON.parse(window.sessionStorage.getItem("loginUser")),
});

const success = (response) => {
  if (response.status == 200) {
    const a = response.data;
    a.computedContent = a.content.replace(/(?:\r\n|\r|\n)/g, "<br />");
    a.computedTime = calTime(a.registTime);

    article.value = a;
  }
};

const fail = (error) => {
  console.log("오류가 발생했습니다.");
  console.log(error);
  router.push({ name: "announce-list" });
};

getArticle(boardNo, articleNo, success, fail);
if (!user.loginUser || user.loginUser.userId != "admin") {
  updateHit(
    articleNo,
    () => {},
    (err) => console.log(err)
  );
}

const deleteArticle = () => {
  if (confirm("정말로 삭제하시겠습니까?")) {
    const success = (response) => {
      if (response.status == 200) {
        alert("삭제가 완료되었습니다.");

        router.push({ name: "announce-list" });
      }
    };

    const fail = (error) => console.log(error);

    delArticle(boardNo, articleNo, success, fail);
  }
};

const calTime = (registTime) => {
  return new Date(registTime).toLocaleDateString();
};
</script>

<template>
  <div class="row justify-content-center">
    <div class="col-lg-8 col-md-10 col-sm-12">
      <div class="row my-2">
        <h2 class="text-secondary px-5">
          {{ article.subject }}
        </h2>
      </div>
      <div class="row">
        <div class="col-md-8">
          <div class="clearfix align-content-center">
            <img
              class="avatar me-2 float-md-start bg-light p-2"
              src="https://raw.githubusercontent.com/twbs/icons/main/icons/person-fill.svg" />
            <p>
              <span class="fw-bold">{{ article.nickName }}</span> <br />
              <span class="text-secondary fw-light">
                {{ article.computedTime }}
              </span>
            </p>
          </div>
        </div>
        <div class="col-md-4 align-self-center text-end">
          조회 : {{ article.hit }}
        </div>
        <div class="divider mb-3"></div>
        <div class="text-secondary" v-html="article.computedContent"></div>
        <div class="divider mt-3 mb-3"></div>
        <div class="d-flex justify-content-end">
          <router-link
            :to="{ name: 'announce-list' }"
            class="btn btn-outline-primary mb-3">
            글목록
          </router-link>
          <div v-if="user.loginUser && user.loginUser.userId == 'admin'">
            <router-link
              :to="{
                name: 'announce-modify',
                params: {
                  articleNo: article.articleNo,
                  boardNo: article.boardNo,
                },
              }"
              type="button"
              class="btn btn-outline-success mb-3 ms-1">
              글수정
            </router-link>
            <button
              type="button"
              class="btn btn-outline-danger mb-3 ms-1"
              @click="deleteArticle">
              글삭제
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
