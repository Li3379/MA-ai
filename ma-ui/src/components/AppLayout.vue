<template>
  <el-config-provider :locale="zhCn">
    <div class="layout">
      <el-header class="layout__header">
        <div class="layout__logo">ma-ai 医疗助手</div>
        <el-menu mode="horizontal" :default-active="activeMenu" @select="handleSelect">
          <el-menu-item index="/home">首页</el-menu-item>
          <el-menu-item index="/consult">健康咨询</el-menu-item>
          <el-menu-item index="/exercise">运动方案</el-menu-item>
          <el-menu-item index="/community">社区互助</el-menu-item>
          <el-menu-item index="/login">登录/注册</el-menu-item>
        </el-menu>
      </el-header>
      <el-main class="layout__main">
        <RouterView />
      </el-main>
      <el-footer class="layout__footer">
        © {{ new Date().getFullYear() }} ma-ai. All rights reserved.
      </el-footer>
    </div>
  </el-config-provider>
</template>

<script setup lang="ts">
import zhCn from 'element-plus/dist/locale/zh-cn.mjs';
import { useRoute, useRouter } from 'vue-router';
import { computed } from 'vue';

const router = useRouter();
const route = useRoute();

const activeMenu = computed(() => route.path || '/home');

const handleSelect = (path: string) => {
  if (path !== route.path) {
    router.push(path);
  }
};
</script>

<style scoped lang="scss">
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.layout__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  background-color: #fff;
}

.layout__logo {
  font-size: 20px;
  font-weight: 600;
  color: #409eff;
}

.layout__main {
  flex: 1;
  background: #f5f7fa;
  padding: 24px;
}

.layout__footer {
  text-align: center;
  color: #909399;
  padding: 16px 0;
  background: #fff;
  border-top: 1px solid #ebeef5;
}
</style>
