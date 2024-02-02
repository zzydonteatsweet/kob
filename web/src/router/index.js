import { createRouter, createWebHistory } from 'vue-router'
import NotFound from '../views/error/NotFound.vue'
import PkIndexView from '../views/pk/PkIndexView'
import UserBotsIndexView from '../views/user/bots/UserBotsIndexView'
import RankListIndexView from '../views/ranklist/RankListIndexView'
import RecordIndexView from '../views/record/RecordIndexView'
import UserAccountLoginView from '../views/user/account/UserAccountLoginView'
import UserAccountRegisterView from '../views/user/account/UserAccountRegisterView'
import store from '../store/index' ;
const routes = [
  {
    path: '/',
    name: 'Home',
    // component: HomeView
    redirect: '/pk/',
    meta: {
      requestAuth: true,
    }

  },
  {
    path: '/pk/',
    name: 'Pk_Index',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    // component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
    component: PkIndexView,
    meta: {
      requestAuth: true,
    }


  },
  {
    path:'/rank_list/',
    name:'Rank_List',
    component:RankListIndexView,
    meta: {
      requestAuth: true,
    }


  },
  {
    path: '/record/',
    name: 'Record_Index',
    component:RecordIndexView ,
    meta: {
      requestAuth: true,
    }


  },
  {
    path: '/user/bot/',
    name: 'User_Bot_Index',
    component: UserBotsIndexView,
    meta: {
      requestAuth: true,
    }


  },
  {
    path: '/user/account/login/',
    name: 'user_account_login',
    component: UserAccountLoginView,
    meta: {
      requestAuth: false,
    }


  },
  {
    path: '/user/account/register/',
    name: 'user_account_register',
    component: UserAccountRegisterView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: '/404/',
    name: 'Not_Found_Index',
    component: NotFound ,
    meta: {
      requestAuth: false,
    }
  },
  {
    path:'/:catchAll(.*)',
    redirect: '/404/',

  }
  
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if(to.meta.requestAuth && !store.state.user.is_login) {
    next({name: 'user_account_login'}) ;
  }else {
    next() ;
  }
})
export default router
