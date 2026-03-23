<template>
  <div class="orders-container">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>我的订单</h1>
          <div class="header-nav">
            <el-link type="primary" @click="$router.push('/')">首页</el-link>
            <el-link type="primary" @click="$router.push('/services')">服务列表</el-link>
            <el-link type="primary" @click="$router.push('/profile')">个人中心</el-link>
            <el-button type="danger" @click="handleLogout">退出</el-button>
          </div>
        </div>
      </el-header>
      
      <el-main>
        <el-card class="filter-card">
          <el-form :inline="true" :model="queryParams">
            <el-form-item label="订单状态">
              <el-select v-model="queryParams.orderStatus" placeholder="请选择订单状态" clearable>
                <el-option label="全部" :value="''" />
                <el-option label="待确认" :value="1" />
                <el-option label="已确认" :value="2" />
                <el-option label="服务中" :value="3" />
                <el-option label="已完成" :value="4" />
                <el-option label="已取消" :value="5" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">搜索</el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
        
        <el-card v-for="order in orderList" :key="order.id" class="order-card">
          <div class="order-header">
            <span class="order-no">订单号：{{ order.orderUuidNumber }}</span>
            <el-tag :type="getStatusType(order.orderStatus)">{{ getStatusText(order.orderStatus) }}</el-tag>
          </div>
          <el-row :gutter="20">
            <el-col :span="16">
              <div class="order-info">
                <h3>{{ order.serviceName }}</h3>
                <p>预约日期：{{ formatDate(order.buyTime) }}</p>
                <p>预约时段：{{ order.shijianduan }}</p>
                <p>服务师：{{ order.staffName || '随机分配' }}</p>
                <p>实付金额：¥{{ order.orderTruePrice }}</p>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="order-actions">
                <el-button type="primary" @click="handleViewDetail(order)" size="small">查看详情</el-button>
                <el-button 
                  type="success" 
                  @click="handleComment(order)" 
                  size="small"
                  v-if="order.orderStatus === 4 && !order.orderComment"
                >
                  评价
                </el-button>
                <el-button 
                  type="danger" 
                  @click="handleCancel(order)" 
                  size="small"
                  v-if="order.orderStatus === 1"
                >
                  取消订单
                </el-button>
              </div>
            </el-col>
          </el-row>
        </el-card>
        
        <el-pagination
          v-model:current-page="queryParams.page"
          v-model:page-size="queryParams.limit"
          :total="total"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          style="margin-top: 20px; text-align: center"
        />
      </el-main>
    </el-container>
    
    <el-dialog title="订单详情" v-model="detailDialogVisible" width="600px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="订单号">{{ currentOrder.orderUuidNumber }}</el-descriptions-item>
        <el-descriptions-item label="服务名称">{{ currentOrder.serviceName }}</el-descriptions-item>
        <el-descriptions-item label="预约日期">{{ formatDate(currentOrder.buyTime) }}</el-descriptions-item>
        <el-descriptions-item label="预约时段">{{ currentOrder.shijianduan }}</el-descriptions-item>
        <el-descriptions-item label="服务师">{{ currentOrder.staffName || '随机分配' }}</el-descriptions-item>
        <el-descriptions-item label="实付金额">¥{{ currentOrder.orderTruePrice }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">{{ getStatusText(currentOrder.orderStatus) }}</el-descriptions-item>
        <el-descriptions-item label="支付状态">{{ currentOrder.payStatus === 1 ? '已支付' : '未支付' }}</el-descriptions-item>
        <el-descriptions-item label="备注" v-if="currentOrder.remark">{{ currentOrder.remark }}</el-descriptions-item>
        <el-descriptions-item label="评价" v-if="currentOrder.orderComment">{{ currentOrder.orderComment }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
    
    <el-dialog title="订单评价" v-model="commentDialogVisible" width="500px">
      <el-form :model="commentForm" label-width="80px">
        <el-form-item label="评分">
          <el-rate v-model="commentForm.orderRating" />
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input
            v-model="commentForm.orderComment"
            type="textarea"
            :rows="4"
            placeholder="请输入评价内容"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="commentDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitComment">提交评价</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getOrderList, updateOrder } from '@/api/order'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const orderList = ref([])
const total = ref(0)
const detailDialogVisible = ref(false)
const commentDialogVisible = ref(false)
const currentOrder = ref({})

const queryParams = ref({
  page: 1,
  limit: 10,
  orderStatus: ''
})

const commentForm = ref({
  id: '',
  orderRating: 5,
  orderComment: ''
})

const loadOrders = async () => {
  loading.value = true
  try {
    // 添加当前登录用户的用户ID参数
    const params = {
      ...queryParams.value,
      yonghuId: userStore.userInfo.userId
    }
    const res = await getOrderList(params)
    orderList.value = res.page.list || []
    total.value = res.page.total || 0
  } catch (error) {
    ElMessage.error('加载订单列表失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  queryParams.value.page = 1
  loadOrders()
}

const handleReset = () => {
  queryParams.value = {
    page: 1,
    limit: 10,
    orderStatus: ''
  }
  loadOrders()
}

const handleSizeChange = (val) => {
  queryParams.value.limit = val
  loadOrders()
}

const handleCurrentChange = (val) => {
  queryParams.value.page = val
  loadOrders()
}

const handleViewDetail = (order) => {
  currentOrder.value = order
  detailDialogVisible.value = true
}

const handleComment = (order) => {
  currentOrder.value = order
  commentForm.value = {
    id: order.id,
    orderRating: 5,
    orderComment: ''
  }
  commentDialogVisible.value = true
}

const handleSubmitComment = async () => {
  try {
    await updateOrder(commentForm.value)
    ElMessage.success('评价成功')
    commentDialogVisible.value = false
    loadOrders()
  } catch (error) {
    ElMessage.error('评价失败')
    console.error(error)
  }
}

const handleCancel = async (order) => {
  try {
    await ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await updateOrder({ id: order.id, orderStatus: 5 })
    ElMessage.success('订单已取消')
    loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('取消订单失败')
      console.error(error)
    }
  }
}

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

const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
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
  loadOrders()
})
</script>

<style scoped>
.orders-container {
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

.order-card {
  margin-bottom: 20px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
  margin-bottom: 15px;
}

.order-no {
  color: #909399;
  font-size: 14px;
}

.order-info h3 {
  margin: 0 0 10px 0;
  color: #333;
}

.order-info p {
  color: #666;
  margin: 5px 0;
  font-size: 14px;
}

.order-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
  justify-content: center;
  height: 100%;
}
</style>
