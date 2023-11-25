<script setup>
import { ref, computed, reactive } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getArticle, delArticle, updateHit } from "@/api/board";
import { getComments, postComment, delComments } from "@/api/comment";

import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";

dayjs.locale("ko");
dayjs.extend(relativeTime);

const route = useRoute();
const router = useRouter();

const article = ref({});
const comments = ref({});

const commentList = ref([]);

const comment = ref("");
const submitting = ref(false);

const pagination = ref({
  pageSize: 5,
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

    if (!user.loginUser || user.loginUser.nickName != article.value.nickName) {
      updateHit(articleNo, () => {}, fail);
    }
  }
};

const fail = (error) => {
  alert("오류가 발생했습니다.");
  console.log(error);
  router.push({
    name: "board-list",
    params: {
      boardNo: article.boardNo,
    },
  });
};

getArticle(boardNo, articleNo, success, fail);

getComments(
  articleNo,
  ({ status, data }) => {
    if (status === 200) {
      commentList.value = data.map((comment) => {
        const c = comment;
        c.computedContent = c.content.replace(/(?:\r\n|\r|\n)/g, "<br />");
        c.computedTime = calTime(c.registTime);
        return c;
      });
    }
  },
  fail
);

const deleteArticle = () => {
  if (confirm("정말로 삭제하시겠습니까?")) {
    const success = (response) => {
      if (response.status == 200) {
        alert("삭제가 완료되었습니다.");

        router.push({
          name: "board-list",
          params: {
            boardNo: article.boardNo,
          },
        });
      }
    };

    delArticle(boardNo, articleNo, success, (err) => {
      alert("오류가 발생했습니다.");
      console.log(err);
    });
  }
};

const commentSubmit = async () => {
  if (!comment.value) {
    alert("댓글을 입력하세요");
    return;
  } else if (!user.loginUser) {
    alert("로그인이 필요합니다.");
    return;
  }

  submitting.value = true;

  comments.value = {
    articleNo: articleNo,
    nickName: user.loginUser.nickName,
    content: comment.value,
  };

  // console.log(comments.value);

  await postComment(
    comments.value,
    ({ status, data }) => {
      if (status === 200) {
        alert("댓글이 등록되었습니다.");

        commentList.value = data.map((comment) => {
          const c = comment;
          c.computedContent = c.content.replace(/(?:\r\n|\r|\n)/g, "<br />");
          c.computedTime = calTime(c.registTime);
          return c;
        });
        submitting.value = false;

        comments.value = {};
        comment.value = "";
      }
    },
    (err) => {
      alert("오류가 발생했습니다.");
      console.log(err);
      submitting.value = false;
    }
  );
};

const calTime = (registTime) => {
  return new Date(registTime).toLocaleDateString();
};

const deleteComment = (commentNo) => {
  if (confirm("삭제하시겠습니까?")) {
    delComments(
      articleNo,
      commentNo,
      ({ status, data }) => {
        if (status === 200) {
          alert("삭제되었습니다.");
          commentList.value = data.map((comment) => {
            const c = comment;
            c.computedContent = c.content.replace(/(?:\r\n|\r|\n)/g, "<br />");
            c.computedTime = calTime(c.registTime);
            return c;
          });
        }
      },
      (err) => {
        alert("오류가 발생했습니다.");
        console.log(err);
      }
    );
  }
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
            :to="{
              name: 'board-list',
              params: {
                boardNo: article.boardNo,
              },
            }"
            class="btn btn-outline-primary mb-3">
            글목록
          </router-link>
          <div
            v-if="
              user.loginUser &&
              (user.loginUser.nickName == article.nickName ||
                user.loginUser.userId == 'admin')
            ">
            <router-link
              :to="{
                name: 'board-modify',
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
      <div v-if="article.boardNo != '01'">
        <template class="row">
          <div class="col-6">댓글</div>
          <div class="col-6" style="text-align: right">
            {{ commentList.length }} 개의 답변
          </div>
        </template>

        <a-comment>
          <template #content>
            <a-form-item>
              <a-textarea v-model:value="comment" :rows="4" />
            </a-form-item>
            <a-form-item>
              <a-button
                html-type="submit"
                :loading="submitting"
                type="primary"
                @click="commentSubmit">
                등록
              </a-button>
            </a-form-item>
          </template>
        </a-comment>
        <a-list
          class="comment-list"
          :header="`${commentList.length} 개의 답변`"
          item-layout="horizontal"
          :data-source="commentList"
          :pagination="pagination">
          <template #renderItem="{ item }">
            <a-list-item>
              <template #actions>
                <a
                  v-if="
                    user.loginUser &&
                    (user.loginUser.nickName == item.nickName ||
                      user.loginUser.userId == 'admin')
                  "
                  :key="item.commentNo"
                  @click="deleteComment(item.commentNo)">
                  삭제
                </a>
              </template>
              <a-comment :author="item.nickName">
                <!-- <template #actions>
                  <span v-for="(action, index) in item.actions" :key="index">{{
                    action
                  }}</span>
                </template> -->
                <template #content>
                  <p>
                    {{ item.computedContent }}
                  </p>
                </template>
                <template #datetime>
                  <a-tooltip :title="dayjs(item.registTime).fromNow()">
                    <span>
                      {{ item.computedTime }}
                    </span>
                  </a-tooltip>
                </template>
              </a-comment>
            </a-list-item>
          </template>
        </a-list>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
