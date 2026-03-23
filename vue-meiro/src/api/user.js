import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/yonghu/login',
    method: 'post',
    data: data
  })
}

export function register(data) {
  return request({
    url: '/yonghu/register',
    method: 'post',
    data: data
  })
}

export function getUserInfo() {
  return request({
    url: '/yonghu/session',
    method: 'get'
  })
}

export function updateProfile(data) {
  return request({
    url: '/yonghu/update',
    method: 'post',
    data: data
  })
}

export function resetPassword(username) {
  return request({
    url: '/yonghu/resetPass',
    method: 'get',
    params: { username }
  })
}

export function getUserCount() {
  return request({
    url: '/yonghu/count',
    method: 'get'
  })
}
