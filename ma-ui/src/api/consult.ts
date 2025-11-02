import http from './http';

export interface ConsultMessage {
  consultationId?: number;
  question: string;
}

export interface ConsultResponse {
  answer: string;
}

export function sendConsultMessage(payload: ConsultMessage) {
  return http.post<ConsultResponse>('/api/ai/consult/chat', payload);
}
