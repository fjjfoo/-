<template>
  <div class="service-detail-container">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>美容服务预约系统</h1>
          <div class="header-nav">
            <el-link type="primary" @click="$router.push('/')">首页</el-link>
            <el-link type="primary" @click="$router.push('/services')">服务列表</el-link>
            <el-link type="primary" @click="$router.push('/orders')" v-if="userStore.token">我的订单</el-link>
            <el-button type="danger" @click="handleLogout" v-if="userStore.token">退出</el-button>
          </div>
        </div>
      </el-header>
      
      <el-main v-loading="loading">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card class="detail-card">
              <img :src="serviceDetail.servicePhoto" class="service-image" v-if="serviceDetail.servicePhoto">
              <div class="service-content">
                <h2>{{ serviceDetail.serviceName }}</h2>
                <p class="service-price">¥{{ serviceDetail.serviceNewMoney }}</p>
                <p class="service-duration">服务时长：{{ serviceDetail.serviceDuration }}分钟</p>
                <p class="service-category">服务分类：{{ formatCategory(serviceDetail.serviceCategory) }}</p>
                <p class="service-rating">用户评分：
                  <el-rate v-model="serviceDetail.serviceRating" disabled show-score />
                </p>
                <div class="service-desc">
                  <h3>服务简介</h3>
                  <p>{{ serviceDetail.serviceContent }}</p>
                </div>
                <div class="service-process" v-if="serviceDetail.serviceProcess">
                  <h3>服务流程</h3>
                  <p>{{ serviceDetail.serviceProcess }}</p>
                </div>
                <div class="service-products" v-if="serviceDetail.serviceProducts">
                  <h3>使用产品</h3>
                  <p>{{ serviceDetail.serviceProducts }}</p>
                </div>
                <div class="service-notes" v-if="serviceDetail.serviceNotes">
                  <h3>注意事项</h3>
                  <p>{{ serviceDetail.serviceNotes }}</p>
                </div>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="12">
            <el-card class="booking-card">
              <template #header>
                <h3>预约服务</h3>
              </template>
              <el-form :model="bookingForm" :rules="rules" ref="bookingFormRef" label-width="100px">
                <el-form-item label="预约日期" prop="buyTime">
                  <el-date-picker
                    v-model="bookingForm.buyTime"
                    type="date"
                    placeholder="选择日期"
                    :disabled-date="disabledDate"
                    style="width: 100%"
                  />
                </el-form-item>
                <el-form-item label="预约时段" prop="shijianduan">
                  <el-select v-model="bookingForm.shijianduan" placeholder="选择时段" style="width: 100%">
                    <el-option label="09:00-10:00" value="09:00-10:00" />
                    <el-option label="10:00-11:00" value="10:00-11:00" />
                    <el-option label="11:00-12:00" value="11:00-12:00" />
                    <el-option label="14:00-15:00" value="14:00-15:00" />
                    <el-option label="15:00-16:00" value="15:00-16:00" />
                    <el-option label="16:00-17:00" value="16:00-17:00" />
                    <el-option label="17:00-18:00" value="17:00-18:00" />
                  </el-select>
                </el-form-item>
                <el-form-item label="服务师" prop="staffId">
                  <el-select v-model="bookingForm.staffId" placeholder="选择服务师" style="width: 100%">
                    <el-option label="随机分配" :value="0" />
                    <el-option v-for="staff in staffList" :key="staff.id" :label="staff.staffName" :value="staff.id" />
                  </el-select>
                </el-form-item>
                <el-form-item label="支付方式" prop="payStatus">
                  <el-radio-group v-model="bookingForm.payStatus">
                    <el-radio :label="0">到店支付</el-radio>
                    <el-radio :label="1">在线支付</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                  <el-input
                    v-model="bookingForm.remark"
                    type="textarea"
                    :rows="3"
                    placeholder="请输入备注信息"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleBooking" :loading="bookingLoading" style="width: 100%">
                    立即预约
                  </el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getServiceDetail } from '@/api/service'
