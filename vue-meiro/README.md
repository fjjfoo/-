# 美容服务预约系统 - 前端

## 项目简介

这是一个基于 Vue 3 的美容服务预约系统前端项目，实现了用户注册登录、服务浏览、在线预约、订单管理以及管理员后台管理等功能。

## 技术栈

- Vue 3 (Composition API)
- Vue Router 4
- Pinia (状态管理)
- Element Plus (UI组件库)
- Axios (HTTP客户端)
- Vite (构建工具)

## 项目结构

```
vue-meiro/
├── src/
│   ├── api/                   # API接口
│   │   ├── user.js           # 用户相关接口
│   │   ├── service.js        # 服务相关接口
│   │   └── order.js         # 订单相关接口
│   ├── assets/                # 静态资源
│   ├── components/            # 公共组件
│   ├── router/                # 路由配置
│   │   └── index.js
│   ├── stores/                # 状态管理
│   │   ├── user.js           # 用户状态
│   │   └── counter.js
│   ├── utils/                 # 工具函数
│   │   └── request.js        # Axios封装
│   ├── views/                 # 页面组件
│   │   ├── admin/            # 管理员后台
│   │   │   ├── Index.vue     # 后台主页面
│   │   │   ├── Dashboard.vue # 数据概览
│   │   │   ├── Services.vue  # 服务管理
│   │   │   ├── Orders.vue    # 订单管理
│   │   │   └── Users.vue     # 用户管理
│   │   ├── Login.vue         # 登录页
│   │   ├── Register.vue      # 注册页
│   │   ├── Home.vue          # 首页
│   │   ├── Services.vue      # 服务列表
│   │   ├── ServiceDetail.vue # 服务详情
│   │   ├── Orders.vue        # 订单列表
│   │   └── Profile.vue       # 个人中心
│   ├── App.vue
│   └── main.js
├── public/                   # 公共资源
├── package.json
├── vite.config.js
└── index.html
```

## 功能模块

### 用户端功能
1. **用户注册/登录** - 支持用户注册和登录功能
2. **服务浏览** - 浏览美容服务列表，支持分类筛选
3. **服务详情** - 查看服务详细信息
4. **在线预约** - 选择服务、日期、时段进行预约
5. **订单管理** - 查看订单状态、取消订单、评价订单
6. **个人中心** - 管理个人信息、修改密码

### 管理员后台功能
1. **数据概览** - 查看系统统计数据
2. **用户管理** - 管理用户信息、重置密码
3. **服务管理** - 管理美容服务项目
4. **订单管理** - 处理订单、确认订单、完成订单

## 快速开始

### 环境要求
- Node.js 18+
- npm 或 yarn

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

### 构建生产版本

```bash
npm run build
```

### 预览生产版本

```bash
npm run preview
```

## 配置说明

### API地址配置

API基础URL配置在 `src/utils/request.js` 中：

```javascript
const request = axios.create({
  baseURL: 'http://localhost:8080/tiyuguan',
  timeout: 10000
})
```

请根据实际后端服务地址修改此配置。

### 路由配置

路由配置在 `src/router/index.js` 中，包含以下路由：

- `/` - 首页
- `/login` - 登录页
- `/register` - 注册页
- `/services` - 服务列表
- `/service/:id` - 服务详情
- `/orders` - 我的订单
- `/profile` - 个人中心
- `/admin` - 管理员后台

## 默认账号

### 管理员账号
- 用户名: `admin`
- 密码: `admin`

### 测试用户
- 用户名: `test`
- 密码: `123456`

## 开发说明

### 使用 Composition API

所有组件都使用 Vue 3 的 Composition API 编写：

```vue
<script setup>
import { ref, onMounted } from 'vue'

const data = ref([])

const loadData = async () => {
  // 加载数据逻辑
}

onMounted(() => {
  loadData()
})
</script>
```

### 使用 Pinia 状态管理

用户状态存储在 `src/stores/user.js` 中：

```javascript
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

// 获取用户信息
console.log(userStore.userInfo)

// 退出登录
userStore.logout()
```

### 使用 Element Plus

项目使用 Element Plus 作为UI组件库：

```vue
<template>
  <el-button type="primary">按钮</el-button>
  <el-input v-model="value" placeholder="请输入" />
</template>
```

## 注意事项

1. 确保后端服务正常运行（默认端口8080）
2. 首次使用需要先注册用户或使用管理员账号登录
3. 部分功能需要用户登录后才能使用
4. 管理员后台需要管理员权限才能访问

## 许可证

MIT License
