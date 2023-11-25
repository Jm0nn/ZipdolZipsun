<script setup>
import { ref, computed, onMounted } from "vue";
import { Carousel, Slide } from "vue3-carousel";
import "vue3-carousel/dist/carousel.css";
import { getNewsList } from "@/api/information";

const newsList = ref([]);

const back = ref(0);

const date = computed(() => {
  return new Date(new Date().setDate(new Date().getDate() - back.value));
});

const dateString = computed(() => {
  return (
    date.value.getFullYear() +
    "-" +
    ("0" + (date.value.getMonth() + 1)).slice(-2) +
    "-" +
    ("0" + date.value.getDate()).slice(-2)
  );
});

onMounted(() => {
  getNews();
});

const getNews = () => {
  getNewsList(
    dateString.value,
    ({ status, data }) => {
      if (status === 200) {
        newsList.value = data.list;
      }
    },
    (err) => console.log(err)
  );
};

const before = () => {
  back.value++;
  getNews();
};

const next = () => {
  if (back.value > 0) {
    back.value--;
    getNews();
  }
};
</script>

<template>
  <div>
    <h1 class="text-center" style="font-family: Anton; font-size: 80px">
      News Page
    </h1>
    <p style="text-align: center;">
      <a-button class="me-3" @click="before">&lt;</a-button>
      {{ dateString }}
      <a-button class="ms-3" @click="next" v-show="back > 0">&gt;</a-button>
      <div
        class="ms-3"
        style=" display: inline-block; width: 41.58px;"
        v-show="back == 0">
      </div>
    </p>
    <section class="wrapper">
      <div class="container-fostrap">
        <div class="content">
          <div class="container">
            <div class="row">
              <div class="col-md-4" v-for="news in newsList">
                <div class="card">
                  <a class="img-card" :href="news.linkUrl">
                    <img :src="news.thumbnail" />
                  </a>
                  <div class="card-content">
                    <h4 class="card-title title">
                      <a :href="news.linkUrl">
                        {{ news.title }}
                      </a>
                    </h4>
                    <p class="word">
                      {{ news.summaryContent }}
                    </p>
                  </div>
                  <div class="card-read-more">
                    <a
                      :href="news.linkUrl"
                      target="_blank"
                      class="btn btn-link btn-block">
                      Read More
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <!-- <div v-for="news in newsList">
      <div class="card mb-3">
        <div class="row g-0">
          <div class="col-md-4">
            <img
              :src="news.thumbnail"
              class="img-fluid rounded-start"
              alt="..."
              style="height: 200px; width: 300px" />
          </div>
          <div class="col-md-8">
            <div class="card-body">
              <h5 class="card-title">{{ news.title }}</h5>
              <p class="card-text">
                {{ news.summaryContent }}
              </p>
              <p class="card-text">
                <small class="text-body-secondary">
                  {{ news.publishDateTime }}
                </small>
              </p>
              <a class="news-link" :href="news.linkUrl" target="_blank">
                전체 기사 보기
              </a>
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

#myBtn {
  display: none;
  position: fixed;
  bottom: 20px;
  right: 30px;
  z-index: 99;
  border: none;
  outline: none;
  background-color: rgb(129, 209, 9);
  color: white;
  cursor: pointer;
  padding: 15px;
  border-radius: 10px;
  font-size: 18px;
}

#myBtn:hover {
  background-color: #555;
}

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
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>