import { createOrder, checkStaffAvailability } from '@/api/order'
import { getStaffList } from '@/api/staff'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const loading = ref(false)
const bookingLoading = ref(false)
const serviceDetail = ref({})
const bookingFormRef = ref(null)
const staffList = ref([])

const bookingForm = ref({
  serviceId: route.params.id,
  staffId: 0,
  buyTime: '',
  shijianduan: '',
  payStatus: 0,
  remark: ''
})

const rules = {
  buyTime: [
    { required: true, message: '请选择预约日期', trigger: 'change' }
  ],
  shijianduan: [
    { required: true, message: '请选择预约时段', trigger: 'change' }
  ]
}

const disabledDate = (time) => {
  return time.getTime() < Date.now() - 8.64e7
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

const loadServiceDetail = async () => {
  loading.value = true
  try {
    const res = await getServiceDetail(route.params.id)
    serviceDetail.value = res.meirongService || {}
  } catch (error) {
    ElMessage.error('加载服务详情失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

const loadStaffList = async () => {
  try {
    const res = await getStaffList()
    staffList.value = res.page.list || []
  } catch (error) {
    ElMessage.error('加载服务师列表失败')
    console.error(error)
  }
}

const handleBooking = async () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  // 处理日期，确保发送正确的本地日期
  const formatLocalDate = (date) => {
    if (!date) return null
    const d = new Date(date)
    const year = d.getFullYear()
    const month = String(d.getMonth() + 1).padStart(2, '0')
    const day = String(d.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
  }
  
  try {
    await bookingFormRef.value.validate()
    
    // 检查服务师可用性
    if (bookingForm.value.staffId > 0) {
      const availabilityData = {
        staffId: bookingForm.value.staffId,
        buyTime: formatLocalDate(bookingForm.value.buyTime),
        shijianduan: bookingForm.value.shijianduan
      }
      
      const availabilityRes = await checkStaffAvailability(availabilityData)
      if (!availabilityRes.available) {
        await ElMessageBox.alert(
          '当前选择的服务师在指定时间段内已被占用，请选择其他时间段或更换服务师。',
          '预约冲突',
          {
            confirmButtonText: '确定',
            type: 'warning'
          }
        )
        return
      }
    }
    
    await ElMessageBox.confirm('确认预约该服务吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    bookingLoading.value = true
    
    const orderData = {
      ...bookingForm.value,
      buyTime: formatLocalDate(bookingForm.value.buyTime),
      yonghuId: userStore.userInfo.userId,
      orderTruePrice: serviceDetail.value.serviceNewMoney,
      orderStatus: 1
    }
    
    await createOrder(orderData)
    
    ElMessage.success('预约成功，请等待确认')
    router.push('/orders')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('预约失败')
      console.error(error)
    }
  } finally {
    bookingLoading.value = false
  }
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

onMounted(async () => {
  await loadServiceDetail()
  await loadStaffList()
})
</script>

<style scoped>
.service-detail-container {
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

.detail-card,
.booking-card {
  margin-bottom: 20px;
}

.booking-card :deep(.el-card__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.booking-card :deep(.el-card__header h3) {
  margin: 0;
  color: white;
}

.service-image {
  width: 100%;
  height: 300px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 20px;
}

.service-content h2 {
  color: #333;
  margin-bottom: 15px;
}

.service-price {
  color: #f56c6c;
  font-size: 28px;
  font-weight: bold;
  margin: 15px 0;
}

.service-duration,
.service-category {
  color: #909399;
  font-size: 16px;
  margin: 10px 0;
}

.service-rating {
  margin: 15px 0;
}

.service-desc,
.service-process,
.service-products,
.service-notes {
  margin: 20px 0;
}

.service-desc h3,
.service-process h3,
.service-products h3,
.service-notes h3 {
  color: #333;
  margin-bottom: 10px;
}

.service-desc p,
.service-process p,
.service-products p,
.service-notes p {
  color: #666;
  line-height: 1.6;
}
</style>
