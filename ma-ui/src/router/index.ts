import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('../views/HomeView.vue')
  },
  {
    path: '/consult',
    name: 'Consult',
    component: () => import('../views/ConsultView.vue')
  },
  {
    path: '/exercise',
    name: 'Exercise',
    component: () => import('../views/ExerciseView.vue')
  },
  {
    path: '/community',
    name: 'Community',
    component: () => import('../views/CommunityView.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/AuthView.vue')
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
