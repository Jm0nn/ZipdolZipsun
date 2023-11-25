<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { getAnnounce } from "@/api/board";
import { getNewsList } from "@/api/information";

const router = useRouter();

const articles = ref([]);

const userId = ref("");

if (window.sessionStorage.getItem("loginUser")) {
  userId.value = JSON.parse(window.sessionStorage.getItem("loginUser")).userId;
}

const success = (response) => {
  if (response.status == 200) {
    articles.value = response.data;
    articles.value = articles.value.slice(0, 5);
    articles.value = articles.value.map((article) => {
      let registTime = article.registTime;
      let date = new Date(registTime);
      if (today.setDate(today.getDate() - 1) < date) {
        article.registTime = registTime.substr(11, 5);
      } else if (today.setFullYear(today.getFullYear() - 1) < date) {
        article.registTime = registTime.substr(5, 5);
      } else {
        article.registTime = registTime.substr(0, 10);
      }
      return article;
    });
  }
};

const fail = (error) => console.log(JSON.stringify(error));

getAnnounce(success, fail);

const newsList = ref([]);

let today = new Date();

var year = today.getFullYear();
var month = ("0" + (today.getMonth() + 1)).slice(-2);
var day = ("0" + today.getDate()).slice(-2);

var dateString = year + "-" + month + "-" + day;

onMounted(() => {
  getNewsList(dateString, ({ status, data }) => {
    if (status === 200) {
      newsList.value = data.list;
      newsList.value = newsList.value.slice(0, 5);
    }
  });
});

const goAnnounce = (articleNo) => {
  router.push({
    name: "announce-detail",
    params: {
      articleNo: articleNo,
      boardNo: 1,
    },
  });
};

const goNews = (news) => {
  window.open(news.linkUrl);
};
</script>

