<script setup>
import { ref, computed, watch, onMounted } from "vue";
import { getAptPriceList, getRoomType, rtDeal } from "@/api/map";

import Chart from "chart.js/auto";

const prop = defineProps({
  info: Object,
});

const emit = defineEmits(["addr", "detail"]);

const priceGraph = ref(null);
const roomType = ref({});
const dealList = ref([]);

const detailShow = ref(false);
const tourShow = ref(false);

const ckmodal = ref(false);
const ckmodal2 = ref(false);

const judgeopen = () => {
  ckmodal.value = true;
};

const judgeclose = () => {
  ckmodal.value = false;
};

const judgeopen2 = () => {
  ckmodal2.value = true;
};

const judgeclose2 = () => {
  ckmodal2.value = false;
};

const columns = ref([
  {
    title: "계약일",
    dataIndex: "rtDealDate",
    key: "rtDealDate",
  },
  {
    title: "거래가",
    dataIndex: "rtPrice",
    key: "rtPrice",
  },
  {
    title: "면적",
    dataIndex: "rtArea",
    key: "rtArea",
  },
  {
    title: "층수",
    dataIndex: "rtFloor",
    key: "rtFloor",
  },
]);

const parea = computed(() => {
  if (roomType.value.room_types.length > 0) {
    let res = "";
    let arr = roomType.value.room_types;
    for (let i = 0; i < arr.length; i++) {
      let item = arr[i].공급면적.p;

      res += Math.round(item);
      if (i != arr.length - 1) {
        res += ", ";
      }
    }

    return res;
  }
  return "";
});

const marea = computed(() => {
  if (roomType.value.room_types.length > 0) {
    let res = "";
    let arr = roomType.value.room_types;
    for (let i = 0; i < arr.length; i++) {
      let item = arr[i].공급면적.m2;

      res += item;
      if (i != arr.length - 1) {
        res += ", ";
      }
    }

    return res;
  }
  return "";
});

const ipjuDate = computed(() => {
  if (prop.info.사용승인일) {
    let date = "";
    date += prop.info.사용승인일.substr(0, 4);
    date += "년 ";
    date += prop.info.사용승인일.substr(4, 2);
    date += "월 ";
    date += prop.info.사용승인일.substr(6, 2);
    date += "일";
    return date;
  } else {
    return "-";
  }
});

const score = computed(() => {
  if (prop.info.review_score) {
    return Math.round(prop.info.review_score * 10) / 10.0;
  } else {
    return 0;
  }
});

const minmaxPrice = computed(() => {
  if (roomType.value.id) {
    let min = roomType.value.minSalesPrice;
    let max = roomType.value.maxSalesPrice;

    let minWon = "";
    if (min >= 10000) {
      minWon += Math.floor(min / 10000);
      minWon += "억";
    }

    if (min % 10000 === 0) {
      minWon += "";
    } else {
      minWon += " ";
      minWon += (min % 10000).toLocaleString();
      minWon += "만";
    }

    let maxWon = "";
    if (max >= 10000) {
      maxWon += Math.floor(max / 10000);
      maxWon += "억";
    }

    if (max % 10000 === 0) {
      maxWon += "";
    } else {
      maxWon += " ";
      maxWon += (max % 10000).toLocaleString();
      maxWon += "만";
    }

    return {
      min: minWon,
      max: maxWon,
    };
  } else {
    return {
      min: "-",
      max: "-",
    };
  }
});

const aptCost = computed(() => {
  if (roomType.value.id) {
    let arr = roomType.value.room_types;
    let costs = [];
    for (let item of arr) {
      costs.push(item.manage_cost.관리비);
    }
    // console.log(costs);
    let max = -1;
    let min = 99999;
    for (let num of costs) {
      max = Math.max(num, max);
      min = Math.min(num, min);
    }
    // console.log(max, min);

    if (max === 0) {
      return "";
    } else if (max === min) {
      return max + " 만";
    } else {
      return min + " ~ " + max + " 만";
    }
  } else {
    return "-";
  }
});

const maxminFloor = computed(() => {
  if (prop.info.id) {
    let max = prop.info.최고층수;
    let min = prop.info.최저층수;
    if (max === min) {
      return max + " 층";
    } else {
      return "최고 " + max + " 층, 최저 " + min + " 층";
    }
  } else {
    return "-";
  }
});

