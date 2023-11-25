import { localAxios, zigbangAxios } from "@/utils/http-commons";

const local = localAxios();
const zigbang = zigbangAxios();

// 주소 리스트 요청
const getList = (name, success, fail) => {
  local.get(`/region/list/${name}`).then(success).catch(fail);
};

// 해당 주소 위도,경도 요청
const getPoint = (code, success, fail) => {
  zigbang.get(`/apt/locals/${code}`).then(success).catch(fail);
};

// geohash 요청
const getGeoHash = (point, hashLevel, success, fail) => {
  local
    .get(`/region/geohash/${point.lat}/${point.lng}/${hashLevel}`)
    .then(success)
    .catch(fail);
};

// 아파트 리스트 요청
const getAptList = (geoHash, success, fail) => {
  zigbang
    .get(`/property/apartments/location/v3?e=&geohash=${geoHash}`)
    .then(success)
    .catch(fail);
};

// 아파트 정보 요청
const getAptInfo = (id, success, fail) => {
  zigbang.get(`/apt/danjis/${id}`).then(success).catch(fail);
};

// 동 리스트 요청
const getDongList = (geoHash, success, fail) => {
  zigbang
    .get(
      `v2/local/price?geohash=${geoHash}&local_level=3&period=3&transaction_type_eq=s`
    )
    .then(success)
    .catch(fail);
};

const getDongPriceList = (code, success, fail) => {
  zigbang
    .get(`/apt/locals/trading-volume-chart?localCd=${code}`)
    .then(success)
    .catch(fail);
};

const getAptPriceList = (id, success, fail) => {
  zigbang
    .get(`https://apis.zigbang.com/apt/danjis/${id}/price-chart?period=3y`)
    .then(success)
    .catch(fail);
};

const getRoomType = (id, success, fail) => {
  zigbang.get(`/apt/danjis/${id}/room_types`).then(success).catch(fail);
};

const getRanking = (code, success, fail) => {
  zigbang
    .get(
      `/apt/locals/danji/ranking?page=1&limit=10&bjdCode=${code}&viewPeriod=this_month&sortType=volume_desc&tranType=trade`
    )
    .then(success)
    .catch(fail);
};

const rtDeal = (id, success, fail) => {
  zigbang
    .get(`/apt/danjis/${id}/rt-prices?dealType=sales&offset=0&limit=5`)
    .then(success)
    .catch(fail);
};

export {
  getList,
  getPoint,
  getGeoHash,
  getAptList,
  getAptInfo,
  getDongList,
  getDongPriceList,
  getAptPriceList,
  getRoomType,
  getRanking,
  rtDeal,
};
