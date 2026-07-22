import {AxiosError, AxiosRequestConfig, AxiosResponse, CanceledError} from "axios";
import {openNotify} from "@/components/feedback/notify";
import {redirect} from "react-router-dom";

type ResponseType = {
    success: boolean,
    code: number,
    msg: string,
    data: unknown
}

/**
 * @description 响应拦截器,处理响应数据，从响应中获取data
 */
export const dataInterceptor = (response: AxiosResponse<ResponseType>): Promise<AxiosResponse<unknown>> => {
    if (response.status === 200) {
        if (response.config.responseType === "blob" || response.config.responseType === "stream") {
            return response as unknown as Promise<AxiosResponse<unknown>>;
        }
        const {data, success, msg, code} = response.data
        if (success || code === 0) {
            return data as Promise<AxiosResponse<unknown>>
        } else {
            return Promise.reject(new AxiosError(msg, Object(code).toString(), response.config, response.request, response))
        }
    }
    return Promise.reject(new Error("无法识别返回结果"))
}

export const errorInterceptor = (error: AxiosError): Promise<AxiosError> => {

    // 主动取消请求
    if (error instanceof CanceledError) {
        return Promise.reject(error)
    }

    const config = error.config as { silent: boolean } & AxiosRequestConfig;
    // 非静默请求
    if (!config.silent) {
        openNotify({
            message: error.message || "请求失败",
            variant: "error"
        })
        if (error.status === 401) {
            // 跳转到登陆
            redirect("/sign-in")
        }
    }
    return Promise.reject(error)
}