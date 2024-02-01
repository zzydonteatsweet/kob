import { createRouter, createWebHistory } from 'vue-router'
import NotFound from '../views/error/NotFound.vue'
import PkIndexView from '../views/pk/PkIndexView'
import UserBotsIndexView from '../views/user/bots/UserBotsIndexView'
import RankListIndexView from '../views/ranklist/RankListIndexView'
import RecordIndexView from '../views/record/RecordIndexView'
import UserAccountLoginView from '../views/user/account/UserAccountLoginView'
import UserAccountRegisterView from '../views/user/account/UserAccountRegisterView'
const routes = [
  {
    path: '/',
    name: 'Home',
    // component: HomeView
    redirect: '/pk/',

  },
  {
    path: '/pk/',
    name: 'Pk_Index',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    // component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
    component: PkIndexView
  },
  {
    path:'/rank_list/',
    name:'Rank_List',
    component:RankListIndexView
  },
  {
    path: '/record/',
    name: 'Record_Index',
    component:RecordIndexView 
  },
  {
    path: '/user/bot/',
    name: 'User_Bot_Index',
    component: UserBotsIndexView
  },
  {
    path: '/user/account/login/',
    name: 'user_account_login',
    component: UserAccountLoginView
  },
  {
    path: '/user/account/register/',
    name: 'user_account_register',
    component: UserAccountRegisterView
  },
  {
    path: '/404/',
    name: 'Not_Found_Index',
    component: NotFound 
  },
  {
    path:'/:catchAll(.*)',
    redirect: '/404/'
  }
  
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
