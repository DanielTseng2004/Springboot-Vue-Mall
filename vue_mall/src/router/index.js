import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/Home.vue";
import Login from "../views/Login.vue";
import Register from "../views/Register.vue";
import Products from "../views/Products.vue";
import Orders from "../views/Orders.vue";

const routes = [
  { path: "/", name: "Home", component: Home },
  { path: "/login", name: "Login", component: Login },
  { path: "/register", name: "Register", component: Register },
  { path: "/products", name: "Products", component: Products },
  {
    path: "/products/:productId",
    name: "ProductDetail",
    component: () => import("../views/ProductDetail.vue"),
  },
  {
    path: "/productmanage",
    name: "ProductManage",
    component: () => import("../views/ProductManage.vue"),
  },
  {
    path: "/orders/:userId?",
    name: "Orders",
    component: Orders,
  },
  {
    path: "/cart",
    name: "Cart",
    component: () => import("../views/Cart.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
