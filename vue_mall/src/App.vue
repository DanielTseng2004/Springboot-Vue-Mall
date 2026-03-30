<template>
  <div id="app">
    <NavBar />

    <el-container class="main-container">
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "App",
  // 不再需要 components: { NavBar }，因為已在 main.js 全域註冊
};
</script>

