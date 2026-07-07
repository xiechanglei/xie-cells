import {lazy} from "react";
import {RouteObject, LoaderFunction} from "react-router-dom";

export const routes: RouteObject[] = [
    // 主页
    {path: "/", Component: lazy(() => import("@/pages/Main"))},
]


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