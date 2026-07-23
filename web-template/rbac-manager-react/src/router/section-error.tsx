// --------------------Error Routes--------------------
import {RouteObject} from "react-router";
import {lazy} from "react";

export const Page404 = lazy(() => import('@/pages/page-not-found'));


// --------------------Error Routes--------------------
const Error404Route: RouteObject = {path: '404', element: <Page404/>}
const ErrorWildcardRoute: RouteObject = {path: '*', element: <Page404/>}

export default [Error404Route, ErrorWildcardRoute]
