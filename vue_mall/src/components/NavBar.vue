<template>
  <el-header class="navbar">
    <div class="nav-container">
      <div class="logo">VUE ADMIN</div>

      <el-menu
        :default-active="$route.path"
        class="nav-menu"
        mode="horizontal"
        router
      >
        <el-menu-item index="/">
          <el-icon><HomeFilled /></el-icon> 首頁
        </el-menu-item>

        <el-menu-item index="/products">
          <el-icon><Menu /></el-icon> 產品列表
        </el-menu-item>

        <template v-if="userRole === 'BUYER'">
          <el-menu-item :index="`/orders/${userId}`">
            <el-icon><List /></el-icon> 我的訂單
          </el-menu-item>
          <el-menu-item index="/cart">
            <el-badge
              :value="cartCount"
              :hidden="cartCount === 0"
              class="cart-badge"
            >
              <el-icon><ShoppingCart /></el-icon> 購物車
            </el-badge>
          </el-menu-item>
        </template>

        <template v-if="userRole === 'SELLER'">
          <el-menu-item index="/productmanage">
            <el-icon><Management /></el-icon> 商品管理
          </el-menu-item>
          <el-menu-item index="/orders">
            <el-icon><Tickets /></el-icon> 訂單管理
          </el-menu-item>
        </template>
      </el-menu>

      <div class="auth-links">
        <template v-if="!isLoggedIn">
          <router-link to="/login"
            ><el-button
              link
              class="auth-btn"
              >登入</el-button
            ></router-link
          >
          <router-link to="/register"
            ><el-button
              type="warning"
              size="small"
              >註冊</el-button
            ></router-link
          >
        </template>
        <template v-else>
          <span class="user-info">
            <el-icon><User /></el-icon> {{ userEmail }}
          </span>
          <el-button
            type="danger"
            size="small"
            plain
            @click="handleLogout"
            >登出</el-button
          >
        </template>
      </div>
    </div>
  </el-header>
</template>

<script>
import {
  User,
  HomeFilled,
  Menu,
  ShoppingCart,
  List,
  Management,
  Tickets,
} from "@element-plus/icons-vue";
import { cartUtil } from "../api/cart";

export default {
  name: "Navbar",
  components: {
    User,
    HomeFilled,
    Menu,
    ShoppingCart,
    List,
    Management,
    Tickets,
  },
  data() {
    return {
      isLoggedIn: !!localStorage.getItem("token"),
      userRole: localStorage.getItem("userRole"),
      userId: localStorage.getItem("userId"),
      userEmail: localStorage.getItem("userEmail"),
      cartCount: 0,
    };
  },
  mounted() {
    this.updateCartCount();
    window.addEventListener("cart-updated", this.updateCartCount);
  },
  beforeUnmount() {
    window.removeEventListener("cart-updated", this.updateCartCount);
  },
  methods: {
    updateCartCount() {
      const cart = cartUtil.getCart();
      this.cartCount = cart.reduce((total, item) => total + item.quantity, 0);
    },
    handleLogout() {
      localStorage.clear();
      this.$message.success("已登出");
      this.$router.push("/login").then(() => {
        window.location.reload();
      });
    },
  },
};
</script>



