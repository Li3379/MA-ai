<template>
  <div class="home">
    <el-row :gutter="24">
      <el-col :span="16">
        <el-card shadow="never" class="home__hero">
          <h1>欢迎使用 ma-ai 医疗助手</h1>
          <p>智能健康咨询、个性化运动方案、社区互助一站式平台。</p>
          <el-button type="primary" size="large" @click="goConsult">立即体验健康咨询</el-button>
        </el-card>

        <el-row :gutter="24" class="home__features">
          <el-col :span="8" v-for="item in features" :key="item.title">
            <el-card shadow="hover">
              <h3>{{ item.title }}</h3>
              <p>{{ item.desc }}</p>
            </el-card>
          </el-col>
        </el-row>
      </el-col>

      <el-col :span="8">
        <el-card shadow="hover">
          <h3>快速入口</h3>
          <el-menu class="home__menu" @select="handleSelect">
            <el-menu-item index="/consult">健康咨询</el-menu-item>
            <el-menu-item index="/exercise">运动方案</el-menu-item>
            <el-menu-item index="/community">社区互助</el-menu-item>
          </el-menu>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

const features = [
  { title: 'AI 健康咨询', desc: '基于通义千问与医疗知识库，提供可信赖的健康建议。' },
  { title: '个性化运动方案', desc: '结合个人健康档案，智能生成科学的运动计划。' },
  { title: '社区互助', desc: '与医疗专家和其他用户交流经验，获取更多支持。' }
];

const goConsult = () => {
  router.push('/consult');
};

const handleSelect = (path: string) => {
  router.push(path);
};
</script>

<style scoped lang="scss">
.home {
  &__hero {
    margin-bottom: 24px;
  }

  &__features {
    margin-top: 0;
  }

  &__menu {
    border-right: none;
  }
}
</style>
