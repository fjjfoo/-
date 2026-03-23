<template>
  <div class="services-container">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>美容服务列表</h1>
          <div class="header-nav">
            <el-link type="primary" @click="$router.push('/')">首页</el-link>
            <el-link type="primary" @click="$router.push('/orders')" v-if="userStore.token">我的订单</el-link>
            <el-link type="primary" @click="$router.push('/profile')" v-if="userStore.token">个人中心</el-link>
            <el-button type="danger" @click="handleLogout" v-if="userStore.token">退出</el-button>
          </div>
        </div>
      </el-header>
      
      <el-main>
        <el-card class="filter-card">
          <el-form :inline="true" :model="queryParams">
            <el-form-item label="服务名称">
              <el-input v-model="queryParams.serviceName" placeholder="请输入服务名称" clearable />
            </el-form-item>
            <el-form-item label="服务分类">
              <el-select v-model="queryParams.serviceCategory" placeholder="请选择服务分类" clearable>
                <el-option label="面部护理" :value="1" />
                <el-option label="身体护理" :value="2" />
                <el-option label="美甲美睫" :value="3" />
                <el-option label="其他服务" :value="4" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">搜索</el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
        
        <el-row :gutter="20" v-loading="loading">
          <el-col :span="8" v-for="service in serviceList" :key="service.id">
            <el-card class="service-card" @click="handleViewDetail(service)">
              <img :src="service.servicePhoto" class="service-image" v-if="service.servicePhoto">
              <div class="service-info">
                <h4>{{ service.serviceName }}</h4>
                <p class="service-category">{{ formatCategory(service.serviceCategory) }}</p>
                <p class="service-price">¥{{ service.serviceNewMoney }}</p>
                <p class="service-duration">{{ service.serviceDuration }}分钟</p>
                <div class="service-rating">
                  <el-rate v-model="service.serviceRating" disabled show-score text-color="#ff9900" />
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        
        <el-pagination
          v-model:current-page="queryParams.page"
          v-model:page-size="queryParams.limit"
          :total="total"
          :page-sizes="[9, 18, 27, 36]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          style="margin-top: 20px; text-align: center"
        />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getServiceList } from '@/api/service'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const serviceList = ref([])
const total = ref(0)

const queryParams = ref({
  page: 1,
  limit: 9,
  serviceName: '',
  serviceCategory: '',
  shangxiaTypes: 1
})

const loadServices = async () => {
  loading.value = true
  try {
    const res = await getServiceList(queryParams.value)
    serviceList.value = res.page.list || []
    total.value = res.page.total || 0
  } catch (error) {
    ElMessage.error('加载服务列表失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  queryParams.value.page = 1
  loadServices()
}

const handleReset = () => {
  queryParams.value = {
    page: 1,
    limit: 9,
    serviceName: '',
    serviceCategory: '',
    shangxiaTypes: 1
  }
  loadServices()
}

const handleSizeChange = (val) => {
  queryParams.value.limit = val
  loadServices()
}

const handleCurrentChange = (val) => {
  queryParams.value.page = val
  loadServices()
}

const handleViewDetail = (service) => {
  router.push(`/service/${service.id}`)
}

const formatCategory = (category) => {
  const map = {
    1: '面部护理',
    2: '身体护理',
    3: '美甲美睫',
    4: '其他服务'
  }
  return map[category] || ''
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

onMounted(() => {
  loadServices()
})
</script>

<style scoped>
.services-container {
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

.filter-card {
  margin-bottom: 20px;
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
  font-size: 16px;
}

.service-category {
  color: #909399;
  font-size: 14px;
  margin: 5px 0;
}

.service-price {
  color: #f56c6c;
  font-size: 20px;
  font-weight: bold;
  margin: 10px 0;
}

.service-duration {
  color: #909399;
  font-size: 14px;
  margin: 5px 0;
}

.service-rating {
  margin-top: 10px;
}
</style>
