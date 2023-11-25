<script setup>
import { ref, onMounted } from "vue";
import { getBlogList } from "@/api/information";

const pgno = ref(1);
const blogList = ref([]);

onMounted(() => {
  getList();
});

const getList = () => {
  getBlogList(
    pgno.value,
    ({ status, data }) => {
      if (status === 200) {
        blogList.value = data.items.map((blog) => {
          let b = blog;
          let date = blog.postdate;
          b.computedDate =
            date.substr(0, 4) +
            ". " +
            date.substr(4, 2) +
            ". " +
            date.substr(6, 2) +
            ".";
          return b;
        });
      }
    },
    (err) => console.log(err)
  );
};

const prev = () => {
  if (pgno.value > 1) {
    pgno.value--;
    getList();
  }
};

const next = () => {
  pgno.value++;
  getList();
};

const scrollToTop = () => {
  window.scrollTo(0, 0);
};
</script>

<template>
  <div>
    <h1 class="text-center" style="font-family: Anton; font-size: 80px">Blog Page</h1>
    <p style="text-align: center">
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
              <div class="col-md-6" v-for="blog in blogList">
                <a
                  style="text-decoration: none"
                  :href="blog.link"
                  target="_blank">
                  <div class="card">
                    <div class="card-content">
                      <h4 class="card-title title" v-html="blog.title"></h4>
                      <p class="card-text">
                        <small class="text-body-secondary">
                          {{ blog.bloggername }}
                        </small>
                      </p>
                      <p class="word" v-html="blog.description"></p>
                      <p class="card-text">
                        <small class="text-body-secondary">
                          {{ blog.computedDate }}
                        </small>
                      </p>
                    </div>
                  </div>
                </a>
              </div>
            </div>
          </div>
        </div>
        <a-button type="text" @click="scrollToTop" class="top border">▲</a-button>
      </div>
    </section>
    <!-- <div v-for="blog in blogList">
      {{ blog }}
      <div class="card mb-3">
        <div class="row g-0">
          <div class="col-md-8">
            <div class="card-body">
              <h5 class="card-title">{{ blog.title }}</h5>
              <p class="card-text">
                {{ blog.description }}
              </p>
              <p class="card-text">
                <small class="text-body-secondary">
                  {{ blog.postdate }}
                </small>
              </p>
              <a class="news-link" :href="blog.link" target="_blank">
                블로그 이동
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

.top {
  position: fixed;
  bottom: 20px;
  right: 20px;
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
