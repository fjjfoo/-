<template>
  <div class="admin-staff-container">
    <el-card class="card-container">
      <template #header>
        <div class="header-content">
          <h3>服务师管理</h3>
          <el-button type="primary" @click="handleAdd">新增服务师</el-button>
        </div>
      </template>
      
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="服务师姓名">
          <el-input v-model="queryParams.staffName" placeholder="请输入服务师姓名" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.staffStatus" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      
      <div class="table-container">
        <el-table :data="staffList" stripe v-loading="loading" style="width: 100%" :max-height="tableHeight">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="staffName" label="服务师姓名" min-width="120" />
          <el-table-column prop="staffPhone" label="手机号" min-width="120" />
          <el-table-column prop="staffEmail" label="邮箱" min-width="150" />
          <el-table-column prop="staffSkills" label="技能标签" min-width="150" />
          <el-table-column prop="staffRating" label="评分" width="100" />
          <el-table-column prop="staffStatus" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.staffStatus === 1 ? 'success' : 'danger'">
                {{ row.staffStatus === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="300" fixed="right">
            <template #default="{ row }">
              <div class="action-buttons">
                <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
                <el-button :type="row.staffStatus === 1 ? 'warning' : 'success'" size="small" @click="handleToggleStatus(row)">
                  {{ row.staffStatus === 1 ? '禁用' : '启用' }}
                </el-button>
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
      <el-form :model="staffForm" :rules="rules" ref="staffFormRef" label-width="120px">
        <el-form-item label="服务师姓名" prop="staffName">
          <el-input v-model="staffForm.staffName" placeholder="请输入服务师姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="staffPhone">
          <el-input v-model="staffForm.staffPhone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="staffEmail">
          <el-input v-model="staffForm.staffEmail" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="头像URL" prop="staffAvatar">
          <el-input v-model="staffForm.staffAvatar" placeholder="请输入头像URL" />
        </el-form-item>
        <el-form-item label="技能标签" prop="staffSkills">
          <el-input v-model="staffForm.staffSkills" placeholder="请输入技能标签，多个标签用逗号分隔" />
        </el-form-item>
        <el-form-item label="服务师简介" prop="staffIntroduction">
          <el-input v-model="staffForm.staffIntroduction" type="textarea" :rows="3" placeholder="请输入服务师简介" />
        </el-form-item>
        <el-form-item label="状态" prop="staffStatus">
          <el-radio-group v-model="staffForm.staffStatus">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="2">禁用</el-radio>
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
import { getStaffList, getStaffDetail, addStaff, updateStaff, deleteStaff, updateStaffStatus } from '@/api/staff'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增服务师')
const staffFormRef = ref(null)

const staffList = ref([])
const total = ref(0)

const queryParams = ref({
  page: 1,
  limit: 10,
  staffName: '',
  staffStatus: ''
})

const staffForm = ref({
  id: '',
  staffName: '',
  staffPhone: '',
  staffEmail: '',
  staffAvatar: '',
  staffSkills: '',
  staffIntroduction: '',
  staffStatus: 1
})

const rules = {
  staffName: [
    { required: true, message: '请输入服务师姓名', trigger: 'blur' }
  ],
  staffPhone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  staffEmail: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
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

const loadStaff = async () => {
  loading.value = true
  try {
    const res = await getStaffList(queryParams.value)
    staffList.value = res.page.list || []
    total.value = res.page.total || 0
  } catch (error) {
    ElMessage.error('加载服务师列表失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  queryParams.value.page = 1
  loadStaff()
}

const handleReset = () => {
  queryParams.value = {
    page: 1,
    limit: 10,
    staffName: '',
    staffStatus: ''
  }
  loadStaff()
}

const handleSizeChange = (val) => {
  queryParams.value.limit = val
  loadStaff()
}

const handleCurrentChange = (val) => {
  queryParams.value.page = val
  loadStaff()
}

const handleAdd = () => {
  dialogTitle.value = '新增服务师'
  staffForm.value = {
    id: '',
    staffName: '',
    staffPhone: '',
    staffEmail: '',
    staffAvatar: '',
    staffSkills: '',
    staffIntroduction: '',
    staffStatus: 1
  }
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  dialogTitle.value = '编辑服务师'
  try {
    const res = await getStaffDetail(row.id)
    staffForm.value = res.staff || {}
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('加载服务师详情失败')
    console.error(error)
  }
}

const handleToggleStatus = async (row) => {
  try {
    const newStatus = row.staffStatus === 1 ? 2 : 1
    await ElMessageBox.confirm(`确定要${newStatus === 1 ? '启用' : '禁用'}该服务师吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await updateStaffStatus(row.id, newStatus)
    ElMessage.success(`${newStatus === 1 ? '启用' : '禁用'}成功`)
    loadStaff()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
      console.error(error)
    }
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该服务师吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteStaff(row.id)
    ElMessage.success('删除成功')
    loadStaff()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
      console.error(error)
    }
  }
}

const handleSubmit = async () => {
  try {
    await staffFormRef.value.validate()
    
    if (staffForm.value.id) {
      await updateStaff(staffForm.value)
      ElMessage.success('修改成功')
    } else {
      await addStaff(staffForm.value)
      ElMessage.success('新增成功')
    }
    
    dialogVisible.value = false
    loadStaff()
  } catch (error) {
    ElMessage.error('操作失败')
    console.error(error)
  }
}

onMounted(() => {
  loadStaff()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.admin-staff-container {
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