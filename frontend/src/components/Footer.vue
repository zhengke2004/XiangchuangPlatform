<template>
  <footer class="footer">
    <div class="container">
      <div class="row">
        <div class="col-md-4 mb-4">
          <h5>乡创农游智能体平台</h5>
          <p>连接城乡，体验田园，助力乡村振兴</p>
        </div>
        <div class="col-md-4 mb-4">
          <h5>联系方式</h5>
          <p><FontAwesomeIcon icon="fa-solid fa-envelope" class="mr-2"/>contact@xiangchuang.com</p>
          <p><FontAwesomeIcon icon="fa-solid fa-phone" class="mr-2"/>400-123-4567</p>
        </div>
        <div class="col-md-4 mb-4">
          <h5>关注我们</h5>
          <div>
            <a href="#" class="text-white mr-3"><FontAwesomeIcon icon="fa-brands fa-weixin" class="fa-2x"/></a>
            <a href="#" class="text-white mr-3"><FontAwesomeIcon icon="fa-brands fa-weibo" class="fa-2x"/></a>
            <a href="#" class="text-white"><FontAwesomeIcon icon="fa-brands fa-tiktok" class="fa-2x"/></a>
          </div>
        </div>
      </div>
      <hr class="bg-white">
      <p class="text-center mb-0">版权所有 &copy; 2025 乡创农游智能体平台</p>
    </div>
  </footer>
</template>

<script setup>
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
</script>

<style scoped>
.footer {
  background: rgb(56, 110, 46);
  color: white;
  padding: 40px 0;
  margin-top: 50px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.row {
  display: flex;
  flex-wrap: wrap;
  margin: 0 -15px;
}

.col-md-4 {
  flex: 0 0 33.3333%;
  max-width: 33.3333%;
  padding: 0 15px;
}

.mb-4 {
  margin-bottom: 24px;
}

.footer h5 {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 15px;
  color: white;
}

.footer p {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
  line-height: 1.8;
  margin: 0 0 10px 0;
}

.mr-2 {
  margin-right: 8px;
}

.text-white {
  color: white;
}

.text-white:hover {
  opacity: 0.8;
}

.fa-2x {
  font-size: 1.5em;
}

hr {
  margin: 20px 0;
  border: none;
  border-top: 1px solid rgba(255, 255, 255, 0.3);
}

.text-center {
  text-align: center;
}

.mb-0 {
  margin-bottom: 0;
}

@media (max-width: 768px) {
  .col-md-4 {
    flex: 0 0 100%;
    max-width: 100%;
  }
}
</style>