<template>
  <div class="profile-container">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>个人中心</h1>
          <div class="header-nav">
            <el-link type="primary" @click="$router.push('/')">首页</el-link>
            <el-link type="primary" @click="$router.push('/services')">服务列表</el-link>
            <el-link type="primary" @click="$router.push('/orders')">我的订单</el-link>
            <el-button type="danger" @click="handleLogout">退出</el-button>
          </div>
        </div>
      </el-header>
      
      <el-main>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card class="profile-card">
              <div class="avatar-section">
                <el-avatar :size="100" :src="userInfo.yonghuPhoto">
                  {{ userInfo.yonghuName ? userInfo.yonghuName.charAt(0) : 'U' }}
                </el-avatar>
                <h3>{{ userInfo.yonghuName }}</h3>
                <p>余额：¥{{ userInfo.newMoney || 0 }}</p>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="16">
            <el-card class="info-card">
              <template #header>
                <h3>个人信息</h3>
              </template>
              <el-form :model="userInfo" :rules="rules" ref="profileFormRef" label-width="100px">
                <el-form-item label="用户名">
                  <el-input v-model="userInfo.username" disabled />
                </el-form-item>
                <el-form-item label="真实姓名" prop="yonghuName">
                  <el-input v-model="userInfo.yonghuName" />
                </el-form-item>
                <el-form-item label="手机号" prop="yonghuPhone">
                  <el-input v-model="userInfo.yonghuPhone" />
                </el-form-item>
                <el-form-item label="身份证号" prop="yonghuIdNumber">
                  <el-input v-model="userInfo.yonghuIdNumber" />
                </el-form-item>
                <el-form-item label="性别" prop="sexTypes">
                  <el-radio-group v-model="userInfo.sexTypes">
                    <el-radio :label="1">男</el-radio>
                    <el-radio :label="2">女</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="邮箱" prop="yonghuEmail">
                  <el-input v-model="userInfo.yonghuEmail" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleUpdate" :loading="updateLoading">保存修改</el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </el-col>
        </el-row>
        
        <el-card class="password-card" style="margin-top: 20px">
          <template #header>
            <h3>修改密码</h3>
          </template>
          <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="passwordForm.oldPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleUpdatePassword" :loading="passwordLoading">修改密码</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getUserInfo, updateProfile } from '@/api/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const userInfo = ref({})
const updateLoading = ref(false)
const passwordLoading = ref(false)
const profileFormRef = ref(null)
const passwordFormRef = ref(null)

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const rules = {
  yonghuName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  yonghuPhone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  yonghuIdNumber: [
    { required: true, message: '请输入身份证号', trigger: 'blur' }
  ],
  yonghuEmail: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== passwordForm.value.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const loadUserInfo = async () => {
  try {
    // 首先从本地存储的用户信息中获取
    if (userStore.userInfo && Object.keys(userStore.userInfo).length > 0) {
      userInfo.value = { ...userStore.userInfo }
    } else {
      // 如果本地没有用户信息，则从API获取
      const res = await getUserInfo()
      userInfo.value = res.data || {}
      // 更新本地存储的用户信息
      userStore.setUserInfo(userInfo.value)
    }
  } catch (error) {
    ElMessage.error('加载用户信息失败')
    console.error(error)
  }
}

const handleUpdate = async () => {
  try {
    await profileFormRef.value.validate()
    updateLoading.value = true
    
    await updateProfile(userInfo.value)
    
    ElMessage.success('修改成功')
    loadUserInfo()
  } catch (error) {
    console.error('修改失败:', error)
  } finally {
    updateLoading.value = false
  }
}

const handleUpdatePassword = async () => {
  try {
    await passwordFormRef.value.validate()
    passwordLoading.value = true
    
    ElMessage.success('密码修改成功，请重新登录')
    
    userStore.logout()
    router.push('/login')
  } catch (error) {
    console.error('修改密码失败:', error)
  } finally {
    passwordLoading.value = false
  }
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

onMounted(() => {
  if (!userStore.token) {
    router.push('/login')
    return
  }
  loadUserInfo()
})
</script>

<style scoped>
.profile-container {
  min-height: 100vh;
}

.el-header {
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  padding: 0 50px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.header-content h1 {
  margin: 0;
  color: #667eea;
}

.header-nav {
  display: flex;
  gap: 20px;
  align-items: center;
}

.profile-card {
  text-align: center;
}

.avatar-section {
  padding: 20px;
}

.avatar-section h3 {
  margin: 15px 0 10px 0;
  color: #333;
}

.avatar-section p {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
  margin: 0;
}

.info-card :deep(.el-card__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.info-card :deep(.el-card__header h3) {
  margin: 0;
  color: white;
}

.password-card :deep(.el-card__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.password-card :deep(.el-card__header h3) {
  margin: 0;
  color: white;
}
</style>
