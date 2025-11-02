<template>
  <div class="community">
    <el-row :gutter="24">
      <el-col :span="16">
        <el-card shadow="never" class="community__list">
          <template #header>
            <div class="community__header">
              <span>社区热议</span>
              <el-button type="primary" @click="showDialog = true">发起话题</el-button>
            </div>
          </template>

          <el-skeleton :loading="loading" animated :count="posts.length ? 0 : 3">
            <template #template>
              <el-skeleton-item variant="h3" style="width: 60%" />
              <el-skeleton-item variant="text" />
              <el-skeleton-item variant="text" />
              <el-divider />
            </template>

            <template #default>
              <el-empty v-if="!posts.length" description="暂无帖子，快来发布第一条吧" />
              <el-space v-else direction="vertical" :size="16" style="width: 100%">
                <el-card
                  v-for="post in posts"
                  :key="post.id"
                  shadow="hover"
                >
                  <div class="community__post">
                    <div class="community__post-header">
                      <el-tag size="small">{{ post.category }}</el-tag>
                      <span class="community__post-time">{{ post.created_at }}</span>
                    </div>
                    <h3>{{ post.title }}</h3>
                    <p class="community__post-content">{{ post.content }}</p>
                    <div class="community__post-footer">
                      <span>👍 {{ post.like_count }}</span>
                      <span>💬 {{ post.comment_count }}</span>
                    </div>
                  </div>
                </el-card>
              </el-space>
            </template>
          </el-skeleton>
        </el-card>

        <el-card shadow="hover">
          <template #header>
            <span>实时聊天（演示）</span>
          </template>

          <div class="community__chat">
            <el-empty description="将通过 Supabase Realtime 实现实时聊天" />
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <span>健康专题</span>
          </template>

          <el-timeline>
            <el-timeline-item timestamp="每周一" placement="top">
              <h4>慢性病管理分享</h4>
              <p>邀请内分泌科专家解读糖尿病日常管理要点。</p>
            </el-timeline-item>
            <el-timeline-item timestamp="每周三" placement="top">
              <h4>心理健康沙龙</h4>
              <p>关注情绪管理，提升心理韧性。</p>
            </el-timeline-item>
            <el-timeline-item timestamp="每周五" placement="top">
              <h4>家庭运动课堂</h4>
              <p>学习居家可执行的运动训练，增强体质。</p>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="showDialog" title="发起话题" width="480px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="一句话概括你的话题" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" placeholder="请选择类别">
            <el-option label="慢病管理" value="慢病管理" />
            <el-option label="心理健康" value="心理健康" />
            <el-option label="营养膳食" value="营养膳食" />
            <el-option label="运动康复" value="运动康复" />
          </el-select>
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="4" placeholder="分享你的问题或经验" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" :loading="createLoading" @click="handleCreate">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { createCommunityPost, listCommunityPosts, type CommunityPost } from '@/api/community';

const loading = ref(false);
const createLoading = ref(false);
const posts = ref<CommunityPost[]>([]);
const showDialog = ref(false);

const form = reactive({
  title: '',
  category: '慢病管理',
  content: ''
});

const fetchPosts = async () => {
  try {
    loading.value = true;
    posts.value = await listCommunityPosts();
  } catch (error: any) {
    ElMessage.error(error?.message || '获取社区内容失败');
  } finally {
    loading.value = false;
  }
};

const handleCreate = async () => {
  if (!form.title || !form.content) {
    ElMessage.warning('请填写完整信息');
    return;
  }
  try {
    createLoading.value = true;
    const newPost = await createCommunityPost({
      title: form.title,
      category: form.category,
      content: form.content
    });
    posts.value = [newPost, ...posts.value];
    showDialog.value = false;
    form.title = '';
    form.content = '';
    ElMessage.success('发布成功');
  } catch (error: any) {
    ElMessage.error(error?.message || '发布失败，请检查 Supabase 配置');
  } finally {
    createLoading.value = false;
  }
};

onMounted(() => {
  fetchPosts();
});
</script>

<style scoped lang="scss">
.community {
  display: flex;
  flex-direction: column;
  gap: 24px;

  &__list {
    margin-bottom: 24px;
  }

  &__header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  &__post {
    display: flex;
    flex-direction: column;
    gap: 8px;

    &-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 12px;
      color: #909399;
    }

    &-content {
      color: #606266;
      line-height: 1.6;
      word-break: break-word;
      white-space: pre-wrap;
    }

    &-footer {
      display: flex;
      gap: 16px;
      color: #909399;
      font-size: 13px;
    }
  }

  &__chat {
    min-height: 200px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
</style>
