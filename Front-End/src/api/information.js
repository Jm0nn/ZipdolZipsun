import {
  localAxios,
  naverNewsAxios,
  naverApiAxios,
  nkisAxios,
} from "@/utils/http-commons";

const local = localAxios();
const naverNews = naverNewsAxios();
const naverApi = naverApiAxios();
const nkis = nkisAxios();

const encoded = () => {
  return encodeURI("부동산");
};

// 뉴스 리스트
export const getNewsList = (date, success, fail) => {
  local.get(`/info/news/${date}`).then(success).catch(fail);
};

// 블로그 리스트
export const getBlogList = (pgno, success, fail) => {
  local.get(`/info/blog/${pgno}`).then(success).catch(fail);
};

// 연구 보고서
export const getResearch = (pgno, success, fail) => {
  local.get(`/info/research/${pgno}`).then(success).catch(fail);
};
