import {createHashRouter, redirect, RouteObject, RouterProvider} from 'react-router-dom'
import {withLoader} from "@/router/define";

/**
 * 验证用户身份
 */
const checkToken = ()=>{
    return true;
}

/**
 * loader,这里用于简单判断用户是否登录，页面内部用户调用接口如果出现401，就直接跳转到登录页面
 * 这里简单的判断一下本地的sessionStorage中是否有token，如果没有则跳转到登录页面
 */
const loader: RouteObject["loader"] = async (ol) => {
    // 如果是/login的路由，则不需要进行登陆检查
    const pathName = new URL(ol.request.url).pathname
    // 检查token是否存在，判断之后是否需要跳转到登录页面
    if (pathName !== "/login" && !checkToken()) {
        // 如果没有token，则跳转到登录页面
        return redirect("/login");
    }
    return null;
}

/**
 * 创建hash路由
 */
const router = createHashRouter(withLoader(loader))

const navigate = async (to: string) => {
    await router.navigate(to, {replace: true})
}

export {router, RouterProvider, navigate}