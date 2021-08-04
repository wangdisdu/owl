import axios from 'axios'
import { Message } from 'element-ui'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  withCredentials: true, // send cookies when cross-domain requests
  timeout: 60000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  response => {
    const res = response.data
    // if the custom code is not 0000, it is judged as an error.
    if (res.retCode !== '0000') {
      Message({
        message: res.retMsg || 'Error',
        type: 'error',
        duration: 2 * 1000
      })
      return Promise.reject(new Error(res.retMsg || 'Error'))
    } else {
      return Promise.resolve(res)
    }
  },
  error => {
    // do something with response error
    console.log('err' + error) // for debug
    return Promise.reject(error)
  }
)

export default service
