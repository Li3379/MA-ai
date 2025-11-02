import { ensureSupabaseClient } from './supabase';

export interface CommunityPost {
  id: string;
  title: string;
  content: string;
  category: string;
  like_count: number;
  comment_count: number;
  created_at: string;
}

export async function listCommunityPosts() {
  const supabase = ensureSupabaseClient();
  const { data, error } = await supabase
    .from<CommunityPost>('community_posts')
    .select('*')
    .order('created_at', { ascending: false })
    .limit(20);
  if (error) {
    throw error;
  }
  return data ?? [];
}

export async function createCommunityPost(payload: { title: string; content: string; category: string }) {
  const supabase = ensureSupabaseClient();
  const { data, error } = await supabase.from('community_posts').insert(payload).select().single();
  if (error) {
    throw error;
  }
  return data as CommunityPost;
}

export function subscribeChatRoom(roomId: string, callback: (payload: any) => void) {
  const supabase = ensureSupabaseClient();
  return supabase
    .channel(`chat-room-${roomId}`)
    .on('postgres_changes', { event: 'INSERT', schema: 'public', table: 'chat_messages', filter: `room_id=eq.${roomId}` }, callback)
    .subscribe();
}
