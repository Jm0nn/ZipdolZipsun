import { localAxios } from "@/utils/http-commons";

const local = localAxios();

// 댓글 리스트 출력
export const getComments = (articleNo, success, fail) => {
  local.get(`/comment/${articleNo}`).then(success).catch(fail);
};

// 댓글 수정
export const putComments = (comment, success, fail) => {
  local.put(`/comment`, JSON.stringify(comment)).then(success).catch(fail);
};

// 댓글 삭제
export const delComments = (articleNo, commentNo, success, fail) => {
  local
    .delete(`/comment/delete/${articleNo}/${commentNo}`)
    .then(success)
    .catch(fail);
};

// 댓글 작성
export const postComment = (comment, success, fail) => {
  local.post(`/comment`, JSON.stringify(comment)).then(success).catch(fail);
};
