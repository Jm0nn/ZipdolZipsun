<script setup>
import { ref, reactive, onMounted } from "vue";
import { RouterLink, useRouter } from "vue-router";
import { useCookies } from "vue3-cookies";
import { postUser, getPass } from "@/api/member";

const router = useRouter();
const { cookies } = useCookies();

const formRef = ref();
const findRef = ref();

const modalCheck = ref(false);
const login = ref(false);
const find = ref(false);
const show = ref(false);

const password = ref("");

const formState = reactive({
  userId: "",
  userPass: "",
  remember: false,
});

const findState = reactive({
  userId: "",
});

onMounted(() => {
  modalCheck.value = false;
  login.value = false;
  find.value = false;
  show.value = false;
});

const modalOpen = () => {
  modalCheck.value = true;
  login.value = true;
  find.value = false;
  show.value = false;

  formState.userId = "";
  formState.userPass = "";

  findState.userId = "";
  password.value = "";

  if (cookies.isKey("saveId")) {
    formState.userId = cookies.get("saveId");
    formState.remember = true;
  }
};

const modalClose = () => {
  modalCheck.value = false;
  login.value = false;
  find.value = false;
  show.value = false;

  formState.userId = "";
  formState.userPass = "";

  findState.userId = "";
  password.value = "";
};

const onLogin = () => {
  formRef.value
    .validate()
    .then(() => {
      const user = {
        userId: formState.userId,
        userPass: formState.userPass,
      };

      postUser(
        user,
        ({ status, data }) => {
          if (status === 200) {
            window.sessionStorage.setItem("loginUser", JSON.stringify(data));

            if (formState.remember) {
              cookies.set("saveId", user.userId);
            } else {
              if (cookies.isKey("saveId")) {
                cookies.remove("saveId");
              }
            }

            modalOpen();

            router.go(0);
          }
        },
        (err) => {
          alert("로그인에 실패했습니다.");
          console.log(error);
        }
      );
    })
    .catch((error) => console.log(error));
};

const findPass = () => {
  modalCheck.value = true;
  login.value = false;
  find.value = true;
  show.value = false;

  formState.userId = "";
  formState.userPass = "";

  findState.userId = "";
  password.value = "";
};

const findPassword = () => {
  findRef.value
    .validate()
    .then(() => {
      getPass(
        findState.userId,
        ({ status, data }) => {
          if (status === 200) {
            formState.userId = "";
            formState.userPass = "";

            password.value = data;

            modalCheck.value = true;
            login.value = false;
            find.value = false;
            show.value = true;
          }
        },
        (err) => {
          alert("오류가 발생했습니다.");
          console.log(err);
        }
      );
    })
    .catch((error) => console.log(error));
};

const user = reactive({
  loginUser: JSON.parse(window.sessionStorage.getItem("loginUser")),
});

const logout = async () => {
  window.sessionStorage.removeItem("loginUser");
  alert("로그아웃 되었습니다.");
  await router.push({ name: "main-view" });
  router.go(0);
};
</script>

