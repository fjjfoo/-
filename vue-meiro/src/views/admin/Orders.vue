<template>
  <div class="admin-orders-container">
    <el-card class="card-container">
      <template #header>
        <div class="header-content">
          <h3>订单管理</h3>
        </div>
      </template>
      
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="订单编号">
          <el-input v-model="queryParams.orderUuidNumber" placeholder="请输入订单编号" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="queryParams.orderStatus" placeholder="请选择订单状态" clearable style="width: 150px">
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
      
      <div class="table-container">
        <el-table :data="orderList" stripe v-loading="loading" style="width: 100%" :max-height="tableHeight">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="orderUuidNumber" label="订单编号" width="180" />
          <el-table-column prop="serviceName" label="服务名称" min-width="150" />
          <el-table-column prop="yonghuName" label="用户姓名" min-width="100" />
          <el-table-column prop="yonghuPhone" label="联系电话" min-width="120" />
          <el-table-column prop="buyTime" label="预约日期" width="120">
            <template #default="{ row }">
              {{ formatDate(row.buyTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="shijianduan" label="预约时段" width="120" />
          <el-table-column prop="orderTruePrice" label="订单金额" width="100" />
          <el-table-column prop="orderStatus" label="订单状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.orderStatus)">{{ getStatusText(row.orderStatus) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="payStatus" label="支付状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.payStatus === 1 ? 'success' : 'warning'">
                {{ row.payStatus === 1 ? '已支付' : '未支付' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="300" fixed="right">
            <template #default="{ row }">
              <div class="action-buttons">
                <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
                <el-button type="success" size="small" @click="handleConfirm(row)" v-if="row.orderStatus === 1">确认</el-button>
                <el-button type="warning" size="small" @click="handleComplete(row)" v-if="row.orderStatus === 3">完成</el-button>
                <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.page"
          v-model:page-size="queryParams.limit"
          :total="total"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px" destroy-on-close>
      <el-form :model="orderForm" :rules="rules" ref="orderFormRef" label-width="100px">
        <el-form-item label="订单编号">
          <el-input v-model="orderForm.orderUuidNumber" disabled />
        </el-form-item>
        <el-form-item label="订单状态" prop="orderStatus">
          <el-select v-model="orderForm.orderStatus" placeholder="请选择订单状态" style="width: 100%">
            <el-option label="待确认" :value="1" />
            <el-option label="已确认" :value="2" />
            <el-option label="服务中" :value="3" />
            <el-option label="已完成" :value="4" />
            <el-option label="已取消" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="orderForm.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onBeforeUnmount } from 'vue'
import { getOrderList, updateOrder, deleteOrder } from '@/api/order'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('编辑订单')
const orderFormRef = ref(null)

const orderList = ref([])
const total = ref(0)

const queryParams = ref({
  page: 1,
  limit: 10,
  orderUuidNumber: '',
  orderStatus: ''
})

const orderForm = ref({
  id: '',
  orderUuidNumber: '',
  orderStatus: 1,
  remark: ''
})

const rules = {
  orderStatus: [
    { required: true, message: '请选择订单状态', trigger: 'change' }
  ]
}

// 计算表格高度，确保表格能够自适应屏幕
const tableHeight = computed(() => {
  const windowHeight = window.innerHeight
  return windowHeight - 300 // 减去其他元素的高度
})

// 监听窗口大小变化，调整表格高度
const handleResize = () => {
  // 表格高度会通过computed自动更新
}

const loadOrders = async () => {
  loading.value = true
  try {
    const res = await getOrderList(queryParams.value)
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
    orderUuidNumber: '',
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

const handleEdit = (row) => {
  dialogTitle.value = '编辑订单'
  orderForm.value = {
    id: row.id,
    orderUuidNumber: row.orderUuidNumber,
    orderStatus: row.orderStatus,
    remark: row.remark || ''
  }
  dialogVisible.value = true
}

const handleConfirm = async (row) => {
  try {
    await ElMessageBox.confirm('确定要确认该订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await updateOrder({ id: row.id, orderStatus: 2 })
    ElMessage.success('订单已确认')
    loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('确认订单失败')
      console.error(error)
    }
  }
}

const handleComplete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要完成该订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await updateOrder({ id: row.id, orderStatus: 4 })
    ElMessage.success('订单已完成')
    loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('完成订单失败')
      console.error(error)
    }
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteOrder(row.id)
    ElMessage.success('删除成功')
    loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
      console.error(error)
    }
  }
}

const handleSubmit = async () => {
  try {
    await orderFormRef.value.validate()
    
    await updateOrder(orderForm.value)
    ElMessage.success('修改成功')
    dialogVisible.value = false
    loadOrders()
  } catch (error) {
    ElMessage.error('操作失败')
    console.error(error)
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

onMounted(() => {
  loadOrders()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.admin-orders-container {
  height: 100%;
  width: 100%;
  padding: 0;
  display: flex;
  flex-direction: column;
}

.card-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  margin: 0;
  border-radius: 0;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-content h3 {
  margin: 0;
}

.search-form {
  margin-bottom: 20px;
}

.table-container {
  flex: 1;
  overflow: auto;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.action-buttons {
  display: flex;
  gap: 8px;
  flex-wrap: nowrap;
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
  .search-form {
    flex-wrap: wrap;
  }
  
  .search-form .el-form-item {
    margin-bottom: 10px;
  }
  
  .action-buttons {
    flex-direction: column;
    gap: 4px;
  }
}
</style>
