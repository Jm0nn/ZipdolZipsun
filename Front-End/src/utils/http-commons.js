import axios from "axios";

const {
  VITE_API_BASE_URL,
  VITE_ZIGBANG_BASE_URL,
  VITE_NAVER_NEWS_BASE_URL,
  VITE_NAVER_AIP_BASE_URL,
  VITE_NKIS_BASE_URL,
  VITE_NAVER_SERVICE_CLIENT_ID,
  VITE_NAVER_SERVICE_CLIENT_SECRET,
} = import.meta.env;

function naverApiAxios() {
  const instance = axios.create({
    baseURL: VITE_NAVER_AIP_BASE_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
      "X-Naver-Client-Id": VITE_NAVER_SERVICE_CLIENT_ID,
      "X-Naver-Client-Secret": VITE_NAVER_SERVICE_CLIENT_SECRET,
    },
  });
  return instance;
}

function nkisAxios() {
  const instance = axios.create({
    baseURL: VITE_NKIS_BASE_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

function naverNewsAxios() {
  const instance = axios.create({
    baseURL: VITE_NAVER_NEWS_BASE_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

function zigbangAxios() {
  const instance = axios.create({
    baseURL: VITE_ZIGBANG_BASE_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

function localAxios() {
  const instance = axios.create({
    baseURL: VITE_API_BASE_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });

  return instance;
}

export { localAxios, zigbangAxios, naverNewsAxios, naverApiAxios, nkisAxios };
