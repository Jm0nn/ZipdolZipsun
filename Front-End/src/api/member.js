import { localAxios } from "@/utils/http-commons";

const local = localAxios();

export const postUser = (user, success, fail) => {
  local.post(`/user/login`, JSON.stringify(user)).then(success).catch(fail);
};

export const chkId = (userId, success, fail) => {
  local.get(`/user/id/${userId}`).then(success).catch(fail);
};

export const registUser = (user, success, fail) => {
  local.post(`/user/regist`, JSON.stringify(user)).then(success).catch(fail);
};

export const getPass = (id, success, fail) => {
  local.get(`/user/find/${id}`).then(success).catch(fail);
};

export const putUser = (user, success, fail) => {
  local.put(`/user/modify`, JSON.stringify(user)).then(success).catch(fail);
};

export const delUser = (userId, success, fail) => {
  local.delete(`/user/${userId}`).then(success).catch(fail);
};

export const chkNickName = (nickName, success, fail) => {
  local.get(`/user/nickname/${nickName}`).then(success).catch(fail);
};

export const getUser = (userNo, success, fail) => {
  local.get(`/user/info/${userNo}`).then(success).catch(fail);
};

export const getUsers = (success, fail) => {
  local.get(`/user`).then(success).catch(fail);
};
