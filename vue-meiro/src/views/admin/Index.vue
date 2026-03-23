<template>
  <div class="admin-container">
    <el-container class="full-height">
      <el-aside width="200px" class="aside-container">
        <div class="logo">
          <h2>美容院预约管理后台</h2>
        </div>
        <el-menu
          :default-active="activeMenu"
          router
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
          class="menu-container"
        >
          <el-menu-item index="/admin/dashboard">
            <el-icon><DataAnalysis /></el-icon>
            <span>数据概览</span>
          </el-menu-item>
          <el-menu-item index="/admin/users">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/services">
            <el-icon><Brush /></el-icon>
            <span>服务管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/orders">
            <el-icon><Document /></el-icon>
            <span>订单管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/staff">
            <el-icon><Avatar /></el-icon>
            <span>服务师管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <el-container class="main-container">
        <el-header class="header-container">
          <div class="header-content">
            <span>欢迎，管理员</span>
            <el-button type="danger" @click="handleLogout">退出登录</el-button>
          </div>
        </el-header>
        
        <el-main class="content-container">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { DataAnalysis, User, Brush, Document, Avatar } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

// 监听窗口大小变化，确保布局自适应
const handleResize = () => {
  // 可以在这里添加需要的响应式逻辑
}

onMounted(() => {
  window.addEventListener('resize', handleResize)
})

// 组件卸载时移除事件监听器
const beforeUnmount = () => {
  window.removeEventListener('resize', handleResize)
}
</script>

<style scoped>
.admin-container {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

.full-height {
  height: 100%;
}

.aside-container {
  background-color: #545c64;
  height: 100%;
  overflow-y: auto;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  border-bottom: 1px solid #434a50;
}

.logo h2 {
  margin: 0;
  font-size: 18px;
}

.menu-container {
  height: calc(100% - 60px);
  overflow-y: auto;
}

.main-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.header-container {
  background-color: #fff;
  border-bottom: 1px solid #dcdfe6;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 60px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.content-container {
  background-color: #f0f2f5;
  padding: 20px;
  flex: 1;
  overflow-y: auto;
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
  .aside-container {
    width: 180px !important;
  }
  
  .content-container {
    padding: 10px;
  }
}

@media screen and (max-width: 480px) {
  .aside-container {
    width: 160px !important;
  }
  
  .logo h2 {
    font-size: 16px;
  }
  
  .header-content span {
    font-size: 14px;
  }
}
</style>
