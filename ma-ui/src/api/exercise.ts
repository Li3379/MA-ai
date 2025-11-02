import http from './http';

export interface ExercisePlanPayload {
  age: number;
  height: number;
  weight: number;
  chronicDiseases?: string;
  goal: string;
  availableDays: number;
}

export interface ExercisePlanResponse {
  content: string;
}

export function requestExercisePlan(payload: ExercisePlanPayload) {
  return http.post<ExercisePlanResponse>('/api/ai/exercise/plan', payload);
}
