import request from '@/utils/request'

export function metric(name, data) {
  const query = data || {}
  const url = '/api/v1/monitor/' + name + '/metric'
  return request({
    url: url,
    method: 'post',
    data: query
  })
}

export function history(name, data) {
  const query = data || {}
  const url = '/api/v1/monitor/' + name + '/history'
  return request({
    url: url,
    method: 'post',
    data: query
  })
}

export default {
  metric,
  history
}
