import { createRouter, createWebHistory } from "vue-router";
import MainView from "@/views/MainView.vue";
import HouseView from "@/views/HouseView.vue";
import AnnounceView from "@/views/AnnounceView.vue";
import BoardView from "@/views/BoardView.vue";
import AdminView from "@/views/AdminView.vue";
import MemberView from "@/views/MemberView.vue";
import InfoView from "@/views/InfoView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "main-view",
      component: MainView,
    },
    {
      path: "/house",
      name: "house-view",
      component: HouseView,
    },
    {
      path: "/announce",
      name: "announce-view",
      component: AnnounceView,
      redirect: { name: "announce-list" },
      children: [
        {
          path: "list",
          name: "announce-list",
          component: () => import("@/components/announce/AnnounceList.vue"),
        },
        {
          path: ":boardNo/:articleNo",
          name: "announce-detail",
          component: () => import("@/components/announce/AnnounceDetail.vue"),
        },
        {
          path: "modify/:boardNo/:articleNo",
          name: "announce-modify",
          component: () => import("@/components/announce/AnnounceModify.vue"),
          beforeEnter: (to, from) => {
            let loginUser = JSON.parse(
              window.sessionStorage.getItem("loginUser")
            );
            if (!loginUser || loginUser.userId != "admin") {
              alert("로그인이 필요합니다.");
              return false;
            }
          },
        },
        {
          path: "write/:boardNo",
          name: "announce-write",
          component: () => import("@/components/announce/AnnounceCreate.vue"),
          beforeEnter: (to, from) => {
            let loginUser = JSON.parse(
              window.sessionStorage.getItem("loginUser")
            );
            if (!loginUser || loginUser.userId != "admin") {
              alert("로그인이 필요합니다.");
              return false;
            }
          },
        },
      ],
    },
    {
      path: "/board",
      name: "board-view",
      component: BoardView,
      redirect: { name: "board-list" },
      children: [
        {
          path: "list/:boardNo",
          name: "board-list",
          component: () => import("@/components/board/BoardList.vue"),
        },
        {
          path: ":boardNo/:articleNo",
          name: "board-detail",
          component: () => import("@/components/board/BoardDetail.vue"),
        },
        {
          path: "modify/:boardNo/:articleNo",
          name: "board-modify",
          component: () => import("@/components/board/BoardModify.vue"),
          beforeEnter: (to, from) => {
            let loginUser = JSON.parse(
              window.sessionStorage.getItem("loginUser")
            );
            if (!loginUser) {
              alert("로그인이 필요합니다.");
              return false;
            }
          },
        },
        {
          path: "write/:boardNo",
          name: "board-write",
          component: () => import("@/components/board/BoardCreate.vue"),
          beforeEnter: (to, from) => {
            let loginUser = JSON.parse(
              window.sessionStorage.getItem("loginUser")
            );
            if (!loginUser) {
              alert("로그인이 필요합니다.");
              return false;
            }
          },
        },
      ],
    },
    {
      path: "/information",
      name: "info-view",
      component: InfoView,
      redirect: { name: "news-view" },
      children: [
        {
          path: "news",
          name: "news-view",
          component: () => import("@/components/information/NewsPage.vue"),
        },
        {
          path: "blog",
          name: "blog-view",
          component: () => import("@/components/information/BlogPage.vue"),
        },
        {
          path: "research",
          name: "research-view",
          component: () => import("@/components/information/ResearchPage.vue"),
        },
      ],
    },
    {
      path: "/admin",
      name: "admin-view",
      component: AdminView,
      beforeEnter: (to, from) => {
        let loginUser = JSON.parse(window.sessionStorage.getItem("loginUser"));
        if (!loginUser || loginUser.userId != "admin") {
          alert("로그인이 필요합니다.");
          return false;
        }
      },
    },
    {
      path: "/member",
      name: "member-view",
      component: MemberView,
      redirect: { name: "member-login" },
      children: [
        {
          path: "regist",
          name: "member-regist",
          component: () => import("@/components/member/MemberRegist.vue"),
          beforeEnter: (to, from) => {
            let loginUser = JSON.parse(
              window.sessionStorage.getItem("loginUser")
            );
            if (loginUser) {
              router.push({ name: "main-view" });
            }
          },
        },
        {
          path: "modify",
          name: "member-modify",
          component: () => import("@/components/member/MemberModify.vue"),
          beforeEnter: (to, from) => {
            let loginUser = JSON.parse(
              window.sessionStorage.getItem("loginUser")
            );
            if (!loginUser) {
              alert("로그인이 필요합니다.");
              return false;
            }
          },
        },
      ],
    },
  ],
});

export default router;
