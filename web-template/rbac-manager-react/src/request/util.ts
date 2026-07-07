import {PageRequest, ParamType, UploadParamType} from "./type";

/**
 * @description 初始化分页请求参数
 * @param params 原始请求参数
 * @param pageRequest 分页请求
 */
export const initPageRequest = (params: Record<string, ParamType | ParamType[]>, pageRequest?: PageRequest<unknown>): Record<string, ParamType | ParamType[]> => {
    const newParams = {...params};
    if (pageRequest) {
        newParams.pageNo = pageRequest.pageNo;
        newParams.pageSize = pageRequest.pageSize;
        if (pageRequest.sort) {
            newParams.sort = Object.entries(pageRequest.sort).map(([key, value]) => `${key}:${value}`).join(",");
        }
    }
    return newParams;
}

export const formatParamValue = (value: ParamType) => {
    if (value === undefined || value === null) {
        return;
    }
    if (value instanceof Date) {
        return value.getTime().toString();
    }
    return Object(value).toString();
}

/**
 * @description 将请求参数转换为URLSearchParams，主要是为了解决数组参数的问题，axios默认给参数的数组加了[]，导致后端无法解析
 * @param params 请求参数
 */
export const formatFormParam = (params: Record<string, ParamType | ParamType[]>): URLSearchParams => {
    const urlSearchParams = new URLSearchParams();
    for (const key of Object.keys(params)) {
        const value = params[key];
        if (value === undefined || value === null) {
            continue;
        }
        if (Array.isArray(value)) {
            value.forEach(v => urlSearchParams.append(key, formatParamValue(v)))
        } else {
            urlSearchParams.append(key, formatParamValue(value));
        }
    }
    return urlSearchParams;
}

/**
 * @description 将请求参数转换为FormData，主要是为了解决文件上传的问题
 */
export const formatUploadFormData = async (params: Record<string, UploadParamType | ParamType[]>): Promise<FormData> => {
    const formData = new FormData();
    for (const key of Object.keys(params)) {
        const value = params[key];
        if (value === undefined || value === null) {
            continue;
        }
        if (Array.isArray(value)) {
            value.forEach(v => formData.append(key, formatParamValue(v)))
        } else {
            if (value instanceof File) {
                formData.append(key, value, value.name);
                continue;
            }
            if (value instanceof Blob) {
                formData.append(key, value, "blob");
                continue;
            }
            if (value instanceof HTMLImageElement) {
                formData.append(key, await getBlobFromImage(value), "image");
                continue;
            }
            if (value instanceof HTMLCanvasElement) {
                formData.append(key, await getBlobFromCanvas(value), "canvas");
                continue;
            }
            formData.append(key, formatParamValue(value));
        }
    }
    return formData;
}


const getBlobFromImage = async (image: HTMLImageElement): Promise<Blob> => {
    const canvas = document.createElement("canvas");
    canvas.width = image.width;
    canvas.height = image.height;
    const ctx = canvas.getContext("2d");
    if (!ctx) {
        throw new Error("canvas 2d context is null");
    }
    ctx.drawImage(image, 0, 0, image.width, image.height);
    return getBlobFromCanvas(canvas);
}

const getBlobFromCanvas = async (canvas: HTMLCanvasElement): Promise<Blob> => {
    return new Promise((resolve, reject) => {
        canvas.toBlob((blob) => {
            if (!blob) {
                reject(new Error("canvas to blob failed"));
            } else {
                resolve(blob);
            }
        });
    });
}

export const getFileNameFromContentDisposition = (contentDisposition: string = ""): string | undefined => {
    const fileNameMatch = contentDisposition.match(/filename\*?=['"]?([^;\r\n"]+)/i);
    if (fileNameMatch && fileNameMatch[1]) {
        return decodeURIComponent(fileNameMatch[1].replace(/['"]/g, ''));
    }
};