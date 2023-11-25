<script setup>
import { ref } from "vue";
const prop = defineProps({ article: Object });
const emit = defineEmits(["evtProcess"]);

const refSubject = ref(null);
const refContent = ref(null);

const validate = () => {
  let isValid = true;
  let errMsg = "";
  const article = prop.article;

  !article.subject
    ? ((isValid = false), (errMsg = "제목을 입력해주세요."))
    : !article.content
    ? ((isValid = false), (errMsg = "내용을 입력해주세요."))
    : (isValid = true);

  if (!isValid) {
    alert(errMsg);
  } else {
    emit("evtProcess");
  }
};
</script>

<template>
  <div class="col-lg-8 col-md-10 col-sm-12">
    <div class="mb-3">
      <label for="subject" class="form-label">제목 : </label>
      <input
        type="text"
        class="form-control"
        id="subject"
        name="subject"
        v-model="article.subject"
        ref="refSubject" />
    </div>
    <div class="mb-3">
      <label for="content" class="form-label">내용 : </label>
      <textarea
        class="form-control"
        id="content"
        name="content"
        rows="7"
        v-model="article.content"
        ref="refContent"></textarea>
    </div>
    <div class="col-auto text-center">
      <slot name="form-submit" :param="validate"></slot>
      <router-link
        :to="{
          name: 'announce-list',
        }"
        class="btn btn-outline-danger mb-3">
        목록으로이동...
      </router-link>
    </div>
  </div>
</template>

<style scoped></style>
