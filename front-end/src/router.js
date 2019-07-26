import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import ('./components/Home-Page/Home.vue')
    },
    {
      path: '/job-posts',
      name: 'job-posts',
      meta: { title: 'Job Posts' },
      component: () => import ('./components/Job-Posts-Page/JobPosts.vue')
    },
    {
      path: '/job-details/:id/:type',
      name: 'job-details',
      meta: { title: 'Job Details' },
      component: () => import ('./components/Job-Details-Page/JobDetails.vue')
    },
    {
      path: '/hr-expert',
      name: 'admin-page',
      meta: { title: 'Admin Page' },
      component: () => import ('./components/HR-Admin/AdminPage.vue')
    },
    {
      path: '/applications/:id',
      name: 'applications',
      meta: { title: 'Applications' },
      component: () => import ('./components/HR-Admin/Applications.vue')
    },
    {
      path: '/new-post',
      name: 'new-post',
      meta: { title: 'Create Post' },
      component: () => import ('./components/HR-Admin/NewPost.vue')
    },
    {
      path: '/profile/:id',
      name: 'user-profile',
      meta: { title: 'My Profile' },
      component: () => import ('./components/User-Profile/UserProfile.vue')
    },
    {
      path: '/hr-login',
      name: 'hr-login',
      meta: { title: 'Login' },
      component: () => import ('./components/Hr-Login/HrLogin.vue')
    },
    {
      path: '/talent-scout',
      name: 'talent-scout',
      meta: { title: 'Talent Scout' },
      component: () => import ('./components/HR-Admin/TalentScout.vue')
    }
  ]
})
