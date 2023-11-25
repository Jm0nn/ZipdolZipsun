///////////////////////// select box 설정 (지역, 매매기간) /////////////////////////
let date = new Date();

let yearEl = document.querySelector("#year");
let yearOpt = `<option value="">매매년도선택</option>`;
let year = date.getFullYear();
for (let i = year; i > year - 20; i--) {
  yearOpt += `<option value="${i}">${i}년</option>`;
}
yearEl.innerHTML = yearOpt;

// 브라우저가 열리면 시도정보 얻기.
sendRequest("sido", "*00000000");

document.querySelector("#year").addEventListener("change", function () {
  let month = date.getMonth() + 1;
  let monthEl = document.querySelector("#month");
  let monthOpt = `<option value="">매매월선택</option>`;
  let yearSel = document.querySelector("#year");
  let m =
    yearSel[yearSel.selectedIndex].value == date.getFullYear() ? month : 13;
  for (let i = 1; i < m; i++) {
    monthOpt += `<option value="${i < 10 ? "0" + i : i}">${i}월</option>`;
  }
  monthEl.innerHTML = monthOpt;
});

// https://juso.dev/docs/reg-code-api/
// let url = "https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes";
// let regcode = "*00000000";
// 전국 특별/광역시, 도
// https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=*00000000

// 시도가 바뀌면 구군정보 얻기.
document.querySelector("#sido").addEventListener("change", function () {
  if (this[this.selectedIndex].value) {
    let regcode = this[this.selectedIndex].value.substr(0, 2) + "*00000";
    sendRequest("gugun", regcode);
  } else {
    initOption("gugun");
    initOption("dong");
  }
});

// 구군이 바뀌면 동정보 얻기.
document.querySelector("#gugun").addEventListener("change", function () {
  if (this[this.selectedIndex].value) {
    let regcode = this[this.selectedIndex].value.substr(0, 5) + "*";
    sendRequest("dong", regcode);
  } else {
    initOption("dong");
  }
});

function sendRequest(selid, regcode) {
  const url = "https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes";
  let params = "regcode_pattern=" + regcode + "&is_ignore_zero=true";
  fetch(`${url}?${params}`)
    .then((response) => response.json())
    .then((data) => addOption(selid, data));
}

function addOption(selid, data) {
  let opt = ``;
  initOption(selid);
  switch (selid) {
    case "sido":
      opt += `<option value="">시도선택</option>`;
      data.regcodes.forEach(function (regcode) {
        opt += `
          <option value="${regcode.code}">${regcode.name}</option>
          `;
      });
      break;
    case "gugun":
      opt += `<option value="">구군선택</option>`;
      for (let i = 0; i < data.regcodes.length; i++) {
        if (i != data.regcodes.length - 1) {
          if (
            data.regcodes[i].name.split(" ")[1] ==
              data.regcodes[i + 1].name.split(" ")[1] &&
            data.regcodes[i].name.split(" ").length !=
              data.regcodes[i + 1].name.split(" ").length
          ) {
            data.regcodes.splice(i, 1);
            i--;
          }
        }
      }
      let name = "";
      data.regcodes.forEach(function (regcode) {
        if (regcode.name.split(" ").length == 2)
          name = regcode.name.split(" ")[1];
        else
          name = regcode.name.split(" ")[1] + " " + regcode.name.split(" ")[2];
        opt += `
          <option value="${regcode.code}">${name}</option>
          `;
      });
      break;
    case "dong":
      opt += `<option value="">동선택</option>`;
      let idx = 2;
      data.regcodes.forEach(function (regcode) {
        if (regcode.name.split(" ").length != 3) idx = 3;
        opt += `
          <option value="${regcode.code}">${
          regcode.name.split(" ")[idx]
        }</option>
          `;
      });
  }
  document.querySelector(`#${selid}`).innerHTML = opt;
}

function initOption(selid) {
  let options = document.querySelector(`#${selid}`);
  options.length = 0;
  // let len = options.length;
  // for (let i = len - 1; i >= 0; i--) {
  //   options.remove(i);
  // }
}

