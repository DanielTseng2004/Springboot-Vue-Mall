import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import ElementPlus, { ElLoading, ElMessage, ElMessageBox } from "element-plus";
import "element-plus/dist/index.css";
import "../all.css";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import NavBar from "./components/NavBar.vue";

const app = createApp(App);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.component("NavBar", NavBar);

app.config.globalProperties.$loadingService = ElLoading.service;
app.config.globalProperties.$message = ElMessage;
app.config.globalProperties.$confirm = ElMessageBox.confirm;

app.use(ElementPlus);
app.use(router);
app.mount("#app");
