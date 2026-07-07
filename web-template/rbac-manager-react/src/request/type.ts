import {AxiosRequestConfig} from "axios";

/** 普通请求参数只能是以下类型 */
export type ParamType = string | boolean | number | Date | undefined | null;

/** 上传文件请求参数可以是以下类型 */
export type UploadParamType = ParamType | File | Blob | HTMLImageElement | HTMLCanvasElement;

/** 分页响应对象 */
export type PageResponse<T> = {
    /** 当前页的内容 */
    content: T[],
    /** 总元素数 */
    totalElements: number
    /** 总页数 */
    totalPages: number
    /** 当前页码 */
    pageNo: number
    /** 每页大小 */
    pageSize: number
    /** 是否是第一页 */
    first: boolean
    /** 是否是最后一页*/
    last: boolean
}

/** 分页请求对象*/
export type PageRequest<T> = {
    /** 页码，从1开始，默认1 */
    pageNo?: number,
    /** 每页大小,后台默认是20 */
    pageSize?: number,
    /** 排序字段，keyT中的任意字段名，value是排序方式 */
    sort?: Partial<Record<keyof T, "asc" | "desc">>
}

export type DownloadResponse = {
    data: Blob,
    status: number,
    config: AxiosRequestConfig,
    fileName?: string,
    save: (fileName?: string) => void,
    headers: {
        'accept-ranges': string,
        'content-length': string,
        'content-type': string,
        'content-disposition': string
    } & Record<string, string>
}