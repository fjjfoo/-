import request from '@/utils/request'

export function getOrderList(params) {
  return request({
    url: '/meirongOrder/list',
    method: 'get',
    params: params
  })
}

export function createOrder(data) {
  return request({
    url: '/meirongOrder/save',
    method: 'post',
    data: data
  })
}

export function updateOrder(data) {
  return request({
    url: '/meirongOrder/update',
    method: 'post',
    data: data
  })
}

export function deleteOrder(id) {
  return request({
    url: '/meirongOrder/delete',
    method: 'post',
    data: [id]
  })
}

export function getOrderDetail(id) {
  return request({
    url: '/meirongOrder/info/' + id,
    method: 'get'
  })
}

export function getOrderCount() {
  return request({
    url: '/meirongOrder/count',
    method: 'get'
  })
}

export function getTotalRevenue() {
  return request({
    url: '/meirongOrder/totalRevenue',
    method: 'get'
  })
}

export function checkStaffAvailability(data) {
  return request({
    url: '/meirongOrder/checkStaffAvailability',
    method: 'post',
    data: data
  })
}
