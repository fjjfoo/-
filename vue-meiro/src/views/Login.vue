<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2>美容服务预约系统</h2>
      </template>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" prefix-icon="User" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width: 100%">登录</el-button>
        </el-form-item>
        <el-form-item>
          <el-link type="primary" @click="$router.push('/register')">还没有账号？立即注册</el-link>
          <el-link type="info" @click="handleForgotPassword" style="margin-left: 10px">忘记密码？</el-link>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { login } from '@/api/user'

const router = useRouter()
const userStore = useUserStore()

const loginForm = ref({
  username: '',
  password: ''
})

const loading = ref(false)
const loginFormRef = ref(null)

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  try {
    await loginFormRef.value.validate()
    loading.value = true
    
    const res = await login(loginForm.value)
    
    if (res.token) {
      userStore.setToken(res.token)
      userStore.setRole(res.role)
      // 存储完整的用户信息
      userStore.setUserInfo({
        username: res.username,
        userId: res.userId,
        yonghuName: res.yonghuName,
        yonghuPhone: res.yonghuPhone,
        yonghuIdNumber: res.yonghuIdNumber,
        yonghuEmail: res.yonghuEmail,
        sexTypes: res.sexTypes,
        newMoney: res.newMoney
      })
      
      ElMessage.success('登录成功')
      
      if (res.role === 'admin') {
        router.push('/admin')
      } else {
        router.push('/')
      }
    }
  } catch (error) {
    console.error('登录失败:', error)
  } finally {
    loading.value = false
  }
}

const handleForgotPassword = () => {
  ElMessage.info('请联系管理员重置密码')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
  border-radius: 10px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.login-card :deep(.el-card__header) {
  text-align: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.login-card :deep(.el-card__header h2) {
  margin: 0;
  color: white;
}
</style>
