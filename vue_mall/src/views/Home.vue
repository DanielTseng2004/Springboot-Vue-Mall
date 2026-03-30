<template>
  <div class="home">
    <el-row justify="center">
      <el-col :span="16">
        <el-card
          class="welcome-card"
          shadow="never"
        >
          <template #header>
            <div class="welcome-header">
              <el-icon
                size="40"
                color="#409eff"
                ><Shop
              /></el-icon>
              <h1>歡迎來到商城</h1>
            </div>
          </template>
          <p>探索我們的產品，享受購物樂趣！</p>
          <el-row
            :gutter="20"
            class="action-row"
          >
            <el-col
              :xs="24"
              :sm="8"
              class="mb-4"
            >
              <el-button
                type="primary"
                size="large"
                class="w-full custom-btn"
                @click="$router.push('/products')"
              >
                <el-icon><ShoppingBag /></el-icon>
                <span>瀏覽產品</span>
              </el-button>
            </el-col>

            <el-col
              :xs="24"
              :sm="8"
              class="mb-4"
            >
              <el-button
                type="success"
                size="large"
                class="w-full custom-btn"
                @click="$router.push('/login')"
              >
                <el-icon><User /></el-icon>
                <span>登入帳號</span>
              </el-button>
            </el-col>

            <el-col
              :xs="24"
              :sm="8"
              class="mb-4"
            >
              <el-button
                type="info"
                size="large"
                class="w-full custom-btn"
                @click="$router.push('/register')"
              >
                <el-icon><UserFilled /></el-icon>
                <span>立即註冊</span>
              </el-button>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { Shop, ShoppingBag, User } from "@element-plus/icons-vue";

export default {
  name: "Home",
  components: {
    Shop,
    ShoppingBag,
    User,
  },
};
</script>
