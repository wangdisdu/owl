import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/api/v1/session/login',
    method: 'post',
    data
  })
}

export function info() {
  return request({
    url: '/api/v1/session/info',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/api/v1/session/logout',
    method: 'post'
  })
}

export default {
  login,
  info,
  logout
}
