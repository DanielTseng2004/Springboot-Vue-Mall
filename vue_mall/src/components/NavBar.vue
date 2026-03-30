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

<style scoped>
/* 1. 基礎背景與高度 */
.navbar {
  background-color: #3b99fc; /* 圖片中的主藍色 */
  color: white;
  padding: 0;
  height: 60px;
  border-bottom: none;
}

.nav-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
  height: 100%;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: white;
  margin-right: 30px;
  white-space: nowrap;
}

/* 2. 選單樣式重置 */
.nav-menu {
  flex: 1;
  background-color: transparent !important;
  border-bottom: none !important;
  height: 60px;
}

/* 項目基礎樣式 */
.nav-menu :deep(.el-menu-item) {
  color: rgba(255, 255, 255, 0.9) !important;
  font-size: 15px;
  height: 60px;
  line-height: 60px;
  border-bottom: none !important;
  transition: all 0.2s;
  padding: 0 20px;
}

/* 3. 選中狀態 (核心修改) */
.nav-menu :deep(.el-menu-item.is-active) {
  background-color: #61afff !important; /* 選中時的淺藍色塊 */
  color: white !important;
  position: relative;
}

/* 選中時底部的黃色裝飾線 */
.nav-menu :deep(.el-menu-item.is-active::after) {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background-color: #fadb14; /* 圖片中的黃色線 */
}

/* 懸停效果 */
.nav-menu :deep(.el-menu-item:hover) {
  background-color: rgba(255, 255, 255, 0.1) !important;
}

/* 4. 右側與其他元件 */
.auth-links {
  display: flex;
  align-items: center;
  gap: 12px;
}

.auth-btn {
  color: white !important;
}

.user-info {
  color: white;
  font-size: 14px;
  margin-right: 10px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.el-icon {
  margin-right: 4px;
  vertical-align: middle;
}

/* Badge 修正 */
.cart-badge :deep(.el-badge__content) {
  top: 18px;
}
</style>