const conv = computed(() => {
  if (prop.info.id) {
    let arr = prop.info.편의시설;
    let res = "";
    for (let i = 0; i < arr.length; i++) {
      res += arr[i];
      if (i != arr.length - 1) {
        res += ", ";
      }
    }
    return res;
  } else {
    return "-";
  }
});

let ctx = null;
let aptChart = null;

onMounted(() => {
  ctx = document.getElementById("aptChart");
});

watch(detailShow, () => {
  if (detailShow.value) {
    emit("detail", false);
  } else {
    emit("detail", true);
  }
});

watch(
  () => prop.info,
  () => {
    // console.log(prop.info);
    detailShow.value = false;
    tourShow.value = false;
    ckmodal.value = false;
    ckmodal2.value = false;

    if (aptChart) {
      aptChart.destroy();
    }

    if (prop.info.id != null) {
      emit("addr", prop.info.name);
      getAptPriceList(
        prop.info.id,
        ({ status, data }) => {
          if (status === 200) {
            // console.log(data);
            priceGraph.value = data;
            // console.log(data);

            const dat1 = priceGraph.value.list[0];
            const dat2 = priceGraph.value.list[1];

            aptChart = new Chart(ctx, {
              type: "line",
              data: {
                labels: dat1.data.map((row) => row.date),
                datasets: [
                  {
                    label: dat1.label,
                    data: dat1.data.map((row) => row.price),
                    pointBorderWidth: 0,
                    pointStyle: "line",
                    borderColor: ["Tomato"],
                    backgroundColor: ["Tomato"],
                  },
                  {
                    label: dat2.label,
                    data: dat2.data.map((row) => row.price),
                    pointBorderWidth: 0,
                    pointStyle: "line",
                    borderColor: ["Limegreen"],
                    backgroundColor: ["Limegreen"],
                  },
                ],
              },
              options: {
                title: {
                  display: true,
                  text: "최근 거래가",
                },
                interaction: {
                  mode: "index",
                  intersect: false,
                },
                scales: {
                  x: {
                    afterTickToLabelConversion: function (scaleInstance) {
                      const ticks = scaleInstance.ticks;
                      const newTicks = ticks.filter((tick) => {
                        return tick.label.slice(5) === "06-01";
                      });

                      scaleInstance.ticks = newTicks.map((tick) => {
                        const newTick = tick;
                        newTick.label = tick.label.slice(0, 4);
                        return newTick;
                      });
                    },
                    grid: {
                      color: "transparent",
                    },
                  },
                  y: {
                    ticks: {
                      stepSize: Math.round(
                        (priceGraph.value.summary.maxPrice -
                          priceGraph.value.summary.minPrice) /
                          2,
                        2
                      ),
                    },
                  },
                },
                parsing: {
                  xAxisKey: "date",
                  yAxisKey: "price",
                },
              },
            });

            // console.log(chart);
          }
        },
        (err) => console.log(err)
      );

      getRoomType(
        prop.info.id,
        ({ status, data }) => {
          if (status === 200) {
            roomType.value = data;
            let price = roomType.value.average_sales_price;
            roomType.value.average_sales_price = price.toLocaleString();
          }
        },
        (err) => console.log(err)
      );

      rtDeal(
        prop.info.id,
        ({ status, data }) => {
          if (status === 200) {
            // console.log(data);
            dealList.value = data.data.map((deal) => {
              let d = deal;
              let price = deal.rtPrice;
              let resPrice = "";

              // console.log(price);

              if (price >= 10000) {
                resPrice += Math.floor(price / 10000);
                resPrice += "억";

                if (price % 10000 > 0) {
                  resPrice += " ";
                  resPrice += (price % 10000).toLocaleString();
                  resPrice += "만";
                }
              }

              d.rtPrice = resPrice;

              return d;
            });
          }
        },
        (err) => console.log(err)
      );
    }
  },
  { deep: true }
);

const showDetail = () => {
  detailShow.value = true;
};

const deleteDetail = () => {
  detailShow.value = false;
};

const getScore1 = (score) => {
  let res = "";
  for (let i = 0; i < score; i++) {
    res += "★";
  }
  return res;
};

const getScore2 = (score) => {
  let blank = 5 - score;
  let res = "";
  for (let i = 0; i < blank; i++) {
    res += "★";
  }
  return res;
};
</script>

