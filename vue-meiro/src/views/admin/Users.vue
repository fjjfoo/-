<template>
  <div class="admin-users-container">
    <el-card class="card-container">
      <template #header>
        <div class="header-content">
          <h3>用户管理</h3>
          <el-button type="primary" @click="handleAdd">新增用户</el-button>
        </div>
      </template>
      
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="queryParams.yonghuPhone" placeholder="请输入手机号" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      
      <div class="table-container">
        <el-table :data="userList" stripe v-loading="loading" style="width: 100%" :max-height="tableHeight">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="username" label="用户名" min-width="120" />
          <el-table-column prop="yonghuName" label="真实姓名" min-width="100" />
          <el-table-column prop="yonghuPhone" label="手机号" min-width="120" />
          <el-table-column prop="yonghuIdNumber" label="身份证号" min-width="180" />
          <el-table-column prop="sexTypes" label="性别" width="80">
            <template #default="{ row }">
              {{ row.sexTypes === 1 ? '男' : '女' }}
            </template>
          </el-table-column>
          <el-table-column prop="yonghuEmail" label="邮箱" min-width="150" />
          <el-table-column prop="newMoney" label="余额" width="100" />
          <el-table-column prop="createTime" label="注册时间" width="120">
            <template #default="{ row }">
              {{ formatDate(row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="250" fixed="right">
            <template #default="{ row }">
              <div class="action-buttons">
                <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
                <el-button type="warning" size="small" @click="handleResetPassword(row)">重置密码</el-button>
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
      <el-form :model="userForm" :rules="rules" ref="userFormRef" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="yonghuName">
          <el-input v-model="userForm.yonghuName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="yonghuPhone">
          <el-input v-model="userForm.yonghuPhone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="身份证号" prop="yonghuIdNumber">
          <el-input v-model="userForm.yonghuIdNumber" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="性别" prop="sexTypes">
          <el-radio-group v-model="userForm.sexTypes">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="邮箱" prop="yonghuEmail">
          <el-input v-model="userForm.yonghuEmail" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="余额" prop="newMoney">
          <el-input v-model="userForm.newMoney" type="number" placeholder="请输入余额" />
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
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增用户')
const userFormRef = ref(null)

const userList = ref([])
const total = ref(0)

const queryParams = ref({
  page: 1,
  limit: 10,
  username: '',
  yonghuPhone: ''
})

const userForm = ref({
  id: '',
  username: '',
  yonghuName: '',
  yonghuPhone: '',
  yonghuIdNumber: '',
  sexTypes: 2,
  yonghuEmail: '',
  newMoney: 0
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
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

// 计算表格高度，确保表格能够自适应屏幕
const tableHeight = computed(() => {
  const windowHeight = window.innerHeight
  return windowHeight - 300 // 减去其他元素的高度
})

// 监听窗口大小变化，调整表格高度
const handleResize = () => {
  // 表格高度会通过computed自动更新
}

const loadUsers = async () => {
  loading.value = true
  try {
    const queryString = new URLSearchParams(queryParams.value).toString()
    const res = await fetch(`http://localhost:8080/tiyuguan/yonghu/page?${queryString}`, {
      method: 'get',
      headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
      }
    }).then(res => res.json())
    
    userList.value = res.data.list || []
    total.value = res.data.total || 0
  } catch (error) {
    ElMessage.error('加载用户列表失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  queryParams.value.page = 1
  loadUsers()
}

const handleReset = () => {
  queryParams.value = {
    page: 1,
    limit: 10,
    username: '',
    yonghuPhone: ''
  }
  loadUsers()
}

const handleSizeChange = (val) => {
  queryParams.value.limit = val
  loadUsers()
}

const handleCurrentChange = (val) => {
  queryParams.value.page = val
  loadUsers()
}

const handleAdd = () => {
  dialogTitle.value = '新增用户'
  userForm.value = {
    id: '',
    username: '',
    yonghuName: '',
    yonghuPhone: '',
    yonghuIdNumber: '',
    sexTypes: 2,
    yonghuEmail: '',
    newMoney: 0
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑用户'
  userForm.value = { ...row }
  dialogVisible.value = true
}

const handleResetPassword = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要重置用户 ${row.yonghuName} 的密码吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await fetch(`http://localhost:8080/tiyuguan/yonghu/resetPassword?id=${row.id}`, {
      method: 'get',
      headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
      }
    })
    
    ElMessage.success('密码已重置为：123456')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('重置密码失败')
      console.error(error)
    }
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除用户 ${row.yonghuName} 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await fetch('http://localhost:8080/tiyuguan/yonghu/delete', {
      method: 'post',
      headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token'),
        'Content-Type': 'application/json'
      },
      body: JSON.stringify([row.id])
    })
    
    ElMessage.success('删除成功')
    loadUsers()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
      console.error(error)
    }
  }
}

const handleSubmit = async () => {
  try {
    await userFormRef.value.validate()
    
    const url = userForm.value.id ? 'http://localhost:8080/tiyuguan/yonghu/update' : 'http://localhost:8080/tiyuguan/yonghu/save'
    const method = 'post'
    
    await fetch(url, {
      method: method,
      headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token'),
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(userForm.value)
    })
    
    ElMessage.success(userForm.value.id ? '修改成功' : '新增成功')
    dialogVisible.value = false
    loadUsers()
  } catch (error) {
    ElMessage.error('操作失败')
    console.error(error)
  }
}

const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

onMounted(() => {
  loadUsers()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.admin-users-container {
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
