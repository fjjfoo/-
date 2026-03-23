<template>
  <div class="admin-services-container">
    <el-card class="card-container">
      <template #header>
        <div class="header-content">
          <h3>服务管理</h3>
          <el-button type="primary" @click="handleAdd">新增服务</el-button>
        </div>
      </template>
      
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="服务名称">
          <el-input v-model="queryParams.serviceName" placeholder="请输入服务名称" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="服务分类">
          <el-select v-model="queryParams.serviceCategory" placeholder="请选择服务分类" clearable style="width: 150px">
            <el-option label="面部护理" :value="1" />
            <el-option label="身体护理" :value="2" />
            <el-option label="美甲美睫" :value="3" />
            <el-option label="其他服务" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="上架状态">
          <el-select v-model="queryParams.shangxiaTypes" placeholder="请选择上架状态" clearable style="width: 120px">
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      
      <div class="table-container">
        <el-table :data="serviceList" stripe v-loading="loading" style="width: 100%" :max-height="tableHeight">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="serviceName" label="服务名称" min-width="150" />
          <el-table-column prop="serviceCategory" label="服务分类" width="100">
            <template #default="{ row }">
              {{ formatCategory(row.serviceCategory) }}
            </template>
          </el-table-column>
          <el-table-column prop="serviceNewMoney" label="价格" width="100" />
          <el-table-column prop="serviceDuration" label="时长(分钟)" width="120" />
          <el-table-column prop="serviceClicknum" label="点击次数" width="100" />
          <el-table-column prop="serviceRating" label="评分" width="100" />
          <el-table-column prop="shangxiaTypes" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.shangxiaTypes === 1 ? 'success' : 'danger'">
                {{ row.shangxiaTypes === 1 ? '上架' : '下架' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <div class="action-buttons">
                <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
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
    
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="800px" destroy-on-close>
      <el-form :model="serviceForm" :rules="rules" ref="serviceFormRef" label-width="120px">
        <el-form-item label="服务名称" prop="serviceName">
          <el-input v-model="serviceForm.serviceName" placeholder="请输入服务名称" />
        </el-form-item>
        <el-form-item label="服务分类" prop="serviceCategory">
          <el-select v-model="serviceForm.serviceCategory" placeholder="请选择服务分类" style="width: 100%">
            <el-option label="面部护理" :value="1" />
            <el-option label="身体护理" :value="2" />
            <el-option label="美甲美睫" :value="3" />
            <el-option label="其他服务" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="服务原价" prop="serviceOldMoney">
          <el-input v-model="serviceForm.serviceOldMoney" type="number" placeholder="请输入服务原价" />
        </el-form-item>
        <el-form-item label="服务现价" prop="serviceNewMoney">
          <el-input v-model="serviceForm.serviceNewMoney" type="number" placeholder="请输入服务现价" />
        </el-form-item>
        <el-form-item label="服务时长" prop="serviceDuration">
          <el-input v-model="serviceForm.serviceDuration" type="number" placeholder="请输入服务时长(分钟)" />
        </el-form-item>
        <el-form-item label="服务照片" prop="servicePhoto">
          <el-input v-model="serviceForm.servicePhoto" placeholder="请输入服务照片URL" />
        </el-form-item>
        <el-form-item label="服务流程" prop="serviceProcess">
          <el-input v-model="serviceForm.serviceProcess" type="textarea" :rows="3" placeholder="请输入服务流程" />
        </el-form-item>
        <el-form-item label="使用产品" prop="serviceProducts">
          <el-input v-model="serviceForm.serviceProducts" type="textarea" :rows="3" placeholder="请输入使用产品" />
        </el-form-item>
        <el-form-item label="注意事项" prop="serviceNotes">
          <el-input v-model="serviceForm.serviceNotes" type="textarea" :rows="3" placeholder="请输入注意事项" />
        </el-form-item>
        <el-form-item label="服务简介" prop="serviceContent">
          <el-input v-model="serviceForm.serviceContent" type="textarea" :rows="3" placeholder="请输入服务简介" />
        </el-form-item>
        <el-form-item label="上架状态" prop="shangxiaTypes">
          <el-radio-group v-model="serviceForm.shangxiaTypes">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="2">下架</el-radio>
          </el-radio-group>
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
import { getServiceList, getServiceDetail, addService, updateService, deleteService } from '@/api/service'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增服务')
const serviceFormRef = ref(null)

const serviceList = ref([])
const total = ref(0)

const queryParams = ref({
  page: 1,
  limit: 10,
  serviceName: '',
  serviceCategory: '',
  shangxiaTypes: ''
})

const serviceForm = ref({
  id: '',
  serviceName: '',
  serviceCategory: '',
  serviceOldMoney: 0,
  serviceNewMoney: 0,
  serviceDuration: 0,
  servicePhoto: '',
  serviceProcess: '',
  serviceProducts: '',
  serviceNotes: '',
  serviceContent: '',
  shangxiaTypes: 1
})

const rules = {
  serviceName: [
    { required: true, message: '请输入服务名称', trigger: 'blur' }
  ],
  serviceCategory: [
    { required: true, message: '请选择服务分类', trigger: 'change' }
  ],
  serviceNewMoney: [
    { required: true, message: '请输入服务现价', trigger: 'blur' }
  ],
  serviceDuration: [
    { required: true, message: '请输入服务时长', trigger: 'blur' }
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
    limit: 10,
    serviceName: '',
    serviceCategory: '',
    shangxiaTypes: ''
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

const handleAdd = () => {
  dialogTitle.value = '新增服务'
  serviceForm.value = {
    id: '',
    serviceName: '',
    serviceCategory: '',
    serviceOldMoney: 0,
    serviceNewMoney: 0,
    serviceDuration: 0,
    servicePhoto: '',
    serviceProcess: '',
    serviceProducts: '',
    serviceNotes: '',
    serviceContent: '',
    shangxiaTypes: 1
  }
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  dialogTitle.value = '编辑服务'
  try {
    const res = await getServiceDetail(row.id)
    serviceForm.value = res.meirongService || {}
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('加载服务详情失败')
    console.error(error)
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该服务吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteService(row.id)
    ElMessage.success('删除成功')
    loadServices()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
      console.error(error)
    }
  }
}

const handleSubmit = async () => {
  try {
    await serviceFormRef.value.validate()
    
    if (serviceForm.value.id) {
      await updateService(serviceForm.value)
      ElMessage.success('修改成功')
    } else {
      await addService(serviceForm.value)
      ElMessage.success('新增成功')
    }
    
    dialogVisible.value = false
    loadServices()
  } catch (error) {
    ElMessage.error('操作失败')
    console.error(error)
  }
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

onMounted(() => {
  loadServices()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.admin-services-container {
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