<template>
  <!-- Carousel Start -->
  <div class="container-fluid px-0">
    <div id="carouselId" class="carousel slide" data-bs-ride="carousel">
      <div class="carousel-inner" role="listbox">
        <div class="carousel-item active">
          <img
            src="@/assets/apt.jpg"
            alt="apt image"
            style="width: 100%; height: 710px"
            class="img-fluid" />
          <div class="carousel-caption">
            <div class="container carousel-content">
              <h1 class="text-white display-1 mb-4 animated fadeInRight">
                관심있는 지역의 아파트를 찾아보세요
              </h1>
              <p class="mb-4 text-white fs-5 animated fadeInDown">
                매물정보 및 생생한 리뷰를 확인할 수 있어요
              </p>
              <router-link
                type="button"
                class="px-4 py-sm-3 px-sm-5 btn btn-danger rounded-pill carousel-content-btn1 animated fadeInLefte"
                :to="{ name: 'house-view' }">
                부동산 검색
              </router-link>
            </div>
          </div>
        </div>
        <div class="carousel-item">
          <img
            src="@/assets/community.jpg"
            style="width: 100%; height: 710px"
            class="img-fluid"
            alt="Second slide" />
          <div class="carousel-caption">
            <div class="container carousel-content">
              <h1 class="text-white display-1 mb-4 animated fadeInLeft">
                커뮤니티에서 다양한 의견을<br />주고받아보세요
              </h1>
              <p class="mb-4 text-white fs-5 animated fadeInDown">
                지역별 주민들간 소통을 할 수 있어요
              </p>
              <router-link
                type="button"
                class="px-4 py-sm-3 px-sm-5 btn btn-danger rounded-pill carousel-content-btn1 animated fadeInLefte"
                :to="{
                  name: 'board-list',
                  params: {
                    boardNo: '10',
                  },
                }">
                커뮤니티 이동
              </router-link>
            </div>
          </div>
        </div>
      </div>
      <button
        class="carousel-control-prev"
        type="button"
        data-bs-target="#carouselId"
        data-bs-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Previous</span>
      </button>
      <button
        class="carousel-control-next"
        type="button"
        data-bs-target="#carouselId"
        data-bs-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Next</span>
      </button>
    </div>
  </div>
  <!-- Carousel End -->

  <!-- About Start -->
  <div class="container-fluid py-5 my-5">
    <div class="container pt-5">
      <div class="row g-5">
        <div
          class="col-lg-5 col-md-6 col-sm-12 wow fadeIn"
          data-wow-delay=".3s">
          <div class="h-100 position-relative">
            <div class="position-absolute w-75" style="top: 0%">
              <img
                src="@/assets/zipdolzipsun_logo.png"
                class="img-fluid w-100 rounded"
                alt="" />
            </div>
          </div>
        </div>
        <div
          class="col-lg-7 col-md-6 col-sm-12 wow fadeIn"
          data-wow-delay=".5s">
          <h5 class="text-primary">About Us</h5>
          <h1 class="mb-4">zipdolzipsun</h1>
          <p>
            전국에 있는 아파트들의 상세정보를 확인 및 커뮤니티 기능을 제공하는
            <br />
            <strong>집돌집순</strong>
            사이트입니다.
          </p>
          <p class="mb-4">구성원 : 김정민, 박민호</p>
        </div>
      </div>
    </div>
  </div>
  <!-- About End -->

  <!-- Services Start -->
  <div class="container-fluid services py-5 mb-5">
    <div class="container">
      <div
        class="text-center mx-auto pb-5 wow fadeIn"
        data-wow-delay=".3s"
        style="max-width: 600px">
        <h2 class="text-primary">Our Services</h2>
      </div>
      <div class="row g-5 services-inner">
        <div class="col-md-6 col-lg-4 wow fadeIn" data-wow-delay=".3s">
          <div class="services-item bg-light">
            <div class="p-4 text-center services-content content_1">
              <div class="services-content-icon">
                <i class="fa fa-code fa-7x mb-4 text-primary"></i>
                <h4 class="mb-3">지역별 매물 조회</h4>
                <p class="mb-4">
                  전국에 있는 아파트들을 대상으로 하여 시세정보, 리뷰 등을
                  확인할 수 있어요.
                </p>
                <router-link
                  class="btn btn-secondary text-white px-5 py-3 rounded-pill"
                  :to="{ name: 'house-view' }">
                  바로가기
                </router-link>
              </div>
            </div>
          </div>
        </div>
        <div class="col-md-6 col-lg-4 wow fadeIn" data-wow-delay=".5s">
          <div class="services-item bg-light">
            <div class="p-4 text-center services-content2 content_2">
              <div class="services-content-icon">
                <i class="fa fa-file-code fa-7x mb-4 text-primary"></i>
                <h4 class="mb-3">커뮤니티</h4>
                <p class="mb-4">
                  지역별 소통공간을 활용하여 다양한 의견을 주고받을 수 있어요.
                </p>
                <router-link
                  class="btn btn-secondary text-white px-5 py-3 rounded-pill"
                  :to="{
                    name: 'board-list',
                    params: {
                      boardNo: '10',
                    },
                  }">
                  바로가기
                </router-link>
              </div>
            </div>
          </div>
        </div>
        <div class="col-md-6 col-lg-4 wow fadeIn" data-wow-delay=".7s">
          <div class="services-item bg-light">
            <div class="p-4 text-center services-content3 content_3">
              <div class="services-content-icon">
                <i class="fa fa-external-link-alt fa-7x mb-4 text-primary"></i>
                <h4 class="mb-3">부동산 정보 조회</h4>
                <p class="mb-4">
                  부동산과 관련된 뉴스, 블로그 글 및 정책/연구자료를 확인할 수
                  있어요
                </p>
                <router-link
                  class="btn btn-secondary text-white px-5 py-3 rounded-pill"
                  :to="{ name: 'info-view' }">
                  바로가기
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- Services End -->

  <!-- 원래 홈페이지 코드-->
  <!-- <div class="img main_image">
    <img src="@/assets/apt.jpg" alt="apt image" style="width: 100%" />
    <img
      src="@/assets/zipdolzipsun_logo.png"
      class="main_image_text"
      style="font-size: 50px" />
  </div>
  <br />
  <div class="container mb-5">
    <div style="text-align: center">
      <div>
        <h1>나에게 필요한 지역의 정보를 입력해 보세요</h1>
      </div>
      <router-link
        class="btn btn-outline-secondary m-3 find-home"
        :to="{ name: 'house-view' }">
        부동산 검색
      </router-link>
    </div>
    <div class="row">
      <div class="col-sm-6 mb-3 mb-sm-0">
        <div class="card mb-5">
          <div class="card-body">
            <h3 class="card-title text-center">주요 공지사항</h3>
            <table class="table table-hover">
              <tbody>
                <tr
                  class="text-center"
                  v-for="article in articles"
                  :key="article.articleNo"
                  @click="goAnnounce(article.articleNo)"
                  style="cursor: pointer">
                  <td class="text-center" style="width: 70%">
                    {{ article.subject }}
                  </td>
                  <td class="text-center" style="width: 20%">
                    {{ article.registTime }}
                  </td>
                  <td style="width: 10%"></td>
                </tr>
              </tbody>
            </table>
            <router-link
              class="d-flex justify-content-center btn btn-info"
              :to="{ name: 'announce-view' }">
              전체 공지사항 보기
            </router-link>
          </div>
        </div>
      </div>
      <div class="col-sm-6 mb-3 mb-sm-0">
        <div class="card mb-5">
          <div class="card-body">
            <h3 class="card-title text-center">주요 뉴스</h3>
            <table class="table table-hover">
              <tbody>
                <tr
                  class="text-center"
                  v-for="(news, index) in newsList"
                  :key="index"
                  @click="goNews(news)"
                  style="cursor: pointer">
                  <td class="text-start">
                    {{ news.title }}
                  </td>
                </tr>
              </tbody>
            </table>
            <router-link
              class="d-flex justify-content-center btn btn-info"
              :to="{ name: 'news-view' }">
              전체 뉴스 보기
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div> -->

  <!-- <KakaoMap :latitude="37.39843974939604" :longitude="127.10972941510465" /> -->
