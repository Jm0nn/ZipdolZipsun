import { localAxios } from "@/utils/http-commons";

const local = localAxios();

export const getUsers = (success, fail) => {
  local.get(`/admin`).then(success).catch(fail);
};

export const delUser = (userId, success, fail) => {
  local.delete(`/admin/${userId}`).then(success).catch(fail);
};

export const putUser = (user, success, fail) => {
  local.put(`/admin`, JSON.stringify(user)).then(success).catch(fail);
};

export const chkId = (userId, success, fail) => {
  local.get(`/member/check/${userId}`).then(success).catch(fail);
};

export const postUser = (user, success, fail) => {
  local.post(`/admin`, JSON.stringify(user)).then(success).catch(fail);
};

export const getUser = (userId, success, fail) => {
  local.get(`/admin/${userId}`).then(success).catch(fail);
};

export const chkNickName = (nickName, success, fail) => {
  local.get(`/user/nickname/${nickName}`).then(success).catch(fail);
};