///////////////////////// 아파트 매매 정보 /////////////////////////
document.querySelector("#list-btn").addEventListener("click", function () {
  let url =
    "http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev";
  let gugunSel = document.querySelector("#gugun");
  let regCode = gugunSel[gugunSel.selectedIndex].value.substr(0, 5);
  let yearSel = document.querySelector("#year");
  let year = yearSel[yearSel.selectedIndex].value;
  let monthSel = document.querySelector("#month");
  let month = monthSel[monthSel.selectedIndex].value;
  let dealYM = year + month;
  let queryParams =
    encodeURIComponent("serviceKey") + "=" + serviceKey; /*Service Key*/
  queryParams +=
    "&" +
    encodeURIComponent("LAWD_CD") +
    "=" +
    encodeURIComponent(regCode); /*아파트소재 구군*/
  queryParams +=
    "&" +
    encodeURIComponent("DEAL_YMD") +
    "=" +
    encodeURIComponent(dealYM); /*조회년월*/
  queryParams +=
    "&" +
    encodeURIComponent("pageNo") +
    "=" +
    encodeURIComponent("1"); /*페이지번호*/
  queryParams +=
    "&" +
    encodeURIComponent("numOfRows") +
    "=" +
    encodeURIComponent("10000"); /*페이지당건수*/

  fetch(`${url}?${queryParams}`)
    .then((response) => response.text())
    .then((data) => makeAptList(data));
});

function makeAptList(data) {
  document.querySelector("table").setAttribute("style", "display: ;");
  let thead = document.querySelector("#apttitle");
  let tbody = document.querySelector("#aptlist");
  let parser = new DOMParser();
  const xml = parser.parseFromString(data, "application/xml");
  // console.log(xml);

  initTable();

  let tr = document.createElement("tr");
  let nametitle = document.createElement("th");
  nametitle.appendChild(document.createTextNode("아파트이름"));

  let dongtitle = document.createElement("th");
  dongtitle.appendChild(document.createTextNode("법정동"));

  tr.appendChild(nametitle);
  tr.appendChild(dongtitle);

  thead.appendChild(tr);

  let uniqueApt = new Set();

  let apts = xml.querySelectorAll("item");

  apts.forEach((apt) => {
    let aptname = apt.querySelector("아파트").textContent;

    if (!uniqueApt.has(aptname)) {
      let tr = document.createElement("tr");

      let nameTd = document.createElement("td");
      nameTd.appendChild(
        document.createTextNode(apt.querySelector("아파트").textContent)
      );
      tr.appendChild(nameTd);

      // let floorTd = document.createElement("td");
      // floorTd.appendChild(
      //   document.createTextNode(apt.querySelector("층").textContent)
      // );
      // tr.appendChild(floorTd);

      // let areaTd = document.createElement("td");
      // areaTd.appendChild(
      //   document.createTextNode(apt.querySelector("전용면적").textContent)
      // );
      // tr.appendChild(areaTd);

      let dongTd = document.createElement("td");
      dongTd.appendChild(
        document.createTextNode(apt.querySelector("법정동").textContent)
      );
      tr.appendChild(dongTd);

      // let priceTd = document.createElement("td");
      // priceTd.appendChild(
      //   document.createTextNode(
      //     apt.querySelector("거래금액").textContent + "만원"
      //   )
      // );
      // priceTd.classList.add("text-end");
      // tr.appendChild(priceTd);

      let address =
        apt.querySelector("중개사소재지").textContent +
        " " +
        apt.querySelector("법정동").textContent +
        " " +
        apt.querySelector("지번").textContent;

      tr.addEventListener("click", () => {
        makeList(data, apt.querySelector("아파트").textContent);
        viewAptMap(apt.querySelector("아파트").textContent, address);
      });

      tbody.appendChild(tr);

      uniqueApt.add(aptname);
    }
  });
}

