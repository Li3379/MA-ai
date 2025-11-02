<template>
  <div class="auth">
    <el-card class="auth__card" shadow="always">
      <template #header>
        <el-segmented v-model="mode" :options="modes" />
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" autocomplete="email" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" show-password placeholder="请输入密码" autocomplete="current-password" />
        </el-form-item>

        <el-form-item v-if="mode === 'register'" label="确认密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" show-password placeholder="请再次输入密码" autocomplete="new-password" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="loading" @click="submit">
            {{ mode === 'login' ? '登录' : '注册' }}
          </el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>

      <div class="auth__divider">
        <span>或使用 Supabase 社交登录（待集成）</span>
      </div>
      <el-button-group class="auth__social">
        <el-button disabled>微信</el-button>
        <el-button disabled>QQ</el-button>
        <el-button disabled>GitHub</el-button>
      </el-button-group>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import type { FormInstance, FormRules } from 'element-plus';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { login, register } from '@/api/auth';

const router = useRouter();
const mode = ref<'login' | 'register'>('login');
const modes = [
  { label: '登录', value: 'login' },
  { label: '注册', value: 'register' }
];

const formRef = ref<FormInstance>();
const loading = ref(false);

const form = reactive({
  email: '',
  password: '',
  confirmPassword: ''
});

const rules: FormRules = {
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [
    {
      validator: (_, value, callback) => {
        if (mode.value === 'register' && value !== form.password) {
          callback(new Error('两次输入的密码不一致'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ]
};

const submit = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid) => {
    if (!valid) return;
    loading.value = true;
    try {
      if (mode.value === 'login') {
        await login({
          email: form.email,
          password: form.password
        });
        ElMessage.success('登录成功');
        router.push('/home');
      } else {
        await register({
          email: form.email,
          password: form.password
        });
        ElMessage.success('注册成功，请前往邮箱完成验证');
        mode.value = 'login';
      }
    } catch (error: any) {
      ElMessage.error(error?.message || '操作失败，请稍后再试');
    } finally {
      loading.value = false;
    }
  });
};

const reset = () => {
  formRef.value?.resetFields();
};
</script>

<style scoped lang="scss">
.auth {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 480px;

  &__card {
    width: 420px;
  }

  &__divider {
    margin: 18px 0 12px;
    text-align: center;
    color: #909399;
  }

  &__social {
    display: flex;
    justify-content: center;
    width: 100%;
  }
}
</style>
