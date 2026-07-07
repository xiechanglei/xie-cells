import axios from 'axios';
import {tokenInterceptor} from "./request.interceptor";
import {dataInterceptor, errorInterceptor} from "./response.interceptor";
import {axiosConfig} from "./axios.config";

export const axiosClient = axios.create(axiosConfig);
// 请求拦截器
axiosClient.interceptors.request.use(tokenInterceptor);
// 响应拦截器
axiosClient.interceptors.response.use(dataInterceptor);
// 异常拦截
axiosClient.interceptors.response.use(undefined, errorInterceptor);
