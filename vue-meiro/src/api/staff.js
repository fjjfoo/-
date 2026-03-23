import request from '@/utils/request'

export function getStaffList(params) {
  return request({
    url: '/staff/list',
    method: 'get',
    params: params
  })
}

export function getStaffDetail(id) {
  return request({
    url: '/staff/info/' + id,
    method: 'get'
  })
}

export function addStaff(data) {
  return request({
    url: '/staff/save',
    method: 'post',
    data: data
  })
}

export function updateStaff(data) {
  return request({
    url: '/staff/update',
    method: 'post',
    data: data
  })
}

export function deleteStaff(id) {
  return request({
    url: '/staff/delete',
    method: 'post',
    data: [id]
  })
}

export function updateStaffStatus(id, status) {
  return request({
    url: '/staff/updateStatus',
    method: 'post',
    data: { id: id, status: status }
  })
}

export function getStaffCount() {
  return request({
    url: '/staff/count',
    method: 'get'
  })
}