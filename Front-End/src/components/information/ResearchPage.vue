<script setup>
import { ref, onMounted } from "vue";
import { getResearch } from "@/api/information";

const researchList = ref([]);

const pgno = ref(1);

let parser = new DOMParser();

onMounted(() => {
  getResearchList();
});

const prev = () => {
  if (pgno.value > 1) {
    pgno.value--;
    getResearchList();
  }
};

const next = () => {
  pgno.value++;
  getResearchList();
};

const getResearchList = () => {
  
  getResearch(pgno.value, ({ status, data }) => {
    if (status === 200) {
      researchList.value = [];
      let obj = xmlToJson(parser.parseFromString(data, "text/html"));
      researchList.value = obj.HTML.BODY.ROOT.RESULTS.RESULT;
      // console.log(researchList.value);
    }
  });
};

const oepnResearch = (id) => {
  for (let research of researchList.value) {
    if (id === research.OTP_ID) {
      let url = research.ORG_LINK_POPUP;
      let name = research.OTP_HAN_NM;
      let specs =
        "width=1000, height=800, top=200, left=100, toolbar=no, menubar=no, scrollbars=no, resizable=yes";
      window.open(url, name, specs);
      break;
    }
  }
};

// xml을 json으로 변환해주는 xmlToJson함수 선언
const xmlToJson = (xml) => {
  // Create the return object
  var obj = {};

  if (xml.nodeType == 1) {
    // element
    // do attributes
    if (xml.attributes.length > 0) {
      obj["@attributes"] = {};
      for (var j = 0; j < xml.attributes.length; j++) {
        var attribute = xml.attributes.item(j);
        obj["@attributes"][attribute.nodeName] = attribute.nodeValue;
      }
    }
  } else if (xml.nodeType == 3) {
    // text
    obj = xml.nodeValue;
  }

  // do children
  // If all text nodes inside, get concatenated text from them.
  var textNodes = [].slice.call(xml.childNodes).filter(function (node) {
    return node.nodeType === 3;
  });
  if (xml.hasChildNodes() && xml.childNodes.length === textNodes.length) {
    obj = [].slice.call(xml.childNodes).reduce(function (text, node) {
      return text + node.nodeValue;
    }, "");
  } else if (xml.hasChildNodes()) {
    for (var i = 0; i < xml.childNodes.length; i++) {
      var item = xml.childNodes.item(i);
      var nodeName = item.nodeName;
      if (typeof obj[nodeName] == "undefined") {
        obj[nodeName] = xmlToJson(item);
      } else {
        if (typeof obj[nodeName].push == "undefined") {
          var old = obj[nodeName];
          obj[nodeName] = [];
          obj[nodeName].push(old);
        }
        obj[nodeName].push(xmlToJson(item));
      }
    }
  }
  return obj;
};
</script>

<template>
  <div>
    <h1 class="text-center"  style="font-family: Anton; font-size: 80px">Research Page</h1>
    <p style="text-align: center;">
      <div
        class="me-3"
        style=" display: inline-block; width: 41.58px;"
        v-show="pgno == 1">
      </div>
      <a-button class="me-3" @click="prev" v-show="pgno > 1">&lt;</a-button>
      <span>&nbsp;{{pgno}} 페이지&nbsp;</span>
      <a-button class="ms-3" @click="next">&gt;</a-button>
    </p>
    <section class="wrapper">
      <div class="container-fostrap">
        <div class="content">
          <div class="container">
            <div class="row">
              <div class="col-md-4" v-for="research in researchList">
                <div style="text-decoration: none; cursor : pointer;" @click="oepnResearch(research.OTP_ID)">
                  <div class="card">
                    <div class="card-content">
                      <h4 class="card-title title">
                        {{ research.OTP_HAN_NM }}
                      </h4>
                      <p class="card-text">
                        <small class="text-body-secondary">
                          <b>연구책임자</b> {{ research.INCHARGE_NM  }}
                        </small>
                        <div></div>
                        <small class="text-body-secondary">
                          <b>기관</b> {{  research.PUBAGC }}
                        </small>
                        <div></div>
                        <small class="text-body-secondary">
                          <b>출판연도</b> {{ research.PBL_YY }}
                        </small>
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <!-- <div v-for="research in researchList">
      {{ research }}
      <div class="card mb-3">
        <div class="row g-0">
          <div class="col-md-8">
            <div class="card-body" @click="oepnResearch(research.OTP_ID)">
              <h5 class="card-title">{{ research.OTP_HAN_NM }}</h5>
              <p class="card-text">연구책임자 {{ research.INCHARGE_NM }}</p>
              <p class="card-text">기관 {{ research.PUBAGC }}</p>
              <p class="card-text">
                <small class="text-body-secondary">
                  출판연도 {{ research.PBL_YY }}
                </small>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div> -->
  </div>
</template>

<style scoped>
@import url(https://fonts.googleapis.com/css?family=Roboto:400,100,900);
@import url(https://fonts.googleapis.com/css2?family=Lobster&display=swap);
@import url(https://fonts.googleapis.com/css2?family=Anton&family=Roboto:wght@400;500;700&display=swap);

html,
body {
  -moz-box-sizing: border-box;
  box-sizing: border-box;
  height: 100%;
  width: 100%;
  background: #fff;
  font-family: "Roboto", sans-serif;
  font-weight: 400;
}

.wrapper {
  display: table;
  height: 100%;
  width: 100%;
}

.container-fostrap {
  display: table-cell;
  padding: 1em;
  text-align: center;
  vertical-align: middle;
}
.fostrap-logo {
  width: 100px;
  margin-bottom: 15px;
}
h1.heading {
  color: #fff;
  font-size: 1.15em;
  font-weight: 900;
  margin: 0 0 0.5em;
  color: #505050;
}
@media (min-width: 450px) {
  h1.heading {
    font-size: 3.55em;
  }
}
@media (min-width: 760px) {
  h1.heading {
    font-size: 3.05em;
  }
}
@media (min-width: 900px) {
  h1.heading {
    font-size: 3.25em;
    margin: 0 0 0.3em;
  }
}
.card {
  display: block;
  margin-bottom: 20px;
  line-height: 1.42857143;
  background-color: #fff;
  border-radius: 2px;
  box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.16), 0 2px 10px 0 rgba(0, 0, 0, 0.12);
  transition: box-shadow 0.25s;
}
.card:hover {
  box-shadow: 0 8px 17px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
}
.img-card {
  width: 100%;
  height: 200px;
  border-top-left-radius: 2px;
  border-top-right-radius: 2px;
  display: block;
  overflow: hidden;
}
.img-card img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  transition: all 0.25s ease;
}
.card-content {
  padding: 15px;
  text-align: left;
}
.card-title {
  margin-top: 0px;
  font-weight: 700;
  font-size: 1.65em;
}
.card-title a {
  color: #000;
  text-decoration: none !important;
}
.card-read-more {
  border-top: 1px solid #d4d4d4;
}
.card-read-more a {
  text-decoration: none !important;
  padding: 10px;
  font-weight: 600;
  text-transform: uppercase;
}

.word {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: normal;
  line-height: 1.2;
  height: 3.6em;
  text-align: left;
  word-wrap: break-word;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.title {
  color: black;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: normal;
  word-wrap: break-word;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}
</style>