</template>

<style scoped>
/*** Carousel Start ***/

.carousel-item {
  position: relative;
}

.carousel-item::after {
  content: "";
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  background: rgba(0, 0, 0, 0.6);
}

.carousel-caption {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
}

.carousel-item p {
  max-width: 700px;
  margin: 0 auto 35px auto;
}

.carousel-control-prev {
  width: 90px;
  height: 60px;
  position: absolute;
  top: 50%;
  left: 0;
  background: var(--bs-primary);
  border-radius: 0 50px 50px 0;
  opacity: 1;
}

.carousel-control-prev:hover {
  background: var(--bs-secondary);
  transition: 0.8s;
}

.carousel-control-next {
  width: 90px;
  height: 60px;
  position: absolute;
  top: 50%;
  right: 0;
  background: var(--bs-primary);
  border-radius: 50px 0 0 50px;
  opacity: 1;
}

.carousel-control-next:hover {
  background: var(--bs-secondary);
  transition: 0.8s;
}

.carousel-caption .carousel-content a button.carousel-content-btn1 {
  background: var(--bs-secondary);
  color: var(--bs-dark);
  opacity: 1;
  border: 0;
  border-radius: 20px;
}

.carousel-caption .carousel-content a button.carousel-content-btn1:hover {
  background: var(--bs-primary);
  color: #ffffff;
  border: 0;
  opacity: 1;
  transition: 1s;
  border-radius: 20px;
}

.carousel-caption .carousel-content a button.carousel-content-btn2 {
  background: var(--bs-primary);
  color: var(--bs-white);
  opacity: 1;
  border: 0;
  border-radius: 20px;
}

.carousel-caption .carousel-content a button.carousel-content-btn2:hover {
  background: var(--bs-secondary);
  color: var(--bs-dark);
  border: 0;
  opacity: 1;
  transition: 1s;
  border-radius: 20px;
}

#carouselId .carousel-indicators li {
  width: 30px;
  height: 10px;
  background: var(--bs-primary);
  margin: 10px;
  border-radius: 30px;
  opacity: 1;
}

#carouselId .carousel-indicators li:hover {
  background: var(--bs-secondary);
  opacity: 1;
}

