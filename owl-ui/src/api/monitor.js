import request from '@/utils/request'

export function metric(name) {
  const url = '/api/v1/monitor/' + name + '/metric'
  return request({
    url: url,
    method: 'get'
  })
}

export function history(name, data) {
  const url = '/api/v1/monitor/' + name + '/history'
  return request({
    url: url,
    method: 'post',
    data
  })
}

export default {
  metric,
  history
}
