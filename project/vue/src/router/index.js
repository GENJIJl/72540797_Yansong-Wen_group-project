import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)


const routes = [
  {path: '/register', name: 'register', component: () => import('../views/Register.vue')},
  {path: '/login', name: 'login', component: () => import('../views/Login.vue')},
  {path: '/profile', name: 'UserProfile', component: () => import('../views/front.vue')},
  {path: '/student/resources', name: 'StudentResources', component: () => import('../views/student/StudentResource.vue')},


  {path: '/manager', name: 'manager', component: () => import('../views/managerlayout.vue'),
    children:[
      {path: 'home', component: () => import('../views/manager/HomeView.vue')},
      {path: 'about', component: () => import('../views/manager/AboutView.vue')},
      {path: 'admin', component: () => import('../views/manager/admin.vue')},
      {path: 'teacher', component: () => import('../views/manager/teacher.vue')},
      {path: 'grade', component: () => import('../views/manager/grade.vue')},
      {path: 'courseResource', component: () => import('../views/manager/courseResource.vue')},
      {path: 'message', component: () => import('../views/manager/message.vue')},
      {path: 'analysis', component: () => import('../views/manager/analysis.vue')},
    ]
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

// 路由守卫：
//
// 重定向到登录页面：如果访问的是根路径（/），则重定向到登录页面（/login）。
//
// 检查用户是否登录：如果用户没有登录（即localStorage中没有user），并且要访问的页面不是登录页面、注册页面或首页，则重定向到登录页面。
//
//如果以上条件都不满足，则放行，允许访问。

router.beforeEach((to ,from, next) => {
  if (to.path === '/') {
    return next("/login");
  }
  let user = localStorage.getItem("user");
  if (!user && to.path !== '/login' && to.path !== '/register' && to.path !== '/front/index') {
    return next("/login");
  }


  next();
})

export default router