import http from './http';
import { ensureSupabaseClient } from './supabase';

export interface LoginPayload {
  email: string;
  password: string;
}

export interface RegisterPayload extends LoginPayload {
  confirmPassword?: string;
}

export async function login(payload: LoginPayload) {
  const supabase = ensureSupabaseClient();
  const { data, error } = await supabase.auth.signInWithPassword(payload);
  if (error) {
    throw error;
  }
  if (data.session?.access_token) {
    localStorage.setItem('maai_token', data.session.access_token);
  }
  return http.post('/auth/login', payload);
}

export async function register(payload: RegisterPayload) {
  const supabase = ensureSupabaseClient();
  const { data, error } = await supabase.auth.signUp({
    email: payload.email,
    password: payload.password
  });
  if (error) {
    throw error;
  }
  return http.post('/auth/register', payload, {
    headers: {
      'X-Supabase-Uid': data.user?.id ?? ''
    }
  });
}

export async function logout() {
  const supabase = ensureSupabaseClient();
  await supabase.auth.signOut();
  localStorage.removeItem('maai_token');
  return http.post('/auth/logout');
}
