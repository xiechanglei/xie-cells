import type {RouteObject} from 'react-router';

import {lazy, Suspense} from 'react';
import {Outlet, redirect} from 'react-router-dom';
import {varAlpha} from 'minimal-shared/utils';

import Box from '@mui/material/Box';
import LinearProgress, {linearProgressClasses} from '@mui/material/LinearProgress';

import {AuthLayout} from '@/layouts/auth';
import {DashboardLayout} from '@/layouts/dashboard';
import {authApi} from "@/server-api/auth";

// ----------------------------------------------------------------------

export const DashboardPage = lazy(() => import('@/pages/dashboard'));
export const SignInPage = lazy(() => import('@/pages/sign-in'));
export const Page404 = lazy(() => import('@/pages/page-not-found'));

const renderFallback = () => (
    <Box
        sx={{
            display: 'flex',
            flex: '1 1 auto',
            alignItems: 'center',
            justifyContent: 'center',
        }}
    >
        <LinearProgress
            sx={{
                width: 1,
                maxWidth: 320,
                bgcolor: (theme) => varAlpha(theme.vars!.palette.text.primaryChannel, 0.16),
                [`& .${linearProgressClasses.bar}`]: {bgcolor: 'text.primary'},
            }}
        />
    </Box>
);

// eslint-disable-next-line react-refresh/only-export-components
export const routesSection: RouteObject[] = [
    {
        element: (
            <DashboardLayout>
                <Suspense fallback={renderFallback()}>
                    <Outlet/>
                </Suspense>
            </DashboardLayout>
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
    },
    {
        path: 'sign-in',
        element: (
            <AuthLayout>
                <SignInPage/>
            </AuthLayout>
        ),
    },
    {
        path: '404',
        element: <Page404/>,
    },
    {path: '*', element: <Page404/>},
];
