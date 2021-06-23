import request from '@/utils/request'

export function builders() {
  return request({
    url: '/api/v1/integration/builder/list',
    method: 'get'
  })
}

export function list() {
  return request({
    url: '/api/v1/integration',
    method: 'get'
  })
}

export function create(data) {
  return request({
    url: '/api/v1/integration',
    method: 'post',
    data
  })
}

export function update(data) {
  const url = '/api/v1/integration/' + data.name
  return request({
    url: url,
    method: 'put',
    data
  })
}

export function remove(name) {
  const url = '/api/v1/integration/' + name
  return request({
    url: url,
    method: 'delete'
  })
}

export function get(name) {
  const url = '/api/v1/integration/' + name
  return request({
    url: url,
    method: 'get'
  })
}

export function schema(name) {
  const url = '/api/v1/integration/' + name + "/schema"
  return request({
    url: url,
    method: 'get'
  })
}

export function query(data) {
  const url = '/api/v1/integration/' + data.name + "/query"
  return request({
    url: url,
    method: 'post',
    data
  })
}

export default {
  builders,
  list,
  create,
  update,
  remove,
  get,
  schema,
  query
}