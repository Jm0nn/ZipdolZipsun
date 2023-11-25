<script setup>
import { reactive } from "vue";
import { useRouter } from "vue-router";
import { postArticle } from "@/api/board";
import AnnounceFormItem from "./item/AnnounceFormItem.vue";

const router = useRouter();

const user = reactive({
  loginUser: JSON.parse(window.sessionStorage.getItem("loginUser")),
});

const article = reactive({
  userId: `${user.loginUser.userId}`,
  userNo: `${user.loginUser.userNo}`,
  subject: "",
  content: "",
  boardNo: "01",
  articleNo: "",
  articleTypeNo: "1",
});

const writeArticle = () => {
  const success = (response) => {
    if (response.status == 200) {
      alert("등록이 완료되었습니다.");

      router.push({ name: "announce-list" });
    }
  };

  const fail = (error) => console.log(error);

  postArticle(article, "01", success, fail);
};
</script>

<template>
  <div class="row justify-content-center">
    <div class="col-lg-8 col-md-10 col-sm-12">
      <announce-form-item
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
      </announce-form-item>
    </div>
  </div>
</template>

<style scoped></style>
