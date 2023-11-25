import { localAxios } from "@/utils/http-commons";

const local = localAxios();

// 전체 리스트 출력(게시판)
export const getArticles = (boardNo, success, fail) => {
  local.get(`/board/list/${boardNo}`).then(success).catch(fail);
};

// 전체 리스트 출력(공지)
export const getAnnounce = (success, fail) => {
  local.get(`/board/announcelist`).then(success).catch(fail);
};

//상세 페이지 들어가기
export const getArticle = (boardNo, articleNo, success, fail) => {
  local.get(`/board/${boardNo}/${articleNo}`).then(success).catch(fail);
};

// 글 수정
export const putArticle = (article, boardNo, articleNo, success, fail) => {
  local
    .put(`/board/modify/${boardNo}/${articleNo}`, JSON.stringify(article))
    .then(success)
    .catch(fail);
};

// 글 삭제
export const delArticle = (boardNo, articleNo, success, fail) => {
  local
    .delete(`/board/delete/${boardNo}/${articleNo}`)
    .then(success)
    .catch(fail);
};

// 글 작성
export const postArticle = (article, boardNo, success, fail) => {
  local
    .post(`/board/write/${boardNo}`, JSON.stringify(article))
    .then(success)
    .catch(fail);
};

// 조회수 증가
export const updateHit = (articleNo, success, fail) => {
  local.put(`/board/hit/${articleNo}`).then(success).catch(fail);
};

export const searchArticles = (search, success, fail) => {
  local.get(`/board/search`, { params: search }).then(success).catch(fail);
};
