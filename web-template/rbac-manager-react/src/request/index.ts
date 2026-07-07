import {axiosClient} from "./axios.client";
import {ParamType, UploadParamType, DownloadResponse, PageRequest, PageResponse} from "./type";
import {AxiosInstance, AxiosRequestConfig, Method} from "axios";
import {formatFormParam, formatUploadFormData, getFileNameFromContentDisposition, initPageRequest} from "./util";
import {FetchError} from "./error";
import {authApi} from "@/server-api/auth";

export type {PageRequest, PageResponse, ParamType, UploadParamType, DownloadResponse} from "./type";
export {initPageRequest} from "./util";
export {FetchError} from "./error";


/**
 *
 * @description 用以调整axios的配置
 */
export const configAxios = (configCallBack: (client: AxiosInstance) => void) => {
    configCallBack(axiosClient);
}


/**
 * @description 发送 get请求
 * @param url 请求地址
 * @param params 请求参数
 * @param config 请求配置
 */
export const GET = async <T>(url: string, params: Record<string, ParamType | ParamType[]> = {}, config: AxiosRequestConfig = {}): Promise<T> => {
    return axiosClient.get<unknown, T>(url, {...config, params: formatFormParam(params)} as AxiosRequestConfig);
}

/**
 * @description 发送 get请求，获取分页数据
 * @param url 请求地址
 * @param params 请求参数
 * @param page 分页请求
 * @param config 请求配置
 * @constructor
 */
export const GET_PAGE = async <T>(url: string, params: Record<string, ParamType | ParamType[]> = {}, page?: PageRequest<T>, config: AxiosRequestConfig = {}): Promise<PageResponse<T>> => {
    params = initPageRequest(params, page)
    return GET<PageResponse<T>>(url, params, config)
}

/**
 * @description 发送 post请求
 * @param url 请求地址
 * @param data 请求参数
 * @param config 请求配置
 */
export const POST = async <T>(url: string, data: Record<string, ParamType | ParamType[]> = {}, config: AxiosRequestConfig = {}): Promise<T> => {
    return axiosClient.post<unknown, T>(url, formatFormParam(data), config);
}

/**
 * @description 发送 put请求,获取分页数据
 * @param url 请求地址
 * @param data 请求参数
 * @param page 分页请求
 * @param config 请求配置
 */
export const PUT_PAGE = async <T>(url: string, data: Record<string, ParamType | ParamType[]> = {}, page?: PageRequest<T>, config: AxiosRequestConfig = {}): Promise<PageResponse<T>> => {
    data = initPageRequest(data, page)
    return POST<PageResponse<T>>(url, data, config)
}


/**
 * @description 发送 上传文件请求,上传文件的类型支持File、Blob、HTMLImageElement、HTMLCanvasElement
 * @param url 请求地址
 * @param data 请求参数
 * @param config 请求配置
 */
export const UPLOAD = async <T>(url: string, data: Record<string, UploadParamType | ParamType[]> = {}, config: AxiosRequestConfig = {}): Promise<T> => {
    const formatParams = await formatUploadFormData(data);
    return axiosClient.post<unknown, T>(url, formatParams, {
        ...config,
        headers: {
            ...(config.headers || {}),
            'Content-Type': 'multipart/form-data'
        }
    });
}

/**
 * @description 下载文件请求,
 * 这里是一次下载所有的文件内容，如何保存文件，需要自行实现，比如：
 * <pre>
 *     const blob = new Blob(chunks);
 *         const urlBlob = window.URL.createObjectURL(blob);
 *         const link = document.createElement('a');
 *         link.href = urlBlob;
 *         link.setAttribute('download', fileName);
 *         document.body.appendChild(link);
 *         link.click();
 *         link.remove();
 *     </pre>
 * 如果需要分片下载，需要自行实现，
 * 或者提取token，使用浏览器下载
 */
export const DOWNLOAD = async (url: string, params: Record<string, ParamType | ParamType[]> = {}, config: AxiosRequestConfig = {}): Promise<DownloadResponse> => {
    const formatParams = formatFormParam(params);
    const response = await axiosClient.get(url, {
        params: formatParams,
        responseType: "blob", ...config
    }) as DownloadResponse;
    response.fileName = getFileNameFromContentDisposition(response.headers['content-disposition']);
    response.save = (fileName?: string) => {
        const blob = new Blob([response.data]);
        const urlBlob = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = urlBlob;
        link.setAttribute('download', fileName || response.fileName || "unknown");
        document.body.appendChild(link);
        link.click();
        link.remove();
    }
    return response;
}


/**
 *  @description 发送流请求，返回response，自行处理response
 */
export const STREAM = async (method: Method, url: string, params: Record<string, ParamType | ParamType[]> = {}, abortSignal?: AbortSignal) => {
    const formatParams = formatFormParam(params);
    const baseUrl = axiosClient.defaults.baseURL || "";
    if (baseUrl) {
        url = url.startsWith("/") ? url : "/" + url
        url = baseUrl.endsWith("/") ? baseUrl + url.substring(1) : baseUrl + url
    }
    if (method.toLowerCase() === "get") {
        if (url.includes("?")) {
            url = url + "&" + formatParams.toString()
        } else {
            url = url + "?" + formatParams
        }
    }
    const headers = {} as Record<string, string>
    const tokenInfo = authApi.getTokenInfo();
    headers[tokenInfo.key] = tokenInfo.value
    const response = await fetch(url, {
        method: method,
        body: method === "post" ? formatParams : undefined,
        headers,
        signal: abortSignal
    })
    if (!response.ok) {
        throw new FetchError("请求失败", response)
    }
    return response
}

