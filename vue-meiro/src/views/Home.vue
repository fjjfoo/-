<template>
  <div class="home-container">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>美容服务预约系统</h1>
          <div class="header-nav">
            <el-link type="primary" @click="$router.push('/services')">服务列表</el-link>
            <el-link type="primary" @click="$router.push('/orders')" v-if="userStore.token">我的订单</el-link>
            <el-link type="primary" @click="$router.push('/profile')" v-if="userStore.token">个人中心</el-link>
            <el-button type="primary" @click="$router.push('/login')" v-if="!userStore.token">登录</el-button>
            <el-button type="danger" @click="handleLogout" v-else>退出</el-button>
          </div>
        </div>
      </el-header>
      
      <el-main>
        <div class="banner">
          <h2>专业美容服务，让您焕发美丽</h2>
          <p>我们提供面部护理、身体护理、美甲美睫等多种美容服务</p>
          <el-button type="primary" size="large" @click="$router.push('/services')">立即预约</el-button>
        </div>
        
        <div class="service-categories">
          <h3>服务分类</h3>
          <el-row :gutter="20">
            <el-col :span="6" v-for="category in categories" :key="category.value">
              <el-card class="category-card" @click="handleCategoryClick(category.value)">
                <div class="category-icon">
                  <el-icon :size="40"><component :is="category.icon" /></el-icon>
                </div>
                <h4>{{ category.label }}</h4>
                <p>{{ category.desc }}</p>
              </el-card>
            </el-col>
          </el-row>
        </div>
        
        <div class="featured-services">
          <h3>热门服务</h3>
          <el-row :gutter="20">
            <el-col :span="8" v-for="service in featuredServices" :key="service.id">
              <el-card class="service-card" @click="$router.push(`/service/${service.id}`)">
                <img :src="service.servicePhoto" class="service-image" v-if="service.servicePhoto">
                <div class="service-info">
                  <h4>{{ service.serviceName }}</h4>
                  <p class="service-price">¥{{ service.serviceNewMoney }}</p>
                  <p class="service-duration">{{ service.serviceDuration }}分钟</p>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-main>
      
      <el-footer>
        <p>© 2024 美容服务预约系统 版权所有</p>
      </el-footer>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getServiceList } from '@/api/service'
import { User, Brush, MagicStick, Star } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const categories = ref([
  { label: '面部护理', value: 1, icon: 'User', desc: '深层清洁、补水保湿' },
  { label: '身体护理', value: 2, icon: 'Brush', desc: '全身放松、肌肤护理' },
  { label: '美甲美睫', value: 3, icon: 'MagicStick', desc: '精致美甲、自然美睫' },
  { label: '其他服务', value: 4, icon: 'Star', desc: '更多特色服务' }
])

const featuredServices = ref([])

const handleCategoryClick = (category) => {
  router.push({ path: '/services', query: { serviceCategory: category } })
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

const loadFeaturedServices = async () => {
  try {
    const res = await getServiceList({ page: 1, limit: 3, shangxiaTypes: 1 })
    featuredServices.value = res.page.list || []
  } catch (error) {
    console.error('加载热门服务失败:', error)
  }
}

onMounted(() => {
  loadFeaturedServices()
})
</script>

<style scoped>
.home-container {
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

.banner {
  text-align: center;
  padding: 80px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  margin-bottom: 40px;
}

.banner h2 {
  font-size: 36px;
  margin-bottom: 20px;
}

.banner p {
  font-size: 18px;
  margin-bottom: 30px;
}

.service-categories,
.featured-services {
  margin-bottom: 40px;
}

.service-categories h3,
.featured-services h3 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.category-card {
  text-align: center;
  cursor: pointer;
  transition: transform 0.3s;
  margin-bottom: 20px;
}

.category-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.category-icon {
  margin-bottom: 15px;
  color: #667eea;
}

.service-card {
  cursor: pointer;
  transition: transform 0.3s;
  margin-bottom: 20px;
  overflow: hidden;
}

.service-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.service-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.service-info {
  padding: 15px;
}

.service-info h4 {
  margin: 0 0 10px 0;
  color: #333;
}

.service-price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
  margin: 5px 0;
}

.service-duration {
  color: #909399;
  font-size: 14px;
}

.el-footer {
  text-align: center;
  background: #f5f5f5;
  padding: 20px;
  color: #909399;
}
</style>