<template>
  <header>
    <nav
      class="navbar bg-body-secondary fixed-top"
      style="height: 70px; z-index: 50">
      <router-link
        class="navbar-brand nav-item ms-5"
        :to="{ name: 'main-view' }">
        <img
          src="@/assets/zipdolzipsun_logo.png"
          alt="apt image"
          style="width: 60px" />
      </router-link>
      <router-link
        class="nav-item text-secondary text-decoration-none ms-3"
        :to="{ name: 'announce-list' }">
        공지사항
      </router-link>
      <router-link
        class="nav-item text-secondary text-decoration-none ms-3"
        :to="{ name: 'house-view' }">
        부동산검색
      </router-link>
      <router-link
        class="nav-item text-secondary text-decoration-none ms-3"
        :to="{ name: 'board-list', params: { boardNo: '10' } }">
        커뮤니티
      </router-link>
      <router-link
        class="nav-item text-secondary text-decoration-none ms-3"
        :to="{ name: 'info-view' }">
        부동산정보
      </router-link>
      <div class="nav-item ms-auto" v-if="!user.loginUser">
        <router-link
          class="nav-item btn btn-secondary ms-auto"
          :to="{ name: 'member-regist' }">
          Sign up
        </router-link>
        <button
          class="nav-item btn btn-outline-secondary ms-3 me-5"
          @click="modalOpen">
          Login
        </button>
      </div>
      <div class="nav-item ms-auto" v-if="user.loginUser">
        <router-link
          :to="{ name: 'member-modify' }"
          style="text-decoration: none; font-weight: bold">
          {{ user.loginUser.nickName }} </router-link
        >님 반갑습니다.

        <router-link
          v-if="user.loginUser.userId == 'admin'"
          class="nav-item btn btn-outline-secondary ms-3"
          :to="{ name: 'admin-view' }">
          관리자
        </router-link>
        <button
          class="nav-item btn btn-outline-danger ms-3 me-5"
          @click="logout">
          로그아웃
        </button>
      </div>
    </nav>
    <!-- <div style="margin-top: 70px"></div> -->
  </header>
  <br />
  <br />

  <div class="modal-wrap" v-if="modalCheck" @click="modalClose"></div>
  <div class="modal-container" v-if="modalCheck">
    <div style="display: flex; text-align: center">
      <button
        class="btn-close me-auto"
        aria-label="Close"
        @click="modalClose"></button>
      <h5 style="display: inline" v-if="login"><b>로그인</b></h5>
      <h5 style="display: inline" v-if="find"><b>비밀번호 찾기</b></h5>
      <h5 style="display: inline" v-if="show"><b>비밀번호</b></h5>
      <div id="box" class="ms-auto"></div>
    </div>
    <a-form
      class="mt-2"
      ref="formRef"
      :model="formState"
      name="basic"
      :label-col="{ span: 8 }"
      :wrapper-col="{ span: 16 }"
      autocomplete="off"
      v-if="login">
      <a-form-item
        label="아이디"
        name="userId"
        :rules="[{ required: true, message: '아이디를 입력하세요.' }]">
        <a-input v-model:value="formState.userId" />
      </a-form-item>

      <a-form-item
        label="비밀번호"
        name="userPass"
        :rules="[{ required: true, message: '비밀번호를 입력하세요.' }]">
        <a-input-password v-model:value="formState.userPass" />
      </a-form-item>

      <a-form-item name="remember" :wrapper-col="{ offset: 7, span: 16 }">
        <a-checkbox v-model:checked="formState.remember">
          아이디 저장
        </a-checkbox>
      </a-form-item>

      <a-form-item :wrapper-col="{ offset: 4, span: 20 }">
        <a-button class="me-3" @click="onLogin">로그인</a-button>
        <a-button @click="findPass">비밀번호 찾기</a-button>
      </a-form-item>
    </a-form>

    <a-form
      class="mt-5"
      ref="findRef"
      :model="findState"
      name="findPass"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 16 }"
      autocomplete="off"
      v-if="find">
      <a-form-item
        label="아이디"
        name="userId"
        :rules="[{ required: true, message: '아이디를 입력하세요.' }]">
        <a-input v-model:value="findState.userId" />
      </a-form-item>

      <a-form-item :wrapper-col="{ offset: 3, span: 20 }">
        <a-button class="mt-4 me-3" @click="findPassword">
          비밀번호 찾기
        </a-button>
        <a-button @click="modalOpen">뒤로가기</a-button>
      </a-form-item>
    </a-form>

    <a-form
      class="mt-4"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 16 }"
      autocomplete="off"
      v-if="show">
      <template v-if="password">
        <a-form-item label="아이디" name="userId">
          <a-input
            v-model:value="findState.userId"
            disabled
            style="
              background-color: white;
              color: black;
              pointer-events: none;
            " />
        </a-form-item>

        <a-form-item label="비밀번호" name="userPass">
          <a-input
            v-model:value="password"
            disabled
            style="
              background-color: white;
              color: black;
              pointer-events: none;
            " />
        </a-form-item>

        <a-form-item :wrapper-col="{ offset: 8, span: 20 }">
          <a-button class="mt-3" @click="modalOpen">로그인 하기</a-button>
        </a-form-item>
      </template>
      <template v-if="!password">
        <div class="mt-5" style="text-align: center">
          <h5><b>존재하지 않는 아이디입니다.</b></h5>
        </div>
        <a-form-item :wrapper-col="{ offset: 2, span: 25 }">
          <a-button class="mt-5 me-3" @click="findPass">비밀번호 찾기</a-button>
          <a-button @click="modalOpen">로그인 하기</a-button>
        </a-form-item>
      </template>
    </a-form>
  </div>
</template>

<style scoped>
.modal-wrap {
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  z-index: 50;
}
/* modal or popup */
.modal-container {
  position: fixed;
  top: 65px;
  right: 10px;
  width: 330px;
  height: 300px;
  background: #fff;
  border: 1px solid #aaaaaa;
  border-radius: 10px;
  padding: 20px;
  box-sizing: border-box;
  z-index: 100;
}

#box {
  display: inline-block;
  width: 24px;
  height: 24px;
}
</style>
