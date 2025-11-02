import { createClient } from '@supabase/supabase-js';

const supabaseUrl = import.meta.env.VITE_SUPABASE_URL || '';
const supabaseKey = import.meta.env.VITE_SUPABASE_ANON_KEY || '';

export const supabaseClient = supabaseUrl && supabaseKey
  ? createClient(supabaseUrl, supabaseKey)
  : null;

export const ensureSupabaseClient = () => {
  if (!supabaseClient) {
    throw new Error('Supabase 尚未配置，请在 .env 文件中设置 VITE_SUPABASE_URL 与 VITE_SUPABASE_ANON_KEY');
  }
  return supabaseClient;
};