<template>
  <div class="bg-light">
    <div v-show="!detailShow">
      <div id="imgbox">
        <img
          :src="info.image + '?w=500'"
          alt=""
          v-if="info.image"
          style="overflow: hidden; filter: brightness(70%)" />
        <img
          src="src/assets/no_image.jpg"
          v-if="!info.image"
          alt=""
          style="filter: brightness(70%)" />
        <div class="text">
          <span style="color: gold">★</span> {{ score }}
          <h2>{{ info.name }}</h2>
          {{ ipjuDate.substr(0, 9) }} 입주 • {{ info.총세대수 }} 세대
          <div class="btn btn-light btn-detail" @click="showDetail">
            <b>자세히</b>
          </div>
        </div>
      </div>
      <br />
      <h6 class="px-4 mb-3" style="font-weight: bold; font-size: 14px">
        ⓘ 최근 시세
      </h6>
      <h5 class="px-4 mb-2" style="font-weight: bold">
        매매 {{ minmaxPrice.min }} ~ {{ minmaxPrice.max }}
      </h5>
      <h6 class="px-4 mb-3" style="font-size: 14px">
        평균
        {{ roomType.average_sales_price }}
        만 <small>/ 3.3 m<sup>2</sup></small>
      </h6>
      <canvas id="aptChart" class="bg-white m-3 p-2 border rounded"></canvas>
      <br />
      <h5 class="bg-light px-4 mb-3" style="font-weight: bold">최근 거래</h5>
      <a-table
        class="m-3 border rounded"
        :data-source="dealList"
        :columns="columns"
        :pagination="false">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'rtArea'">
            {{ record.rtArea }} m<sup>2</sup>
          </template>
          <template v-if="column.key === 'rtFloor'">
            {{ record.rtFloor }} 층
          </template>
        </template>
      </a-table>
      <br />

      <div style="padding-top: 10px">
        <h5 class="px-4" style="font-weight: bold; display: inline-block">
          현장투어
          <template v-if="info.desc">
            <a-button
              class="mx-3"
              data-bs-toggle="collapse"
              :data-bs-target="'#tour' + info.id"
              aria-expanded="false"
              :aria-controls="'tour' + info.id"
              v-if="!tourShow"
              @click="tourShow = !tourShow">
              이미지 보기
            </a-button>
            <a-button
              class="mx-3"
              data-bs-toggle="collapse"
              :data-bs-target="'#tour' + info.id"
              aria-expanded="false"
              :aria-controls="'tour' + info.id"
              v-if="tourShow"
              @click="tourShow = !tourShow">
              접기
            </a-button>
          </template>
        </h5>
        <div
          class="flex card m-3 carousel-card"
          style="height: 100px"
          v-if="!info.desc">
          <div
            style="
              text-align: center;
              position: relative;
              top: 50%;
              transform: translateY(-50%);
            ">
            해당 아파트의 현장투어 정보가 없습니다.
          </div>
        </div>
        <template v-else>
          <div
            class="collapse"
            :class="{ show: tourShow }"
            :id="'tour' + info.id">
            <div id="review2" class="carousel slide">
              <div class="carousel-inner">
                <div
                  class="carousel-item"
                  :class="{ active: index == 0 }"
                  v-for="(src, index) in info.현장투어">
                  <div class="card m-3 carousel-card">
                    <img :src="src.img + '?w=500'" alt="" />
                    <div class="card-header bg-light pt-3">
                      <h5 class="card-title">
                        {{ src.title }}
                      </h5>
                    </div>
                    <div class="card-body">
                      <p class="card-text" style="text-align: left">
                        {{ src.desc }}
                      </p>
                    </div>
                  </div>
                </div>
              </div>
              <button
                class="carousel-control-prev control circle"
                type="button"
                data-bs-target="#review2"
                data-bs-slide="prev">
                <span
                  class="carousel-control-prev-icon"
                  aria-hidden="false"></span>
                <span class="visually-hidden">Previous</span>
              </button>
              <button
                class="carousel-control-next control circle"
                type="button"
                data-bs-target="#review2"
                data-bs-slide="next">
                <span
                  class="carousel-control-next-icon"
                  aria-hidden="false"></span>
                <span class="visually-hidden">Next</span>
              </button>
            </div>
          </div>
          <div class="carousel carousel-dark slide" v-if="!ckmodal">
            <div class="carousel-inner">
              <div
                class="carousel-item"
                :class="{ active: index == 0 }"
                v-for="(src, index) in info.resident_review">
                <div class="card m-3 carousel-card" style="height: 100px">
                  <div class="card-body">
                    <p class="card-text word" style="text-align: left">
                      {{ info.desc }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="carousel carousel-dark slide" v-if="ckmodal">
            <div class="carousel-inner">
              <div
                class="carousel-item"
                :class="{ active: index == 0 }"
                v-for="(src, index) in info.resident_review">
                <div class="card m-3 carousel-card">
                  <div class="card-body">
                    <p class="card-text" style="text-align: left">
                      {{ info.desc }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="ms-4" @click="judgeopen" v-if="!ckmodal">
            <a-button>펼치기</a-button>
          </div>
          <div class="ms-4" @click="judgeclose" v-if="ckmodal">
            <a-button>접기</a-button>
          </div>
        </template>
        <br />
        <!-- 
      <div class="collapse" :class="{ show: tourShow }" :id="'tour' + info.id">
        <div class="card mx-2 mt-3" v-for="src in info.현장투어">
          <img :src="src.img + '?w=500'" alt="" />
          <div class="card-body">
            <h5 class="card-title">
              {{ src.title }}
            </h5>
            <p class="card-text">{{ src.desc }}</p>
          </div>
        </div>
      </div>
      <div style="height: 30px"></div> -->

        <h5 class="px-4" style="font-weight: bold">
          거주민 리뷰 ({{ info.review_cnt }})
        </h5>
        <div
          class="flex card m-3 carousel-card"
          style="height: 100px"
          v-if="!info.resident_review || info.resident_review.length == 0">
          <div
            style="
              text-align: center;
              position: relative;
              top: 50%;
              transform: translateY(-50%);
            ">
            해당 아파트의 리뷰가 없습니다.
          </div>
        </div>
        <div id="review" class="carousel carousel-dark slide" v-if="!ckmodal2">
          <div class="carousel-inner">
            <div
              class="carousel-item"
              :class="{ active: index == 0 }"
              v-for="(src, index) in info.resident_review">
              <div class="card m-3 carousel-card" style="height: 160px">
                <div class="card-body">
                  <h5 class="card-title">{{ src.residenceTime }}</h5>
                  <h6
                    class="card-subtitle mb-2 text-body-secondary"
                    style="text-align: right">
                    <span style="color: gold">
                      {{ getScore1(src.score) }}
                    </span>
                    <span>{{ getScore2(src.score) }}</span>
                  </h6>
                  <p class="card-text word" style="text-align: left">
                    {{ src.content }}
                  </p>
                </div>
              </div>
            </div>
          </div>
          <button
            class="carousel-control-prev control circle"
            type="button"
            data-bs-target="#review"
            data-bs-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="false"></span>
            <span class="visually-hidden">Previous</span>
          </button>
          <button
            class="carousel-control-next control circle"
            type="button"
            data-bs-target="#review"
            data-bs-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="false"></span>
            <span class="visually-hidden">Next</span>
          </button>
        </div>

        <div id="review" class="carousel carousel-dark slide" v-if="ckmodal2">
          <div class="carousel-inner">
            <div
              class="carousel-item"
              :class="{ active: index == 0 }"
              v-for="(src, index) in info.resident_review">
              <div class="card m-3 carousel-card">
                <div class="card-body">
                  <h5 class="card-title">{{ src.residenceTime }}</h5>
                  <h6
                    class="card-subtitle mb-2 text-body-secondary"
                    style="text-align: right">
                    <span style="color: gold">
                      {{ getScore1(src.score) }}
                    </span>
                    <span>{{ getScore2(src.score) }}</span>
                  </h6>
                  <p class="card-text" style="text-align: left">
                    {{ src.content }}
                  </p>
                </div>
              </div>
            </div>
          </div>
          <button
            class="carousel-control-prev control circle"
            type="button"
            data-bs-target="#review"
            data-bs-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="false"></span>
            <span class="visually-hidden">Previous</span>
          </button>
          <button
            class="carousel-control-next control circle"
            type="button"
            data-bs-target="#review"
            data-bs-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="false"></span>
            <span class="visually-hidden">Next</span>
          </button>
        </div>
        <template
          v-if="info.resident_review && info.resident_review.length > 0">
          <div class="ms-4">
            <a-button @click="judgeopen2" v-if="!ckmodal2"> 펼치기 </a-button>
          </div>
          <div class="ms-4">
            <a-button @click="judgeclose2" v-if="ckmodal2"> 접기 </a-button>
          </div>
        </template>

        <br />
      </div>
    </div>

    <div class="bg-light" id="info" v-if="detailShow">
      <div id="top" class="sticky-top">
        <button
          class="btn-close me-auto"
          aria-label="Close"
          @click="deleteDetail"></button>
        상세 정보
        <div id="box" class="ms-auto"></div>
      </div>

      <table class="table">
        <tbody>
          <tr>
            <th style="width: 100px">세대수</th>
            <td>{{ info.총세대수 }} 세대</td>
          </tr>
          <tr>
            <th>면적(m<sup>2</sup>)</th>
            <td>{{ marea }}</td>
          </tr>
          <tr>
            <th>면적(평)</th>
            <td>{{ parea }}</td>
          </tr>
          <tr>
            <th>입주시기</th>
            <td>{{ ipjuDate }}</td>
          </tr>
          <tr>
            <th>시공사</th>
            <td>{{ info.시공사 }}</td>
          </tr>
          <tr>
            <th>동수</th>
            <td>{{ info.총동수 }} 동</td>
          </tr>
          <tr>
            <th>유형</th>
            <td>{{ roomType.real_type }}</td>
          </tr>
          <tr>
            <th>관리비(월)</th>
            <td>{{ aptCost }}</td>
          </tr>
          <tr>
            <th>층수</th>
            <td>{{ maxminFloor }}</td>
          </tr>
          <tr>
            <th>주차</th>
            <td>
              {{ info.총주차대수 }} 대 (가구 당 {{ info.가구당주차대수 }} 대)
            </td>
          </tr>
          <tr>
            <th>난방</th>
            <td>{{ info.난방방식 }} ({{ info.난방연료 }})</td>
          </tr>
          <tr>
            <th>편의시설</th>
            <td>{{ conv }}</td>
          </tr>
          <tr>
            <th>도로명주소</th>
            <td>{{ info.roadAddress }}</td>
          </tr>
          <tr>
            <th>구주소</th>
            <td>{{ info.구주소 }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>
.divv {
  box-sizing: border-box;
}
.black-bg {
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  position: fixed;
  padding: 20px;
}
.white-bg {
  width: 100%;
  background: white;
  border-radius: 8px;
  padding: 20px;
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

.word2 {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: normal;
  line-height: 1.2;
  height: 3.6em;
  text-align: left;
  word-wrap: break-word;
  display: -webkit-box;
  -webkit-line-clamp: 15;
  -webkit-box-orient: vertical;
}
#imgbox {
  height: 250px;
  overflow: hidden;
  justify-content: center;
}

.text {
  position: absolute;
  top: 290px;
  left: 53%;
  width: 100%;
  transform: translate(-50%, -50%);
  color: white;
}

#info {
  padding-top: 70px;
  padding-bottom: 50px;
  width: 25vw;
  height: 100vh;
  border-left: 1px solid #bbbbbb;
  z-index: 2;
  position: absolute;
  top: 0;
  right: 0;
  background-color: white;
  overflow-y: scroll;
  -ms-overflow-style: none;
}

#info::-webkit-scrollbar {
  display: none;
}

.btn-detail {
  width: 40px;
  height: 20px;
  padding: 3px;
  margin-left: 10px;
  font-size: 8px;
  justify-content: center;
  align-items: center;
  overflow: visible;
}

#top {
  display: flex;
  height: 34px;
  background-color: white;
  padding: 5px;
  text-align: center;
  border-bottom: 1px solid #bbbbbb;
}

#box {
  display: inline-block;
  width: 24px;
  height: 24px;
}

.carousel:hover button {
  display: block;
}

.control {
  display: none;
}

.carousel-card {
  max-height: 400px;
  overflow: auto;
  -ms-overflow-style: none;
}

.carousel-card::-webkit-scrollbar {
  width: 5px;
}
.carousel-card::-webkit-scrollbar-thumb {
  background-color: #aaaaaa;
  border-radius: 5px;
}
.carousel-card::-webkit-scrollbar-track {
  background-color: #dddddd;
  border-radius: 5px;
}
</style>
