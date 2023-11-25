<script setup>
import { reactive } from "vue";
import { useRoute, useRouter } from "vue-router";
import { postArticle } from "@/api/board";
import BoardFormItem from "@/components/board/item/BoardFormItem.vue";

const route = useRoute();
const router = useRouter();

const user = reactive({
  loginUser: JSON.parse(window.sessionStorage.getItem("loginUser")),
});

const article = reactive({
  userId: `${user.loginUser.userId}`,
  userNo: `${user.loginUser.userNo}`,
  subject: "",
  content: "",
  boardNo: `${route.params.boardNo}`,
  articleNo: "",
  articleTypeNo: "1",
});

//console.log(article.boardNo);

const writeArticle = () => {
  const success = (response) => {
    if (response.status == 200) {
      alert("등록이 완료되었습니다.");

      router.push({
        name: "board-list",
        params: {
          boardNo: route.params.boardNo,
        },
      });
    }
  };

  const fail = (error) => console.log(error);

  postArticle(article, article.boardNo, success, fail);
};
</script>

<template>
  <div class="row justify-content-center">
    <div class="col-lg-8 col-md-10 col-sm-12">
      <board-form-item
        class="write"
        :article="article"
        @evt-process="writeArticle">
        <template v-slot:form-submit="prop">
          <button
            type="button"
            class="btn btn-outline-primary mb-3 me-2"
            @click="prop.param">
            글쓰기
          </button>
        </template>
      </board-form-item>
    </div>
  </div>
</template>

<style scoped></style>
