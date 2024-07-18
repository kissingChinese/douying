import axios from 'axios';
import { useUserStore } from '../stores';
const instance = axios.create({
  baseURL: "/api",
  timeout: 1000000,
  headers: {
    'Content-Type': 'application/json'
  }
});

// 添加请求拦截器
instance.interceptors.request.use(config => {
  const userStore = useUserStore()
  if(userStore.$state.token)
    config.headers.token = userStore.$state.token
  return config;
}, error => {
  return Promise.reject(error)
})

// 添加响应拦截器
instance.interceptors.response.use((response) => {
  // 2xx 范围内的状态码都会触发该函数。
  // 对响应数据做点什么
  return response;
}, (error) => {
  // 超出 2xx 范围的状态码都会触发该函数。
  // 对响应错误做点什么
  return;
});

export default instance;