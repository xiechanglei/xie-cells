import {POST} from "@/request";

const token_name = "xie-tools-simple-auth-token"
/**
 * 登陆接口
 * @param username 用户名
 * @param password 密码
 */
const login = (username: string, password: string) => {
    return POST<string>("/simple-auth/login", {username, password})
}

/**
 * 判断本地是否存在token
 */
const hasToken = () => {
    const token = localStorage.getItem(token_name);
    return token !== null && token !== "";
}

/**
 * 获取本地token
 */
const getTokenInfo = () => {
    return {
        key: token_name,
        value: localStorage.getItem(token_name) || "",
    }
}


/**
 * 设置token
 */
const setToken = (token: string) => {
    // 写入cookie,保持图片等资源的访问
    // document.cookie = `${token_name}=${token}; path=/; max-age=31536000; SameSite=Lax`;
    localStorage.setItem(token_name, token);
}
export const authApi = {login, hasToken, getTokenInfo, setToken}