<template>
  <div class="login">
    <el-row justify="center">
      <el-col :span="8">
        <el-card
          class="login-card"
          shadow="hover"
        >
          <template #header>
            <div class="login-header">
              <el-icon
                size="30"
                color="#409eff"
                ><User
              /></el-icon>
              <h2>登錄</h2>
            </div>
          </template>
          <el-form
            ref="loginFormRef"
            :model="form"
            :rules="rules"
            label-width="80px"
            @submit.prevent="handleLogin"
          >
            <el-form-item
              label="郵箱"
              prop="email"
            >
              <el-input
                v-model="form.email"
                placeholder="請輸入郵箱"
                prefix-icon="Message"
              />
            </el-form-item>
            <el-form-item
              label="密碼"
              prop="password"
            >
              <el-input
                v-model="form.password"
                type="password"
                placeholder="請輸入密碼"
                prefix-icon="Lock"
                show-password
              />
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                :loading="loading"
                @click="handleLogin"
                style="width: 100%"
              >
                登錄
              </el-button>
            </el-form-item>
          </el-form>
          <div class="register-link">
            還沒有帳號？
            <router-link to="/register">立即註冊</router-link>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { User } from "@element-plus/icons-vue";
import userApi from "../api/user.js";

export default {
  name: "Login",
  components: {
    User,
  },
  data() {
    return {
      loading: false,
      form: {
        email: "",
        password: "",
      },
      rules: {
        email: [
          { required: true, message: "請輸入郵箱", trigger: "blur" },
          { type: "email", message: "請輸入正確的郵箱格式", trigger: "blur" },
        ],
        password: [
          { required: true, message: "請輸入密碼", trigger: "blur" },
          { min: 0, message: "密碼長度不能少於6位", trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    async handleLogin() {
      this.$refs.loginFormRef.validate(async (valid) => {
        if (!valid) return;

        this.loading = true;
        try {
          const response = await userApi.login(this.form);
          const { token, role, userId } = response.data;

          localStorage.setItem("token", token);
          localStorage.setItem("userRole", role);
          localStorage.setItem("userId", userId);

          localStorage.setItem("userEmail", this.form.email);
          this.$message.success("登錄成功");
          this.$router.push("/products").then(() => {
            window.location.reload();
          });
        } catch (error) {
          console.error(error);
          this.$message.error("登錄失敗，請檢查帳號密碼");
        } finally {
          this.loading = false;
        }
      });
    },
  },
};
</script>