@media (max-width: 992px) {
  .carousel-item {
    min-height: 500px;
  }

  .carousel-item img {
    min-height: 500px;
    object-fit: cover;
  }

  .carousel-item h1 {
    font-size: 40px !important;
  }

  .carousel-item p {
    font-size: 16px !important;
  }
}

@media (max-width: 768px) {
  .carousel-item {
    min-height: 400px;
  }

  .carousel-item img {
    min-height: 400px;
    object-fit: cover;
  }

  .carousel-item h1 {
    font-size: 28px !important;
  }

  .carousel-item p {
    font-size: 14px !important;
  }
}

.page-header {
  background: linear-gradient(rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.6)),
    url(../img/carousel-1.jpg) center center no-repeat;
  background-size: cover;
}

.page-header .breadcrumb-item + .breadcrumb-item::before {
  color: var(--bs-white);
}

.page-header .breadcrumb-item,
.page-header .breadcrumb-item a {
  font-size: 18px;
  color: var(--bs-white);
}

/*** Carousel End ***/

/*** Services Start ***/

.services .services-item {
  box-shadow: 0 0 60px rgba(0, 0, 0, 0.2);
  width: 100%;
  height: 100%;
  border-radius: 10px;
  padding: 10px 0;
  position: relative;
}

.services-content::after {
  position: absolute;
  content: "";
  width: 100%;
  height: 0;
  top: 0;
  left: 0;
  border-radius: 10px 10px 0 0;
  background: rgba(104, 5, 97, 0.281);
  transition: 0.5s;
}

.services-content::after {
  top: 0;
  bottom: auto;
  border-radius: 10px 10px 10px 10px;
}

.services-item:hover .services-content::after {
  height: 100%;
  opacity: 1;
  transition: 0.5s;
}

.services-content2::after {
  position: absolute;
  content: "";
  width: 100%;
  height: 0;
  top: 0;
  left: 0;
  border-radius: 10px 10px 0 0;
  background: rgba(4, 17, 58, 0.281);
  transition: 0.5s;
}

.services-content2::after {
  top: 0;
  bottom: auto;
  border-radius: 10px 10px 10px 10px;
}

.services-item:hover .services-content2::after {
  height: 100%;
  opacity: 1;
  transition: 0.5s;
}

.services-content3::after {
  position: absolute;
  content: "";
  width: 100%;
  height: 0;
  top: 0;
  left: 0;
  border-radius: 10px 10px 0 0;
  background: rgba(3, 109, 34, 0.281);
  transition: 0.5s;
}

.services-content3::after {
  top: 0;
  bottom: auto;
  border-radius: 10px 10px 10px 10px;
}

.services-item:hover .services-content3::after {
  height: 100%;
  opacity: 1;
  transition: 0.5s;
}

.services-item:hover .services-content-icon {
  position: relative;
  z-index: 2;
}

.services-item .services-content-icon i,
.services-item .services-content-icon p {
  transition: 0.5s;
}

.services-item:hover .services-content-icon i {
  color: var(--bs-secondary) !important;
}

.services-item:hover .services-content-icon p {
  color: var(--bs-white);
}

/*** Services End ***/

/*** Project Start ***/

.project-img {
  position: relative;
  padding: 15px;
}

.project-img::before {
  content: "";
  position: absolute;
  width: 150px;
  height: 150px;
  top: 0;
  left: 0;
  background: var(--bs-secondary);
  border-radius: 10px;
  opacity: 1;
  z-index: -1;
  transition: 0.5s;
}

.project-img::after {
  content: "";
  width: 150px;
  height: 150px;
  position: absolute;
  right: 0;
  bottom: 0;
  background: var(--bs-primary);
  border-radius: 10px;
  opacity: 1;
  z-index: -1;
  transition: 0.5s;
}

.project-content {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
}

.project-content a {
  display: inline-block;
  padding: 20px 25px;
  background: var(--bs-primary);
  border-radius: 10px;
}

.project-item:hover .project-content {
  opacity: 1;
  transition: 0.5s;
}

.project-item:hover .project-img::before,
.project-item:hover .project-img::after {
  opacity: 0;
}

/*** Project End ***/
</style>
