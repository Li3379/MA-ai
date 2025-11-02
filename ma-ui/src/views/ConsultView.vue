<template>
  <div class="consult">
    <el-card class="consult__card">
      <template #header>
        <div class="consult__header">
          <span>AI 健康咨询</span>
          <el-button type="primary" @click="startNewSession">新建会话</el-button>
        </div>
      </template>

      <div class="consult__messages" ref="messageContainer">
        <div v-for="message in messages" :key="message.id" class="consult__message" :class="`consult__message--${message.role}`">
          <div class="consult__bubble">
            <p>{{ message.content }}</p>
            <small>{{ message.timestamp }}</small>
          </div>
        </div>
      </div>

      <div class="consult__input">
        <el-input
          v-model="input"
          type="textarea"
          :rows="3"
          resize="none"
          placeholder="请描述您的症状、用药史或健康问题，AI助理将提供参考建议"
        />
        <el-button type="primary" :loading="loading" @click="sendMessage">发送</el-button>
      </div>
    </el-card>

    <el-alert
      type="warning"
      title="本系统仅提供健康管理参考建议，不能代替医生诊断或治疗。如遇紧急情况，请及时就医。"
      show-icon
      class="consult__alert"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick } from 'vue';
import dayjs from 'dayjs';
import { sendConsultMessage } from '@/api/consult';
import { ElMessage } from 'element-plus';

interface Message {
  id: string;
  role: 'user' | 'assistant';
  content: string;
  timestamp: string;
}

const messages = ref<Message[]>([
  {
    id: 'welcome',
    role: 'assistant',
    content: '您好！我是 ma-ai 医疗助理。请描述您的健康问题或咨询需求，我会为您提供参考建议。',
    timestamp: dayjs().format('YYYY-MM-DD HH:mm')
  }
]);

const input = ref('');
const loading = ref(false);
const messageContainer = ref<HTMLDivElement | null>(null);

const startNewSession = () => {
  messages.value = messages.value.slice(0, 1);
};

const scrollToBottom = async () => {
  await nextTick();
  if (messageContainer.value) {
    messageContainer.value.scrollTop = messageContainer.value.scrollHeight;
  }
};

const sendMessage = async () => {
  if (!input.value.trim()) {
    return;
  }

  const question = input.value;
  const userMessage: Message = {
    id: crypto.randomUUID(),
    role: 'user',
    content: question,
    timestamp: dayjs().format('YYYY-MM-DD HH:mm')
  };
  messages.value.push(userMessage);
  input.value = '';
  await scrollToBottom();

  try {
    loading.value = true;
    const response = await sendConsultMessage({
      question,
      consultationId: undefined
    });
    const assistantMessage: Message = {
      id: crypto.randomUUID(),
      role: 'assistant',
      content: response.answer || 'AI 正在处理中，请稍后重试。',
      timestamp: dayjs().format('YYYY-MM-DD HH:mm')
    };
    messages.value.push(assistantMessage);
    await scrollToBottom();
  } catch (error: any) {
    ElMessage.error(error?.message || '咨询失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped lang="scss">
.consult {
  display: flex;
  flex-direction: column;
  gap: 16px;

  &__card {
    flex: 1;
  }

  &__header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  &__messages {
    height: 420px;
    overflow-y: auto;
    padding: 12px;
    background: #f5f7fa;
    border-radius: 4px;
  }

  &__message {
    display: flex;
    margin-bottom: 12px;

    &--user {
      justify-content: flex-end;
    }
  }

  &__bubble {
    max-width: 70%;
    padding: 12px;
    border-radius: 8px;
    background: #fff;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.06);

    small {
      display: block;
      margin-top: 8px;
      color: #909399;
      text-align: right;
    }
  }

  &__message--assistant .consult__bubble {
    background: #ecf5ff;
  }

  &__input {
    margin-top: 16px;
    display: flex;
    gap: 12px;
  }
}
</style>
