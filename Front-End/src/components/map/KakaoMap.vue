<script setup>
import { ref, watch, onMounted, reactive } from "vue";
import {
  getList,
  getPoint,
  getGeoHash,
  getAptList,
  getAptInfo,
  getDongList,
} from "@/api/map";

const emit = defineEmits(["sendAptInfo", "sendDongInfo"]);

const dong = ref("");
const list = ref([]);

const geoHashes = reactive({
  values: [],
});

const aptList = reactive({
  values: [],
});

const dongList = reactive({
  values: [],
});

const centerPoint = ref({
  lat: 0,
  lng: 0,
});

const positions = ref([]);
const markers = ref([]);

var mks = [];

// 마커를 클릭했을 때 해당 장소의 상세정보를 보여줄 커스텀오버레이입니다
var placeOverlay = null,
  contentNode = null, // 커스텀 오버레이의 컨텐츠 엘리먼트 입니다
  // markers = [], // 마커를 담을 배열입니다
  currCategory = ""; // 현재 선택된 카테고리를 가지고 있을 변수입니다

var category = null,
  children = null;

var ps = null;

var debounce = null;

var map;

onMounted(() => {
  if (window.kakao && window.kakao.maps) {
    initMap();
  } else {
    const script = document.createElement("script");
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${
      import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY
    }&libraries=services,clusterer`;
    /* global kakao */
    script.onload = () => kakao.maps.load(() => initMap());
    document.head.appendChild(script);
  }

  document.querySelector("body").addEventListener("click", function (e) {
    if (e.target != e.currentTarget.querySelector(".find")) {
      clear();
    }
  });
});

watch(dong, () => {
  if (dong.value.length === 0) {
    list.value = [];
  }
});

// 지도 중심좌표 감시
watch(
  () => centerPoint.value,
  () => {
    var moveLatLon = new kakao.maps.LatLng(
      centerPoint.value.lat,
      centerPoint.value.lng
    );
    map.panTo(moveLatLon);
    map.setLevel(3);
  },
  { deep: true }
);

// 지도 내 geoHash 값 변화 감지
watch(
  () => geoHashes.values,
  () => {
    var level = map.getLevel();

    clearTimeout(debounce);
    debounce = setTimeout(() => {
      aptList.values = [];
      dongList.values = [];

      for (let i = 0; i < geoHashes.values.length; i++) {
        let geoHash = geoHashes.values[i];

        if (level < 5) {
          getAptList(geoHash, ({ status, data }) => {
            if (status === 200) {
              // console.log(data.filtered);

              let apts = data.filtered;

              for (let apt of apts) {
                aptList.values.push(apt);
              }
            }
          }),
            (err) => console.log(err);
        } else {
          getDongList(geoHash, ({ status, data }) => {
            if (status === 200) {
              let dongs = data.datas;

              for (let dong of dongs) {
                dongList.values.push(dong);
              }
            }
          });
        }
      }
    }, 50);
  },
  { deep: true }
);

// 아파트 리스트 감시
watch(
  () => aptList.values,
  () => {
    clearTimeout(debounce);
    debounce = setTimeout(() => {
      positions.value = [];
      aptList.values.forEach((apt) => {
        let obj = {};
        obj.latlng = new kakao.maps.LatLng(apt.lat, apt.lng);
        obj.title = apt.name;
        obj.id = apt.id;

        positions.value.push(obj);
      });
      loadMarkers();
    }, 50);
  },
  { deep: true }
);

watch(
  () => dongList.values,
  () => {
    clearTimeout(debounce);
    debounce = setTimeout(() => {
      positions.value = [];
      dongList.values.forEach((dong) => {
        let obj = {};
        obj.latlng = new kakao.maps.LatLng(dong.lat, dong.lng);
        obj.title = dong.name;
        obj.id = dong.id;
        obj.info = dong;

        positions.value.push(obj);
      });
      loadMarkers();
    }, 50);
  },
  { deep: true }
);

// 지도 init
const initMap = () => {
  const container = document.getElementById("map");
  const options = {
    center: new kakao.maps.LatLng(37.56681969667271, 126.97864660058093),
    level: 3,
  };
  map = new kakao.maps.Map(container, options);
  map.setMaxLevel(7);

  getPoints();

  // 지도 이동 이벤트
  kakao.maps.event.addListener(map, "center_changed", () => {
    clearTimeout(debounce);
    debounce = setTimeout(() => {
      getPoints();
    }, 100);
  });

  category = document.getElementById("category");
  children = category.children;
  placeOverlay = new kakao.maps.CustomOverlay({ zIndex: 5 });
  contentNode = document.createElement("div");

  ps = new kakao.maps.services.Places(map);

  // 지도에 idle 이벤트를 등록합니다
  kakao.maps.event.addListener(map, "idle", searchPlaces);

  // 커스텀 오버레이의 컨텐츠 노드에 css class를 추가합니다
  contentNode.className = "placeinfo_wrap";

  // 커스텀 오버레이의 컨텐츠 노드에 mousedown, touchstart 이벤트가 발생했을때
  // 지도 객체에 이벤트가 전달되지 않도록 이벤트 핸들러로 kakao.maps.event.preventMap 메소드를 등록합니다
  addEventHandle(contentNode, "mousedown", kakao.maps.event.preventMap);
  addEventHandle(contentNode, "touchstart", kakao.maps.event.preventMap);

  // 커스텀 오버레이 컨텐츠를 설정합니다
  placeOverlay.setContent(contentNode);

  // 각 카테고리에 클릭 이벤트를 등록합니다
  addCategoryClickEvent();
};

const getPoints = () => {
  var level = map.getLevel();

  // 지도의 중심좌표를 얻어옵니다
  var latlng = map.getCenter();

  // 지도의 현재 영역을 얻어옵니다
  var bounds = map.getBounds();

  // 영역의 남서쪽 좌표를 얻어옵니다
  var swLatLng = bounds.getSouthWest();

  // 영역의 북동쪽 좌표를 얻어옵니다
  var neLatLng = bounds.getNorthEast();

  let swLat = swLatLng.getLat();
  let swLng = swLatLng.getLng();
  let neLat = neLatLng.getLat();
  let neLng = neLatLng.getLng();

  let ctLat = latlng.getLat();
  let ctLng = latlng.getLng();

  const points = [
    {
      lat: swLat,
      lng: swLng,
    },
    {
      lat: swLat,
      lng: ctLng,
    },
    {
      lat: swLat,
      lng: neLng,
    },
    {
      lat: ctLat,
      lng: swLng,
    },
    {
      lat: ctLat,
      lng: ctLng,
    },
    {
      lat: ctLat,
      lng: neLng,
    },
    {
      lat: neLat,
      lng: swLng,
    },
    {
      lat: neLat,
      lng: ctLng,
    },
    {
      lat: neLat,
      lng: neLng,
    },
  ];

  geoHashes.values = [];

  let hashLevel = 4;

  if (level < 5) {
    hashLevel = 5;
  }

  for (let i = 0; i < points.length; i++) {
    getGeoHash(
      points[i],
      hashLevel,
      ({ status, data }) => {
        if (status === 200) {
          if (!geoHashes.values.includes(data)) {
            geoHashes.values.push(data);
          }
        }
      },
      (err) => console.log(err)
    );
  }
};

// 주소 리스트 검색
const call = (e) => {
  clearTimeout(debounce);
  dong.value = e.target.value;

  const success = (response) => {
    if (response.status === 200) {
      list.value = response.data;
      // console.log(list.value);
    }
  };

  const fail = (error) => console.log(error);

  if (dong.value.length !== 0) {
    debounce = setTimeout(() => {
      getList(dong.value, success, fail);
    }, 500);
  }
};

const enter = (e) => {
  clearTimeout(debounce);
  dong.value = e.target.value;

  if (dong.value.length !== 0) {
    getList(
      dong.value,
      (response) => {
        if (response.status === 200) {
          list.value = response.data;
          // console.log(list.value);
        }
      },
      (error) => console.log(error)
    );
  }
};

const clear = () => {
  list.value = [];
};

// 해당 주소로 지도 이동
const move = (code) => {
  list.value.forEach((item) => {
    if (item.regionCode === code) {
      // console.log(code);

      getPoint(
        code,
        (resp) => {
          if (resp.status === 200) {
            centerPoint.value.lat = resp.data.lat;
            centerPoint.value.lng = resp.data.lng;
            // console.log(centerPoint.value);
          }
        },
        (error) => console.log(error)
      );

      dong.value = "";
      list.value = [];

      emit("sendAptInfo", {});
      emit("sendDongInfo", {});

      return;
    }
  });
};

// 마커 불러오기
const loadMarkers = () => {
  var level = map.getLevel();

  // 현재 표시되어있는 marker들이 있다면 map에 등록된 marker를 제거한다.
  deleteMarkers();

  // 마커 이미지를 생성합니다
  const imgSrc = "src/assets/pin.png";
  // 마커 이미지의 이미지 크기 입니다
  const imgSize = new kakao.maps.Size(35, 35);
  const markerImage = new kakao.maps.MarkerImage(imgSrc, imgSize);

  // 마커를 생성합니다
  markers.value = [];
  positions.value.forEach((position) => {
    const marker = new kakao.maps.Marker({
      map: map, // 마커를 표시할 지도
      position: position.latlng, // 마커를 표시할 위치
      title: position.title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됨.
      clickable: true, // // 마커를 클릭했을 때 지도의 클릭 이벤트가 발생하지 않도록 설정합니다
      image: markerImage, // 마커의 이미지
    });

    kakao.maps.event.addListener(marker, "click", () => {
      map.panTo(position.latlng);
      if (level < 5) {
        getAptInfo(
          position.id,
          ({ status, data }) => {
            if (status === 200) {
              // console.log("aptInfo: ", apt.value);
              emit("sendAptInfo", data);
            }
          },
          (err) => console.log(err)
        );
      } else {
        // console.log("dongInfo: ", position.title, position.info);
        emit("sendDongInfo", position.info);
      }
    });

    markers.value.push(marker);
  });
};

// 마커 제거
const deleteMarkers = () => {
  if (markers.value.length > 0) {
    markers.value.forEach((marker) => marker.setMap(null));
  }
};

// 엘리먼트에 이벤트 핸들러를 등록하는 함수입니다
function addEventHandle(target, type, callback) {
  if (target.addEventListener) {
    target.addEventListener(type, callback);
  } else {
    target.attachEvent("on" + type, callback);
  }
}

// 카테고리 검색을 요청하는 함수입니다
function searchPlaces() {
  if (!currCategory) {
    return;
  }

  // 커스텀 오버레이를 숨깁니다
  placeOverlay.setMap(null);

  // 지도에 표시되고 있는 마커를 제거합니다
  removeMarker();

  ps.categorySearch(currCategory, placesSearchCB, { useMapBounds: true });
}

// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
function placesSearchCB(data, status, pagination) {
  if (status === kakao.maps.services.Status.OK) {
    // 정상적으로 검색이 완료됐으면 지도에 마커를 표출합니다
    displayPlaces(data);
  } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
    // 검색결과가 없는경우 해야할 처리가 있다면 이곳에 작성해 주세요
  } else if (status === kakao.maps.services.Status.ERROR) {
    // 에러로 인해 검색결과가 나오지 않은 경우 해야할 처리가 있다면 이곳에 작성해 주세요
  }
}

// 지도에 마커를 표출하는 함수입니다
function displayPlaces(places) {
  // 몇번째 카테고리가 선택되어 있는지 얻어옵니다
  // 이 순서는 스프라이트 이미지에서의 위치를 계산하는데 사용됩니다
  var order = document.getElementById(currCategory).getAttribute("data-order");

  for (var i = 0; i < places.length; i++) {
    // 마커를 생성하고 지도에 표시합니다
    var marker = addMarker(
      new kakao.maps.LatLng(places[i].y, places[i].x),
      order
    );

    // 마커와 검색결과 항목을 클릭 했을 때
    // 장소정보를 표출하도록 클릭 이벤트를 등록합니다
    (function (marker, place) {
      kakao.maps.event.addListener(marker, "click", function () {
        displayPlaceInfo(place);
      });
    })(marker, places[i]);
  }
}

// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
function addMarker(position, order) {
  var imageSrc =
      "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_category.png", // 마커 이미지 url, 스프라이트 이미지를 씁니다
    imageSize = new kakao.maps.Size(27, 28), // 마커 이미지의 크기
    imgOptions = {
      spriteSize: new kakao.maps.Size(72, 208), // 스프라이트 이미지의 크기
      spriteOrigin: new kakao.maps.Point(46, order * 36), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
      offset: new kakao.maps.Point(11, 28), // 마커 좌표에 일치시킬 이미지 내에서의 좌표
    },
    markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
    marker = new kakao.maps.Marker({
      position: position, // 마커의 위치
      image: markerImage,
    });

  marker.setMap(map); // 지도 위에 마커를 표출합니다
  mks.push(marker); // 배열에 생성된 마커를 추가합니다

  return marker;
}

// 지도 위에 표시되고 있는 마커를 모두 제거합니다
function removeMarker() {
  for (var i = 0; i < mks.length; i++) {
    mks[i].setMap(null);
  }
  mks = [];
}

// 클릭한 마커에 대한 장소 상세정보를 커스텀 오버레이로 표시하는 함수입니다
function displayPlaceInfo(place) {
  var content =
    '<div class="placeinfo">' +
    '   <a class="title" href="' +
    place.place_url +
    '" target="_blank" title="' +
    place.place_name +
    '">' +
    place.place_name +
    "</a>";

  if (place.road_address_name) {
    content +=
      '    <span title="' +
      place.road_address_name +
      '">' +
      place.road_address_name +
      "</span>" +
      '  <span class="jibun" title="' +
      place.address_name +
      '">(지번 : ' +
      place.address_name +
      ")</span>";
  } else {
    content +=
      '    <span title="' +
      place.address_name +
      '">' +
      place.address_name +
      "</span>";
  }

  content +=
    '    <span class="tel">' +
    place.phone +
    "</span>" +
    "</div>" +
    '<div class="after"></div>';

  contentNode.innerHTML = content;
  placeOverlay.setPosition(new kakao.maps.LatLng(place.y, place.x));
  placeOverlay.setMap(map);
}

// 각 카테고리에 클릭 이벤트를 등록합니다
function addCategoryClickEvent() {
  for (var i = 0; i < children.length; i++) {
    children[i].onclick = onClickCategory;
  }
}

// 카테고리를 클릭했을 때 호출되는 함수입니다
function onClickCategory() {
  var id = this.id,
    className = this.className;

  placeOverlay.setMap(null);

  if (className === "on") {
    currCategory = "";
    changeCategoryClass();
    removeMarker();
  } else {
    currCategory = id;
    changeCategoryClass(this);
    searchPlaces();
  }
}

// 클릭된 카테고리에만 클릭된 스타일을 적용하는 함수입니다
function changeCategoryClass(el) {
  var i;

  for (i = 0; i < children.length; i++) {
    children[i].className = "";
  }

  if (el) {
    el.className = "on";
  }
}
</script>

<template>
  <div class="map_wrap" style="z-index: 2">
    <div id="map"></div>
    <div class="bg" style="z-index: 3"></div>
    <div class="map_wrap2" style="z-index: 4">
      <input
        id="find"
        type="text"
        :value="dong"
        placeholder="지역 검색"
        @keyup="call"
        @keyup.enter="enter"
        @click="enter" />
      <ul class="category2" v-if="list.length > 0">
        <li
          v-for="addr in list"
          :key="addr.regionCode"
          @click="move(addr.regionCode)">
          <span>{{ addr.addressName }}</span>
        </li>
      </ul>
    </div>

    <ul id="category">
      <li id="BK9" data-order="0">
        <span class="category_bg bank"></span>
        은행
      </li>
      <li id="MT1" data-order="1">
        <span class="category_bg mart"></span>
        마트
      </li>
      <li id="PM9" data-order="2">
        <span class="category_bg pharmacy"></span>
        약국
      </li>
      <li id="OL7" data-order="3">
        <span class="category_bg oil"></span>
        주유소
      </li>
      <li id="CE7" data-order="4">
        <span class="category_bg cafe"></span>
        카페
      </li>
      <li id="CS2" data-order="5">
        <span class="category_bg store"></span>
        편의점
      </li>
    </ul>
  </div>
</template>

<style scoped>
#map {
  position: absolute;
  top: 0;
  width: 100vw;
  height: 100vh;
}

.bg {
  background-color: #f4f5f6;
  width: 322px;
  height: 128px;
  position: absolute;
  top: 90px;
  left: 30px;
  border: 1px solid #909090;
  border-radius: 10px;
}

.map_wrap2 {
  position: absolute;
  top: 167px;
  left: 40px;
  width: 185px;
}

#find {
  width: 302px;
  height: 40px;
  font-size: 15px;
  border: 1px solid #909090;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.4);

  border-radius: 1px;
  outline: none;
  padding-left: 10px;
}

.category2 {
  position: absolute;
  top: 40px;
  width: 302px;
  max-height: 300px;
  font-size: 15px;
  border-radius: 1px;
  border: 1px solid #909090;
  border-top: 0;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.4);
  background: #fff;
  overflow: auto;
  z-index: 4;
  -ms-overflow-style: none;
}
.category2::-webkit-scrollbar {
  width: 5px;
}
.category2::-webkit-scrollbar-thumb {
  background-color: #aaaaaa;
  border-radius: 5px;
}
.category2::-webkit-scrollbar-track {
  background-color: #dddddd;
  border-radius: 5px;
}
.category2 li {
  float: center;
  list-style: none;
  /* border-right: 1px solid #acacac; */
  padding: 0px 0;
  text-align: center;
  cursor: pointer;
}
.category2 li.on {
  background: #eee;
}
.category2 li:hover {
  background: #ffe6e6;
  /* border-left: 1px solid #acacac;
  margin-left: -1px; */
}
.category2 li:last-child {
  margin-right: 0;
  border-right: 0;
}
.category2 li span {
  display: flex;
  border-bottom: 1px solid #dddddd;
  justify-content: center;
  align-items: center;
  padding: 3px 0;
  height: 34px;
}

.map_wrap,
.map_wrap * {
  margin: 0;
  padding: 0;
  font-family: "Malgun Gothic", dotum, "돋움", sans-serif;
}

#category {
  position: absolute;
  top: 100px;
  left: 40px;
  font-size: 12px;
  border-radius: 2px;
  border: 1px solid #909090;

  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.4);
  background: #fff;
  overflow: hidden;
  z-index: 4;
}
#category li {
  float: left;
  list-style: none;
  width: 50px;
  border-right: 1px solid #acacac;
  padding: 6px 0;
  text-align: center;
  cursor: pointer;
}
#category li.on {
  background: #eee;
}
#category li:hover {
  background: #ffe6e6;
  /* border-left: 1px solid #acacac;
  margin-left: -1px; */
}
#category li:last-child {
  margin-right: 0;
  border-right: 0;
}
#category li span {
  display: block;
  margin: 0 auto 3px;
  width: 27px;
  height: 28px;
}
#category li .category_bg {
  background: url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_category.png)
    no-repeat;
}
#category li .bank {
  background-position: -10px 0;
}
#category li .mart {
  background-position: -10px -36px;
}
#category li .pharmacy {
  background-position: -10px -72px;
}
#category li .oil {
  background-position: -10px -108px;
}
#category li .cafe {
  background-position: -10px -144px;
}
#category li .store {
  background-position: -10px -180px;
}
#category li.on .category_bg {
  background-position-x: -46px;
}
</style>
