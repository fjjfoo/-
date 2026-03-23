<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon :size="40" color="#409eff"><User /></el-icon>
            <div class="stat-info">
              <h3>用户总数</h3>
              <p>{{ stats.userCount }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon :size="40" color="#67c23a"><Brush /></el-icon>
            <div class="stat-info">
              <h3>服务总数</h3>
              <p>{{ stats.serviceCount }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon :size="40" color="#e6a23c"><Document /></el-icon>
            <div class="stat-info">
              <h3>订单总数</h3>
              <p>{{ stats.orderCount }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon :size="40" color="#f56c6c"><Money /></el-icon>
            <div class="stat-info">
              <h3>总收入</h3>
              <p>¥{{ stats.totalRevenue }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <h3>最新订单</h3>
          </template>
          <el-table :data="recentOrders" stripe>
            <el-table-column prop="orderUuidNumber" label="订单号" />
            <el-table-column prop="serviceName" label="服务名称" />
            <el-table-column prop="orderTruePrice" label="金额" />
            <el-table-column prop="orderStatus" label="状态">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.orderStatus)">{{ getStatusText(row.orderStatus) }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <h3>热门服务</h3>
          </template>
          <el-table :data="popularServices" stripe>
            <el-table-column prop="serviceName" label="服务名称" />
            <el-table-column prop="serviceNewMoney" label="价格" />
            <el-table-column prop="serviceClicknum" label="点击次数" />
            <el-table-column prop="serviceRating" label="评分" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { User, Brush, Document, Money } from '@element-plus/icons-vue'
import { getOrderList, getOrderCount, getTotalRevenue } from '@/api/order'
import { getServiceList, getServiceCount } from '@/api/service'
import { getUserCount } from '@/api/user'

const stats = ref({
  userCount: 0,
  serviceCount: 0,
  orderCount: 0,
  totalRevenue: 0
})

const recentOrders = ref([])
const popularServices = ref([])

const getStatusType = (status) => {
  const map = {
    1: 'warning',
    2: 'primary',
    3: 'info',
    4: 'success',
    5: 'danger'
  }
  return map[status] || ''
}

const getStatusText = (status) => {
  const map = {
    1: '待确认',
    2: '已确认',
    3: '服务中',
    4: '已完成',
    5: '已取消'
  }
  return map[status] || ''
}

const loadDashboardData = async () => {
  try {
    // 获取最新订单
    const orderRes = await getOrderList({ page: 1, limit: 5 })
    recentOrders.value = orderRes.page.list || []
    
    // 获取热门服务
    const serviceRes = await getServiceList({ page: 1, limit: 5, orderBy: 'serviceClicknum', sort: 'desc' })
    popularServices.value = serviceRes.page.list || []
    
    // 获取统计数据
    const userCountRes = await getUserCount()
    const serviceCountRes = await getServiceCount()
    const orderCountRes = await getOrderCount()
    const totalRevenueRes = await getTotalRevenue()
    
    stats.value = {
      userCount: userCountRes.data || 0,
      serviceCount: serviceCountRes.data || 0,
      orderCount: orderCountRes.data || 0,
      totalRevenue: totalRevenueRes.data || 0
    }
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

onMounted(() => {
  loadDashboardData()
})
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.stat-card {
  margin-bottom: 20px;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-info h3 {
  margin: 0 0 10px 0;
  color: #909399;
  font-size: 14px;
}

.stat-info p {
  margin: 0;
  color: #303133;
  font-size: 24px;
  font-weight: bold;
}

.el-card :deep(.el-card__header) h3 {
  margin: 0;
  color: #303133;
}
</style>
