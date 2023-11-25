import "./assets/main.css";

import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import VueCookies from "vue3-cookies";

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap";

import Antd from "ant-design-vue";

const app = createApp(App);

app.use(router);
app.use(Antd);

app.use(VueCookies, {
  expireTimes: "30d",
  secure: true,
});

app.mount("#app");
