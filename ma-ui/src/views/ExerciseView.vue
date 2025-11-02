<template>
  <div class="exercise">
    <el-row :gutter="24">
      <el-col :span="10">
        <el-card shadow="never">
          <template #header>
            <span>个人健康档案</span>
          </template>

          <el-form :model="profile" label-width="92px" :rules="rules" ref="formRef">
            <el-form-item label="年龄" prop="age">
              <el-input-number v-model="profile.age" :min="1" :max="120" />
            </el-form-item>
            <el-form-item label="身高(cm)" prop="height">
              <el-input-number v-model="profile.height" :min="50" :max="250" />
            </el-form-item>
            <el-form-item label="体重(kg)" prop="weight">
              <el-input-number v-model="profile.weight" :min="20" :max="200" />
            </el-form-item>
            <el-form-item label="慢性病史" prop="chronicDiseases">
              <el-input v-model="profile.chronicDiseases" placeholder="如高血压、糖尿病等" />
            </el-form-item>
            <el-form-item label="运动目标" prop="goal">
              <el-select v-model="profile.goal" placeholder="请选择">
                <el-option label="减脂" value="fat_loss" />
                <el-option label="增肌" value="muscle_gain" />
                <el-option label="康复" value="rehabilitation" />
                <el-option label="保持健康" value="general_health" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="loading" @click="generatePlan">生成方案</el-button>
              <el-button @click="reset">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <el-col :span="14">
        <el-card shadow="hover">
          <template #header>
            <span>智能运动方案</span>
          </template>

          <div v-if="plan" class="exercise__plan">
            <h3>方案概述</h3>
            <p>{{ plan.summary }}</p>

            <h3>每日安排</h3>
            <el-timeline>
              <el-timeline-item
                v-for="(day, index) in plan.weeklySchedule"
                :key="index"
                :timestamp="day.day"
              >
                <ul>
                  <li v-for="item in day.activities" :key="item">{{ item }}</li>
                </ul>
              </el-timeline-item>
            </el-timeline>

            <h3>注意事项</h3>
            <ul>
              <li v-for="tip in plan.precautions" :key="tip">{{ tip }}</li>
            </ul>
          </div>
          <div v-else class="exercise__placeholder">
            <el-empty description="请先完善健康档案并生成运动方案" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import type { FormInstance, FormRules } from 'element-plus';
import { ElMessage } from 'element-plus';
import { requestExercisePlan } from '@/api/exercise';

interface HealthProfile {
  age: number | null;
  height: number | null;
  weight: number | null;
  chronicDiseases: string;
  goal: string;
}

interface ExercisePlan {
  summary: string;
  weeklySchedule: Array<{ day: string; activities: string[] }>;
  precautions: string[];
}

const formRef = ref<FormInstance>();
const loading = ref(false);
const plan = ref<ExercisePlan | null>(null);

const profile = reactive<HealthProfile>({
  age: null,
  height: null,
  weight: null,
  chronicDiseases: '',
  goal: ''
});

const rules: FormRules = {
  age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
  height: [{ required: true, message: '请输入身高', trigger: 'blur' }],
  weight: [{ required: true, message: '请输入体重', trigger: 'blur' }],
  goal: [{ required: true, message: '请选择运动目标', trigger: 'change' }]
};

const generatePlan = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid) => {
    if (!valid) return;
    loading.value = true;
    try {
      const response = await requestExercisePlan({
        age: profile.age!,
        height: profile.height!,
        weight: profile.weight!,
        chronicDiseases: profile.chronicDiseases,
        goal: profile.goal,
        availableDays: 5
      });
      const content = response.content;
      try {
        const parsed = JSON.parse(content);
        plan.value = {
          summary: parsed.summary ?? '请结合自身情况执行该方案。',
          weeklySchedule: (parsed.weekly_schedule ?? []).map((item: any) => ({
            day: item.day,
            activities: item.activities ?? []
          })),
          precautions: parsed.precautions ?? []
        };
      } catch (error) {
        plan.value = {
          summary: content,
          weeklySchedule: [],
          precautions: []
        };
      }
    } catch (error: any) {
      ElMessage.error(error?.message || '生成方案失败，请稍后再试');
    } finally {
      loading.value = false;
    }
  });
};

const reset = () => {
  plan.value = null;
  formRef.value?.resetFields();
};
</script>

<style scoped lang="scss">
.exercise {
  &__plan {
    line-height: 1.8;

    ul {
      margin: 0;
    }
  }

  &__placeholder {
    padding: 48px 0;
  }
}
</style>
