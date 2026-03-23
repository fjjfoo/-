import request from '@/utils/request'

export function getServiceList(params) {
  return request({
    url: '/meirong/list',
    method: 'get',
    params: params
  })
}

export function getServiceDetail(id) {
  return request({
    url: '/meirong/info/' + id,
    method: 'get'
  })
}

export function addService(data) {
  return request({
    url: '/meirong/save',
    method: 'post',
    data: data
  })
}

export function updateService(data) {
  return request({
    url: '/meirong/update',
    method: 'post',
    data: data
  })
}

export function deleteService(id) {
  return request({
    url: '/meirong/delete',
    method: 'post',
    data: [id]
  })
}

export function getServiceCount() {
  return request({
    url: '/meirong/count',
    method: 'get'
  })
}
