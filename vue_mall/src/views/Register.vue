<template>
  <div class="register">
    <el-row justify="center">
      <el-col :span="8">
        <el-card
          class="register-card"
          shadow="hover"
        >
          <template #header>
            <div class="register-header">
              <el-icon
                size="30"
                color="#67c23a"
                ><UserPlus
              /></el-icon>
              <h2>註冊</h2>
            </div>
          </template>
          <el-form
            ref="registerFormRef"
            :model="form"
            :rules="rules"
            label-width="80px"
            @submit.prevent="handleRegister"
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
            <el-form-item
              label="確認密碼"
              prop="confirmPassword"
            >
              <el-input
                v-model="form.confirmPassword"
                type="password"
                placeholder="請再次輸入密碼"
                prefix-icon="Lock"
                show-password
              />
            </el-form-item>
            <el-form-item
              label="角色"
              prop="role"
            >
              <el-select
                v-model="form.role"
                placeholder="請選擇角色"
              >
                <el-option
                  label="顧客"
                  value="CUSTOMER"
                />
                <el-option
                  label="賣家"
                  value="SELLER"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button
                type="success"
                :loading="loading"
                @click="handleRegister"
                style="width: 100%"
              >
                註冊
              </el-button>
            </el-form-item>
          </el-form>
          <div class="login-link">
            已有帳號？
            <router-link to="/login">立即登錄</router-link>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { UserPlus } from "@element-plus/icons-vue";
import userApi from "../api/user.js";

export default {
  name: "Register",
  components: {
    UserPlus,
  },
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.form.password) {
        callback(new Error("兩次輸入密碼不一致"));
      } else {
        callback();
      }
    };

    return {
      loading: false,
      form: {
        email: "",
        password: "",
        confirmPassword: "",
        role: "CUSTOMER",
      },
      rules: {
        email: [
          { required: true, message: "請輸入郵箱", trigger: "blur" },
          { type: "email", message: "請輸入正確的郵箱格式", trigger: "blur" },
        ],
        password: [
          { required: true, message: "請輸入密碼", trigger: "blur" },
          { min: 6, message: "密碼長度不能少於6位", trigger: "blur" },
        ],
        confirmPassword: [
          { required: true, message: "請確認密碼", trigger: "blur" },
          { validator: validateConfirmPassword, trigger: "blur" },
        ],
        role: [{ required: true, message: "請選擇角色", trigger: "change" }],
      },
    };
  },
  methods: {
    async handleRegister() {
      this.loading = true;
      try {
        const response = await userApi.register(this.form);
        this.$message.success("註冊成功");
        console.log("註冊成功", response.data);
        this.$router.push("/login");
      } catch (error) {
        this.$message.error(
          "註冊失敗：" + (error.response?.data?.message || error.message),
        );
        console.error("註冊失敗", error);
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>

<style scoped>
.register {
  padding: 40px 0;
}

.register-card {
  max-width: 400px;
  margin: 0 auto;
}

.register-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.register-header h2 {
  margin: 0;
  color: #67c23a;
}

.login-link {
  text-align: center;
  margin-top: 20px;
}

.login-link a {
  color: #409eff;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>
