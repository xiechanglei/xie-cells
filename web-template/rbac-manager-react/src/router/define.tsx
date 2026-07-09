import {lazy} from "react";
import {RouteObject, LoaderFunction} from "react-router-dom";

export const routes: RouteObject[] = [
    // 主页
    {path: "/", Component: lazy(() => import("@/pages/Main"))},
]

/**
 * 为路由添加一个loader加载器，使每次路由跳转的时候都执行loader函数，可以用于权限校验等场景
 * @param loader
 */
export const withLoader = (loader: LoaderFunction) => {
    const appendLoaderToRoutes = (_routes: RouteObject[]): RouteObject[] => {
        return _routes.map(route => {
            if (route.children) {
                route.children = appendLoaderToRoutes(route.children);
            }
            return {...route, loader};
        });
    }
    return appendLoaderToRoutes(routes)
}