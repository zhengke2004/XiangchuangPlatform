<template>
  <div class="not-found">
    <div class="container">
      <div class="not-found-content">
        <h1>404</h1>
        <p>页面未找到</p>
        <p>抱歉，您访问的页面不存在</p>
        <router-link to="/" class="go-home-btn">返回首页</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.not-found {
  min-height: calc(100vh - 60px);
  display: flex;
  align-items: center;
  justify-content: center;
}

.not-found-content {
  text-align: center;
}

.not-found-content h1 {
  font-size: 120px;
  color: #4a8c3a;
  margin-bottom: 20px;
}

.not-found-content p {
  font-size: 18px;
  color: #666;
  margin-bottom: 10px;
}

.go-home-btn {
  display: inline-block;
  background: #4a8c3a;
  color: white;
  padding: 12px 30px;
  border-radius: 4px;
  margin-top: 20px;
}
</style>