// --------------------Auth Routes--------------------
import {RouteObject} from "react-router";
import {AuthLayout} from "@/layouts/auth";
import {lazy} from "react";


export const SignInPage = lazy(() => import('@/pages/sign-in'));

// --------------------Auth Routes--------------------
const SignInRoute: RouteObject = {
    path: 'sign-in',
    element: (
        <AuthLayout>
            <SignInPage/>
        </AuthLayout>
    ),
}

export default [SignInRoute]