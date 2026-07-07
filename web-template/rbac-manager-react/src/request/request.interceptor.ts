import {InternalAxiosRequestConfig} from "axios";
import {authApi} from "@/server-api/auth";

/**
 * @description 请求拦截器,主要用于注入token
 * @param config
 */
export const tokenInterceptor = (config: InternalAxiosRequestConfig) => {
    const tokenInfo = authApi.getTokenInfo();
    config.headers[tokenInfo.key] = tokenInfo.value
    return config
}