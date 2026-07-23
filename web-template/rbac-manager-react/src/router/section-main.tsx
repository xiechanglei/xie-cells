import {Outlet, RouteObject} from "react-router";
import {MainFallback, MainLayout} from "@/layouts/main";
import {lazy, Suspense} from "react";
import {authApi} from "@/server-api/auth";
import {redirect} from "react-router-dom";

export const DashboardPage = lazy(() => import('@/pages/dashboard'));

// --------------------Main Routes--------------------
const MainRoute: RouteObject = {
    element: (
        <MainLayout>
            <Suspense fallback={<MainFallback/>}>
                <Outlet/>
            </Suspense>
        </MainLayout>
    ),
    children: [
        {index: true, element: <DashboardPage/>},
    ],
    loader: async () => {
        if (!authApi.hasToken()) {
            return redirect("/sign-in");
        }
        return null;
    },
}

export default [MainRoute]