import {CreateAxiosDefaults} from "axios";
import {resource_api_root} from "@/config/config";

/** axios的默认配置 */
export const axiosConfig: CreateAxiosDefaults = {
    baseURL: resource_api_root,
    timeout: 30 * 1000,
    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
}