function makeList(data, aptname) {
  document.querySelector("table").setAttribute("style", "display: ;");
  let thead = document.querySelector("#apttitle");
  let tbody = document.querySelector("#aptlist");
  let parser = new DOMParser();
  const xml = parser.parseFromString(data, "application/xml");
  // console.log(xml);

  initTable();

  let tr = document.createElement("tr");

  let nametitle = document.createElement("th");
  nametitle.appendChild(document.createTextNode("아파트이름"));
  tr.appendChild(nametitle);

  let floortitle = document.createElement("th");
  floortitle.appendChild(document.createTextNode("층"));
  tr.appendChild(floortitle);

  let areatitle = document.createElement("th");
  areatitle.appendChild(document.createTextNode("면적"));
  tr.appendChild(areatitle);

  let dongtitle = document.createElement("th");
  dongtitle.appendChild(document.createTextNode("법정동"));
  tr.appendChild(dongtitle);

  let pricetitle = document.createElement("th");
  pricetitle.appendChild(document.createTextNode("거래금액"));
  tr.appendChild(pricetitle);

  thead.appendChild(tr);

  let apts = xml.querySelectorAll("item");

  apts.forEach((apt) => {
    if (aptname === apt.querySelector("아파트").textContent) {
      let tr = document.createElement("tr");

      let nameTd = document.createElement("td");
      nameTd.appendChild(
        document.createTextNode(apt.querySelector("아파트").textContent)
      );
      tr.appendChild(nameTd);

      let floorTd = document.createElement("td");
      floorTd.appendChild(
        document.createTextNode(apt.querySelector("층").textContent)
      );
      tr.appendChild(floorTd);

      let areaTd = document.createElement("td");
      areaTd.appendChild(
        document.createTextNode(apt.querySelector("전용면적").textContent)
      );
      tr.appendChild(areaTd);

      let dongTd = document.createElement("td");
      dongTd.appendChild(
        document.createTextNode(apt.querySelector("법정동").textContent)
      );
      tr.appendChild(dongTd);

      let priceTd = document.createElement("td");
      priceTd.appendChild(
        document.createTextNode(
          apt.querySelector("거래금액").textContent + "만원"
        )
      );
      priceTd.classList.add("text-end");
      tr.appendChild(priceTd);

      let address =
        apt.querySelector("중개사소재지").textContent +
        " " +
        apt.querySelector("법정동").textContent +
        " " +
        apt.querySelector("지번").textContent;

      tr.addEventListener("click", () =>
        viewMap(
          apt.querySelector("아파트").textContent,
          address,
          apt.querySelector("층").textContent,
          apt.querySelector("전용면적").textContent,
          apt.querySelector("거래금액").textContent
        )
      );

      tbody.appendChild(tr);
    }
  });
}

// 카카오지도
var mapContainer = document.getElementById("map"), // 지도를 표시할 div
  mapOption = {
    center: new kakao.maps.LatLng(37.500613, 127.036431), // 지도의 중심좌표
    level: 3, // 지도의 확대 레벨
  };

// 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
var map = new kakao.maps.Map(mapContainer, mapOption);

var marker, infowindow;
function viewMap(apt, address, floor, area, priceTd) {
  if (marker != null && infowindow != null) {
    marker.setMap(null);
    infowindow.close();
  }
  // 주소-좌표 변환 객체를 생성합니다
  var geocoder = new kakao.maps.services.Geocoder();

  // 주소로 좌표를 검색합니다
  geocoder.addressSearch(address, function (result, status) {
    // 정상적으로 검색이 완료됐으면
    if (status === kakao.maps.services.Status.OK) {
      var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

      // 결과값으로 받은 위치를 마커로 표시합니다
      marker = new kakao.maps.Marker({
        map: map,
        position: coords,
      });

      // 인포윈도우로 장소에 대한 설명을 표시합니다
      infowindow = new kakao.maps.InfoWindow({
        content: `<div style="width:150px;text-align:center;padding:6px 0;">${apt}</div>
        <div style="width:150px;text-align:center;padding:6px 0;">${floor}층</div>
        <div style="width:150px;text-align:center;padding:6px 0;">${area}m<sup>2</sup></div>
        <div style="width:150px;text-align:center;padding:6px 0;">${priceTd}만원</div>`,
      });
      infowindow.open(map, marker);

      // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
      map.setCenter(coords);
    }
  });
}

function viewAptMap(apt, address) {
  if (marker != null && infowindow != null) {
    marker.setMap(null);
    infowindow.close();
  }
  // 주소-좌표 변환 객체를 생성합니다
  var geocoder = new kakao.maps.services.Geocoder();

  // 주소로 좌표를 검색합니다
  geocoder.addressSearch(address, function (result, status) {
    // 정상적으로 검색이 완료됐으면
    if (status === kakao.maps.services.Status.OK) {
      var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

      // 결과값으로 받은 위치를 마커로 표시합니다
      marker = new kakao.maps.Marker({
        map: map,
        position: coords,
      });

      // 인포윈도우로 장소에 대한 설명을 표시합니다
      infowindow = new kakao.maps.InfoWindow({
        content: `<div style="width:150px;text-align:center;padding:6px 0;">${apt}</div>`,
      });
      infowindow.open(map, marker);

      // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
      map.setCenter(coords);
    }
  });
}

function initTable() {
  let thead = document.querySelector("#apttitle");
  let len = thead.rows.length;
  for (let i = len - 1; i >= 0; i--) {
    thead.deleteRow(i);
  }

  let tbody = document.querySelector("#aptlist");
  len = tbody.rows.length;
  for (let i = len - 1; i >= 0; i--) {
    tbody.deleteRow(i);
  }
}
