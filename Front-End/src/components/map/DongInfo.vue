<script setup>
import { ref, computed, watch, onMounted } from "vue";
import { getDongPriceList, getRanking } from "@/api/map";

import Chart from "chart.js/auto";

const prop = defineProps({
  info: Object,
});

const emit = defineEmits(["addr"]);

const priceGraph = ref(null);
const ranking = ref([]);

const apts = ref("");
const household = ref("");

let ctx = null;
let dongChart = null;

const minmaxPrice = computed(() => {
  // console.log(prop.info);
  if (prop.info.id) {
    let min = prop.info.price.sales.min;
    let max = prop.info.price.sales.max;

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
      min: "",
      max: "",
    };
  }
});

onMounted(() => {
  ctx = document.getElementById("dongChart");
});

watch(
  () => prop.info,
  () => {
    if (dongChart) {
      dongChart.destroy();
    }

    if (prop.info.id != null) {
      emit("addr", prop.info.address);
      getDongPriceList(
        prop.info.id,
        ({ status, data }) => {
          if (status === 200) {
            priceGraph.value = data.data.list;

            const dat = priceGraph.value;
            // console.log(dat);

            dongChart = new Chart(ctx, {
              type: "line",
              data: {
                labels: dat.map((row) => row.date),
                datasets: [
                  {
                    label: "매매",
                    data: dat.map((row) => row.salesVolume),
                    pointBorderWidth: 0,
                    pointStyle: "line",
                    borderColor: ["Tomato"],
                    backgroundColor: ["Tomato"],
                  },
                  {
                    label: "전세",
                    data: dat.map((row) => row.charterVolume),
                    pointBorderWidth: 0,
                    pointStyle: "line",
                    borderColor: ["Royalblue"],
                    backgroundColor: ["Royalblue"],
                  },
                  {
                    label: "월세",
                    data: dat.map((row) => row.rentVolume),
                    pointBorderWidth: 0,
                    pointStyle: "line",
                    borderColor: ["Limegreen"],
                    backgroundColor: ["Limegreen"],
                  },

                  {
                    label: "전체",
                    data: dat.map((row) => {
                      return (
                        row.charterVolume + row.rentVolume + row.salesVolume
                      );
                    }),
                    pointBorderWidth: 0,
                    pointStyle: "line",
                    backgroundColor: ["#eeeeee"],
                    borderColor: ["#eeeeee"],
                    fill: true,
                  },
                ],
              },
              options: {
                interaction: {
                  mode: "index",
                  intersect: false,
                },
                scales: {
                  x: {
                    afterTickToLabelConversion: function (scaleInstance) {
                      const ticks = scaleInstance.ticks;
                      const newTicks = ticks.filter((tick) => {
                        return tick.label.slice(5) === "06";
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
                    beginAtZero: true,
                  },
                },
              },
            });

            // console.log(chart);
          }
        },
        (err) => console.log(err)
      );

      getRanking(
        prop.info.id,
        ({ status, data }) => {
          if (status === 200) {
            ranking.value = data.list;
          }
        },
        (err) => console.log(err)
      );
    }
  },
  { deep: true }
);
</script>

<template>
  <div class="bg-light" style="height: 100vh">
    <h1 class="pt-3 text-center">
      <b>{{ info.name }}</b>
    </h1>
    <p class="pe-4" style="text-align: right">
      총 {{ info.apts }} 단지, {{ info.household }} 세대
    </p>
    <canvas id="dongChart" class="bg-white m-3 p-2 border rounded"></canvas>
    <br />
    <h3 class="bg-light px-4 mb-3" style="font-weight: bold">최근 거래</h3>
    <h5 class="px-4 mb-3">
      매매 {{ minmaxPrice.min }} ~ {{ minmaxPrice.max }}
    </h5>
    <br />
    <h3 class="bg-light px-4 mb-3" style="font-weight: bold">
      매물 거래량 순위
    </h3>
    <div class="bg-white m-3 p-2 border rounded">
      <table class="table">
        <thead>
          <tr>
            <th>순위</th>
            <th>단지</th>
            <th>총세대수</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in ranking" :key="item.areaDanjiId">
            <td>{{ item.rankingNumber }}</td>
            <td>{{ item.areaDanjiName }}</td>
            <td>{{ item.총세대수 }} 세대</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped></style>
