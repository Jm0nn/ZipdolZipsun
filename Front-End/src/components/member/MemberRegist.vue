<script setup>
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { registUser, chkId, chkNickName } from "@/api/member";

const router = useRouter();

const formRef = ref();

const labelCol = {
  style: {
    width: "150px",
  },
};

const wrapperCol = {
  span: 14,
};

const formState = reactive({
  userId: "",
  userPass: "",
  checkPass: "",
  userName: "",
  nickName: "",
  sex: "male",
  birthDate: "",
  emailId: "",
  emailDomain: "",
  phoneNumber1: "010",
  phoneNumber2: "",
  phoneNumber3: "",
});

const validateId = async (_rule, value) => {
  if (value === "") {
    return Promise.reject("아이디를 입력하세요.");
  } else {
    if (formState.userId.length < 5 || formState.userId.length > 16) {
      return Promise.reject("아이디는 5자 이상 16자 이하입니다.");
    } else {
      let promise = new Promise((resolve, reject) => {
        chkId(formState.userId, ({ data }) => {
          if (data === 0) {
            resolve();
          } else {
            reject("아이디가 존재합니다.");
          }
        });
      });

      return await promise;
    }
  }
};

const validateName = async (_rule, value) => {
  if (value === "") {
    return Promise.reject("이름을 입력하세요.");
  } else {
    if (formState.userName.length < 2 || formState.userName.length > 16) {
      return Promise.reject("이름은 2자 이상 16자 이하입니다.");
    } else {
      return Promise.resolve();
    }
  }
};

const validateNickName = async (_rule, value) => {
  if (value === "") {
    return Promise.reject("닉네임을 입력하세요.");
  } else {
    if (formState.nickName.length < 2 || formState.nickName.length > 16) {
      return Promise.reject("닉네임은 2자 이상 16자 이하입니다.");
    } else {
      let promise = new Promise((resolve, reject) => {
        chkNickName(formState.nickName, ({ data }) => {
          if (data === 0) {
            resolve();
          } else {
            reject("닉네임이 존재합니다.");
          }
        });
      });

      return await promise;
    }
  }
};

const validatePass = async (_rule, value) => {
  if (value === "") {
    return Promise.reject("비밀번호를 입력하세요.");
  } else {
    if (formState.userPass.length < 6 || formState.userPass.length > 16) {
      return Promise.reject("비밀번호는 6자 이상 16자 이하입니다.");
    }
    if (formState.checkPass !== "") {
      formRef.value.validateFields("checkPass");
    }
    return Promise.resolve();
  }
};

const validatePass2 = async (_rule, value) => {
  if (value === "") {
    return Promise.reject("비밀번호를 다시 입력하세요.");
  } else if (value !== formState.userPass) {
    return Promise.reject("비밀번호가 일치하지 않습니다.");
  } else {
    return Promise.resolve();
  }
};

const rules = {
  userId: [
    {
      required: true,
      validator: validateId,
      trigger: "change",
    },
  ],
  userPass: [
    {
      required: true,
      validator: validatePass,
      trigger: "change",
    },
  ],
  checkPass: [
    {
      required: true,
      validator: validatePass2,
      trigger: "change",
    },
  ],
  userName: [
    {
      required: true,
      validator: validateName,
      trigger: "change",
    },
  ],
  nickName: [
    {
      required: true,
      validator: validateNickName,
      trigger: "change",
    },
  ],
  sex: [
    {
      required: true,
    },
  ],
};

const onSubmit = () => {
  // console.log(formState.birthDate);
  formRef.value.validate().then(() => {
    // console.log(formState);
    registUser(
      formState,
      ({ status }) => {
        if (status === 200) {
          alert("회원가입이 완료되었습니다.");
          router.push({ name: "main-view" });
        }
      },
      (err) => {
        alert("오류가 발생했습니다.");
        console.log(err);
      }
    );
  });
};
</script>

<template>
  <div class="gray-head">
    <h2 class="mt-4 py-3 shadow-sm bg-light text-center">회원가입</h2>
  </div>
  <div class="container">
    <a-form
      class="shadow border bg-body rounded mx-auto mt-5 pt-5 px-5 pb-3"
      ref="formRef"
      :model="formState"
      :label-col="labelCol"
      :wrapper-col="wrapperCol"
      :rules="rules"
      layout="horizontal"
      style="max-width: 600px">
      <a-form-item has-feedback label="아이디" name="userId">
        <a-input v-model:value="formState.userId" autocomplete="off" />
      </a-form-item>

      <a-form-item has-feedback label="비밀번호" name="userPass">
        <a-input
          v-model:value="formState.userPass"
          type="password"
          autocomplete="off" />
      </a-form-item>

      <a-form-item has-feedback label="비밀번호 확인" name="checkPass">
        <a-input
          v-model:value="formState.checkPass"
          type="password"
          autocomplete="off" />
      </a-form-item>

      <a-form-item has-feedback label="이름" name="userName">
        <a-input v-model:value="formState.userName" autocomplete="off" />
      </a-form-item>

      <a-form-item has-feedback label="닉네임" name="nickName">
        <a-input
          v-model:value="formState.nickName"
          autocomplete="off"
          ref="refNn" />
      </a-form-item>

      <a-form-item label="성별" name="sex">
        <a-radio-group v-model:value="formState.sex">
          <a-radio value="male">남자</a-radio>
          <a-radio value="female">여자</a-radio>
        </a-radio-group>
      </a-form-item>

      <a-form-item has-feedback label="생년월일" name="birthDate">
        <a-date-picker
          v-model:value="formState.birthDate"
          value-format="YYYY-MM-DD" />
      </a-form-item>

      <a-form-item label="이메일">
        <a-input-group compat>
          <a-input
            name="emailId"
            v-model:value="formState.emailId"
            style="width: 43%; text-align: center" />
          <a-input
            class="site-input-split"
            style="width: 13%; pointer-events: none; text-align: center"
            placeholder="@"
            disabled />
          <a-input
            name="emailDomain"
            v-model:value="formState.emailDomain"
            style="width: 44%; text-align: center" />
        </a-input-group>
      </a-form-item>

      <a-form-item label="전화번호">
        <a-input-group compact>
          <a-select
            v-model:value="formState.phoneNumber1"
            style="width: 30%; text-align: center">
            <a-select-option value="010">010</a-select-option>
            <a-select-option value="011">011</a-select-option>
            <a-select-option value="016">016</a-select-option>
            <a-select-option value="017">017</a-select-option>
            <a-select-option value="019">019</a-select-option>
          </a-select>
          <a-input-number
            name="phoneNumber2"
            v-model:value="formState.phoneNumber2"
            style="width: 30%; text-align: center" />
          <a-input
            class="site-input-split"
            style="width: 10%; pointer-events: none; text-align: center"
            placeholder="-"
            disabled />
          <a-input-number
            name="phoneNumber3"
            v-model:value="formState.phoneNumber3"
            style="width: 30%; text-align: center" />
        </a-input-group>
      </a-form-item>

      <a-form-item style="text-align: right">
        <a-button size="large" @click="onSubmit">회원가입</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<style scoped></style>
