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
          <el-row :gutter="20">
            <el-col :span="8">
              <el-button
                type="primary"
                size="large"
                @click="$router.push('/products')"
              >
                <el-icon><ShoppingBag /></el-icon>
                瀏覽產品
              </el-button>
            </el-col>
            <el-col :span="8">
              <el-button
                type="success"
                size="large"
                @click="$router.push('/login')"
              >
                <el-icon><User /></el-icon>
                登錄
              </el-button>
            </el-col>
            <el-col :span="8">
              <el-button
                type="info"
                size="large"
                @click="$router.push('/register')"
              >
                <el-icon><UserPlus /></el-icon>
                註冊
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

<style scoped>
.home {
  padding: 40px 0;
}

.welcome-card {
  text-align: center;
}

.welcome-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.welcome-header h1 {
  margin: 0;
  color: #409eff;
}
</style>
