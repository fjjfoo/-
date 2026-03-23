import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/services',
    name: 'Services',
    component: () => import('../views/Services.vue'),
    meta: { title: '服务列表' }
  },
  {
    path: '/service/:id',
    name: 'ServiceDetail',
    component: () => import('../views/ServiceDetail.vue'),
    meta: { title: '服务详情' }
  },
  {
    path: '/orders',
    name: 'Orders',
    component: () => import('../views/Orders.vue'),
    meta: { title: '我的订单', requiresAuth: true }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('../views/Profile.vue'),
    meta: { title: '个人中心', requiresAuth: true }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('../views/admin/Index.vue'),
    meta: { title: '管理员后台', requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: '/admin/dashboard',
        name: 'AdminDashboard',
        component: () => import('../views/admin/Dashboard.vue'),
        meta: { title: '数据概览' }
      },
      {
        path: '/admin/services',
        name: 'AdminServices',
        component: () => import('../views/admin/Services.vue'),
        meta: { title: '服务管理' }
      },
      {
        path: '/admin/orders',
        name: 'AdminOrders',
        component: () => import('../views/admin/Orders.vue'),
        meta: { title: '订单管理' }
      },
      {
        path: '/admin/users',
        name: 'AdminUsers',
        component: () => import('../views/admin/Users.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: '/admin/staff',
        name: 'AdminStaff',
        component: () => import('../views/admin/Staff.vue'),
        meta: { title: '服务师管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title || '美容服务预约系统'
  
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.meta.requiresAdmin && localStorage.getItem('role') !== 'admin') {
    next('/')
  } else {
    next()
  }
})

export default router
