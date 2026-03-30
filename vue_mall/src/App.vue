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

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  color: #2c3e50;
  margin: 0;
  padding: 0;
}

.main-container {
  /* 確保內容至少佔滿螢幕高度，扣除 NavBar 的 60px */
  min-height: calc(100vh - 60px);
  background-color: #f5f7fa; /* 給背景一個淺灰色，質感更好 */
}

.el-main {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}
</style>
